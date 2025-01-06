package com.nttdata.bootcamp.ms.banking.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.nttdata.bootcamp.ms.banking.entity.Transaction;
import com.nttdata.bootcamp.ms.banking.mapper.TransactionMapper;
import com.nttdata.bootcamp.ms.banking.dto.request.TransactionRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.TransactionResponse;
import com.nttdata.bootcamp.ms.banking.repository.TransactionRepository;
import com.nttdata.bootcamp.ms.banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Implementación del servicio de transacciones.
 * Proporciona operaciones para procesar transacciones de depósitos, retiros,
 * transferencias, pagos de créditos y pagos de tarjetas de crédito.
 *
 * <p>Este servicio gestiona la lógica de negocio asociada a transacciones financieras
 * entre cuentas, créditos y tarjetas de crédito. También verifica el estado de las cuentas,
 * los créditos y las tarjetas, y aplica las comisiones necesarias en base
 * a las transacciones realizadas.</p>
 *
 * @version 1.1
 * @author Bruno Andre Castro Barrientos
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;
  private final TransactionMapper transactionMapper;

  // URLs (o nombres de los servicios en Eureka) de otros microservicios:
  @Value("${service.accounts.url}")
  private String accountsServiceUrl;

  @Value("${service.credits.url}")
  private String creditsServiceUrl;

  @Value("${service.cards.url}")
  private String cardsServiceUrl;

  private final WebClient webClient = WebClient.create();

  @Override
  public Mono<TransactionResponse> processTransaction(TransactionRequest request) {
    // 1. Convertimos request a entidad
    Transaction transaction = transactionMapper.requestToEntity(request);

    // 2. Lógica según transactionType
    return handleTransactionLogic(transaction)
        // 3. Guardar registro de transacción
        .flatMap(tx -> transactionRepository.save(tx))
        // 4. Retornar response
        .map(transactionMapper::entityToResponse);
  }

  /**
   * Maneja las reglas de negocio según el tipo de transacción:
   * - DEPOSIT, WITHDRAWAL, TRANSFER
   * - CREDIT_PAYMENT, CREDIT_CARD_PAYMENT
   */
  private Mono<Transaction> handleTransactionLogic(Transaction transaction) {
    switch (transaction.getTransactionType()) {
      case DEPOSIT:
        return handleDeposit(transaction);

      case WITHDRAWAL:
        return handleWithdrawal(transaction);

      case TRANSFER:
        return handleTransfer(transaction);

      case CREDIT_PAYMENT:
        return handleCreditPayment(transaction);

      case CREDIT_CARD_PAYMENT:
        return handleCreditCardPayment(transaction);

      default:
        return Mono.error(new RuntimeException("Tipo de transacción no soportado."));
    }
  }

  /**
   * Depósito a una cuenta:
   * - Verificar que la cuenta esté ACTIVE.
   * - Calcular comisión si se exceden transacciones gratuitas.
   * - Actualizar saldo de la cuenta (microservicio de Cuentas).
   */
  private Mono<Transaction> handleDeposit(Transaction transaction) {
    if (transaction.getDestinationAccountId() == null) {
      return Mono.error(new RuntimeException("No se especifica la cuenta destino para el depósito."));
    }
    return checkAccountActive(transaction.getDestinationAccountId())
        .then(checkTransactionsAllowedAndCommission(transaction.getDestinationAccountId(), transaction))
        .flatMap(tx -> updateAccountBalance(tx.getDestinationAccountId(), tx.getAmount(), true))
        .thenReturn(transaction);
  }

  /**
   * Retiro en cuenta:
   * - Verificar cuenta ACTIVE.
   * - Calcular comisión si exceden transacciones gratuitas.
   * - Debitar el saldo (microservicio de Cuentas).
   */
  private Mono<Transaction> handleWithdrawal(Transaction transaction) {
    if (transaction.getOriginAccountId() == null) {
      return Mono.error(new RuntimeException("No se especifica la cuenta origen para el retiro."));
    }
    return checkAccountActive(transaction.getOriginAccountId())
        .then(checkTransactionsAllowedAndCommission(transaction.getOriginAccountId(), transaction))
        .flatMap(tx -> updateAccountBalance(tx.getOriginAccountId(), tx.getAmount(), false))
        .thenReturn(transaction);
  }

  /**
   * Transferencia entre dos cuentas:
   * - Ambas cuentas deben estar ACTIVE.
   * - Debitar la cuenta origen y acreditar la cuenta destino.
   * - Calcular comisiones si exceden transacciones gratuitas en la cuenta origen.
   */
  private Mono<Transaction> handleTransfer(Transaction transaction) {
    if (transaction.getOriginAccountId() == null || transaction.getDestinationAccountId() == null) {
      return Mono.error(new RuntimeException("Faltan cuentas origen/destino para la transferencia."));
    }
    return checkAccountActive(transaction.getOriginAccountId())
        .then(checkAccountActive(transaction.getDestinationAccountId()))
        .then(checkTransactionsAllowedAndCommission(transaction.getOriginAccountId(), transaction))
        .flatMap(tx -> updateAccountBalance(tx.getOriginAccountId(), tx.getAmount(), false))
        .then(updateAccountBalance(transaction.getDestinationAccountId(), transaction.getAmount(), true))
        .thenReturn(transaction);
  }

  /**
   * Pago de crédito:
   * - Verificar que el crédito esté ACTIVE.
   * - Si hay originAccountId, debitar la cuenta.
   * - Llamar al microservicio de Créditos para "applyPayment".
   */
  private Mono<Transaction> handleCreditPayment(Transaction transaction) {
    if (transaction.getCreditId() == null) {
      return Mono.error(new RuntimeException("No se especifica el crédito a pagar."));
    }
    return checkCreditActive(transaction.getCreditId())
        .then(Mono.defer(() -> {
          // Si hay originAccountId, se descuenta de esa cuenta
          if (transaction.getOriginAccountId() != null) {
            return checkAccountActive(transaction.getOriginAccountId())
                .then(checkTransactionsAllowedAndCommission(transaction.getOriginAccountId(), transaction))
                .flatMap(tx -> updateAccountBalance(tx.getOriginAccountId(), tx.getAmount(), false));
          }
          return Mono.empty();
        }))
        .then(applyCreditPayment(transaction.getCreditId(), transaction.getAmount()))
        .thenReturn(transaction);
  }

  /**
   * Pago de tarjeta de crédito:
   * - Verificar estado de la tarjeta (ACTIVE o BLOCKED). Decidir si se permite pago estando bloqueada.
   * - Debitar la cuenta de origen, si está especificada.
   * - Llamar al microservicio de Tarjetas para incrementar su availableLimit y reducir balance.
   */
  private Mono<Transaction> handleCreditCardPayment(Transaction transaction) {
    if (transaction.getCreditCardId() == null) {
      return Mono.error(new RuntimeException("No se especifica la tarjeta de crédito a pagar."));
    }
    return checkCreditCardStatus(transaction.getCreditCardId())
        .then(Mono.defer(() -> {
          if (transaction.getOriginAccountId() != null) {
            return checkAccountActive(transaction.getOriginAccountId())
                .then(checkTransactionsAllowedAndCommission(transaction.getOriginAccountId(), transaction))
                .flatMap(tx -> updateAccountBalance(tx.getOriginAccountId(), tx.getAmount(), false));
          }
          return Mono.empty();
        }))
        .then(updateCreditCardBalance(transaction.getCreditCardId(), transaction.getAmount(), true))
        .thenReturn(transaction);
  }

  // --------------------------------------------------------------------------------
  // Llamadas y validaciones a otros microservicios usando JsonNode
  // --------------------------------------------------------------------------------

  /**
   * Verifica si la cuenta está en estado ACTIVE.
   */
  private Mono<Void> checkAccountActive(String accountId) {
    return webClient.get()
        .uri(accountsServiceUrl + "/api/accounts/{id}", accountId)
        .retrieve()
        .bodyToMono(JsonNode.class)
        .flatMap(json -> {
          if (json == null || json.isNull()) {
            return Mono.error(new RuntimeException("La cuenta no existe o no se obtuvo información."));
          }
          String status = json.path("status").asText(null); // p.ej. "ACTIVE", "CLOSED", etc.
          if (!"ACTIVE".equalsIgnoreCase(status)) {
            return Mono.error(new RuntimeException("La cuenta no está activa."));
          }
          return Mono.empty();
        });
  }

  /**
   * Verifica y/o aplica comisión si se exceden transacciones gratuitas, etc.
   * - Ejemplo simplificado: se obtienen campos como 'transactionsAllowed' y se decide si cobrar comisión.
   */
  private Mono<Transaction> checkTransactionsAllowedAndCommission(String accountId, Transaction transaction) {
    return webClient.get()
        .uri(accountsServiceUrl + "/api/accounts/{id}", accountId)
        .retrieve()
        .bodyToMono(JsonNode.class)
        .flatMap(json -> {
          if (json == null || json.isNull()) {
            return Mono.error(new RuntimeException("No se encontró la cuenta para calcular comisión."));
          }
          // Ejemplo: transactionAllowed
          int allowed = json.path("transactionsAllowed").asInt(-1);
          if (allowed != -1) {
            // Aquí se debería consultar cuántas transacciones se han hecho en el período
            // y, si excede, agregar comisión a 'transaction'.
            boolean exceed = false; // Lógica real: buscar # transacciones en la BD, etc.
            if (exceed) {
              transaction.setCommission(new BigDecimal("2.00")); // Ejemplo de comisión fija
            }
          }
          return Mono.just(transaction);
        });
  }

  /**
   * Actualiza el saldo de la cuenta:
   * - sign=true => depósito
   * - sign=false => retiro
   */
  private Mono<Void> updateAccountBalance(String accountId, BigDecimal amount, boolean sign) {
    String endpoint = sign ? "/deposit" : "/withdraw";
    return webClient.post()
        .uri(accountsServiceUrl + "/api/accounts/{id}" + endpoint + "?amount={amt}", accountId, amount)
        .retrieve()
        .bodyToMono(Void.class);
  }

  /**
   * Verifica si un crédito está ACTIVE.
   */
  private Mono<Void> checkCreditActive(String creditId) {
    return webClient.get()
        .uri(creditsServiceUrl + "/api/credits/{id}", creditId)
        .retrieve()
        .bodyToMono(JsonNode.class)
        .flatMap(json -> {
          if (json == null || json.isNull()) {
            return Mono.error(new RuntimeException("El crédito no existe."));
          }
          String status = json.path("status").asText(null);
          if (!"ACTIVE".equalsIgnoreCase(status)) {
            return Mono.error(new RuntimeException("El crédito no está activo."));
          }
          return Mono.empty();
        });
  }

  /**
   * Llama a microservicio de Créditos para realizar un pago (reduce outstandingBalance, etc.).
   */
  private Mono<Void> applyCreditPayment(String creditId, BigDecimal amount) {
    return webClient.post()
        .uri(creditsServiceUrl + "/api/credits/{id}/payments/{amt}", creditId, amount)
        .retrieve()
        .bodyToMono(Void.class);
  }

  /**
   * Verifica el estado de la tarjeta de crédito (ACTIVE, BLOCKED, etc.).
   * Si está BLOCKED, decides si permites pago o no.
   */
  private Mono<Void> checkCreditCardStatus(String creditCardId) {
    return webClient.get()
        .uri(cardsServiceUrl + "/api/cards/credit/{id}", creditCardId)
        .retrieve()
        .bodyToMono(JsonNode.class)
        .flatMap(json -> {
          if (json == null || json.isNull()) {
            return Mono.error(new RuntimeException("Tarjeta de crédito no existe."));
          }
          String status = json.path("status").asText(null);
          // Permitir pago aunque esté BLOCKED => Lógica de negocio
          return Mono.empty();
        });
  }

  /**
   * Actualiza la tarjeta de crédito (incrementa availableLimit, reduce balance, etc.)
   * isPayment=true => se trata de un pago
   */
  private Mono<Void> updateCreditCardBalance(String creditCardId, BigDecimal amount, boolean isPayment) {
    return webClient.patch()
        .uri(cardsServiceUrl + "/api/cards/credit/{id}/balance/{amt}/{isPayment}",
            creditCardId, amount, isPayment)
        .retrieve()
        .bodyToMono(Void.class);
  }

  // --------------------------------------------------------------------------------
  // Métodos de consulta sencillos
  // --------------------------------------------------------------------------------
  @Override
  public Mono<TransactionResponse> getById(String id) {
    return transactionRepository.findById(id)
        .map(transactionMapper::entityToResponse);
  }

  @Override
  public Flux<TransactionResponse> getAll() {
    return transactionRepository.findAll()
        .map(transactionMapper::entityToResponse);
  }

  @Override
  public Flux<TransactionResponse> getByAccountId(String accountId) {
    return transactionRepository.findAll()
        .filter(tx -> accountId.equals(tx.getOriginAccountId())
            || accountId.equals(tx.getDestinationAccountId()))
        .map(transactionMapper::entityToResponse);
  }

  @Override
  public Flux<TransactionResponse> getByCreditId(String creditId) {
    return transactionRepository.findAll()
        .filter(tx -> creditId.equals(tx.getCreditId()))
        .map(transactionMapper::entityToResponse);
  }

  @Override
  public Flux<TransactionResponse> getByCreditCardId(String creditCardId) {
    return transactionRepository.findAll()
        .filter(tx -> creditCardId.equals(tx.getCreditCardId()))
        .map(transactionMapper::entityToResponse);
  }

  @Override
  public Flux<TransactionResponse> getByDebitCardId(String debitCardId) {
    return transactionRepository.findAll()
        .filter(tx -> debitCardId.equals(tx.getDebitCardId()))
        .map(transactionMapper::entityToResponse);
  }
}