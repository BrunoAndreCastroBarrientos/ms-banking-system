package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.entity.Transaction;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.mapper.TransactionMapper;
import com.nttdata.bootcamp.ms.banking.dto.request.TransactionRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.TransactionResponse;
import com.nttdata.bootcamp.ms.banking.repository.TransactionRepository;
import com.nttdata.bootcamp.ms.banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
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
  private final WebClient.Builder webClientBuilder;

  @Override
  public Mono<TransactionResponse> processTransaction(TransactionRequest request) {
    return validateTransaction(request) // Validar la transacción
        .flatMap(validTransaction -> performTransaction(validTransaction)) // Realizar la transacción
        .flatMap(transaction -> transactionRepository.save(transaction)) // Guardar la transacción
        .map(TransactionMapper::toResponse); // Mapear a la respuesta
  }


  private Mono<Transaction> validateTransaction(TransactionRequest request) {
    // General validation logic (e.g., null checks, format validation)
    if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      return Mono.error(new ApiValidateException("Amount must be greater than zero."));
    }

    // Additional validations based on transaction type
    switch (request.getTransactionType()) {
      case DEPOSIT:
      case WITHDRAWAL:
        return validateAccountTransaction(request);
      case TRANSFER:
        return validateTransfer(request);
      case CREDIT_PAYMENT:
        return validateCreditPayment(request);
      case CREDIT_CARD_PAYMENT:
        return validateCreditCardPayment(request);
      default:
        return Mono.error(new ApiValidateException("Unsupported transaction type."));
    }
  }

  private Mono<Transaction> validateAccountTransaction(TransactionRequest request) {
    return webClientBuilder.build()
        .get()
        .uri("http://account-service/api/accounts/{id}", request.getOriginAccountId())
        .retrieve()
        .bodyToMono(Transaction.class)
        .flatMap(account -> Mono.just(TransactionMapper.toEntity(request)));
  }

  private Mono<Transaction> validateTransfer(TransactionRequest request) {
    return webClientBuilder.build()
        .get()
        .uri("http://account-service/api/accounts/{id}", request.getOriginAccountId())
        .retrieve()
        .bodyToMono(Transaction.class)
        .flatMap(originAccount -> {
          return webClientBuilder.build()
              .get()
              .uri("http://account-service/api/accounts/{id}", request.getDestinationAccountId())
              .retrieve()
              .bodyToMono(Transaction.class)
              .flatMap(destinationAccount -> {
                return Mono.just(TransactionMapper.toEntity(request));
              });
        });
  }

  private Mono<Transaction> validateCreditPayment(TransactionRequest request) {
    return webClientBuilder.build()
        .get()
        .uri("http://credit-service/api/credits/{id}", request.getCreditId())
        .retrieve()
        .bodyToMono(Transaction.class)
        .flatMap(credit -> {
          return Mono.just(TransactionMapper.toEntity(request));
        });
  }

  private Mono<Transaction> validateCreditCardPayment(TransactionRequest request) {
    return webClientBuilder.build()
        .get()
        .uri("http://card-service/api/credit-cards/{id}", request.getCreditCardId())
        .retrieve()
        .bodyToMono(Transaction.class)
        .flatMap(card -> {
          return Mono.just(TransactionMapper.toEntity(request));
        });
  }

  private Mono<Transaction> performTransaction(Transaction request) {
    switch (request.getTransactionType()) {
      case DEPOSIT:
      case WITHDRAWAL:
      case TRANSFER:
        return updateAccountBalances(request);
      case CREDIT_PAYMENT:
        return updateCreditBalance(request);
      case CREDIT_CARD_PAYMENT:
        return updateCreditCardBalance(request);
      default:
        return Mono.error(new ApiValidateException("Unsupported transaction type."));
    }
  }

  private Mono<Transaction> updateAccountBalances(Transaction request) {
    // Call account microservice to update balances
    return webClientBuilder.build()
        .post()
        .uri("http://account-service/api/accounts/balance")
        .bodyValue(request)
        .retrieve()
        .bodyToMono(Void.class)
        .then(Mono.just(request));
  }

  private Mono<Transaction> updateCreditBalance(Transaction request) {
    // Call credit microservice to update credit balance
    return webClientBuilder.build()
        .post()
        .uri("http://credit-service/api/credits/balance")
        .bodyValue(request)
        .retrieve()
        .bodyToMono(Void.class)
        .then(Mono.just(request));
  }

  private Mono<Transaction> updateCreditCardBalance(Transaction request) {
    // Call card microservice to update card balance
    return webClientBuilder.build()
        .post()
        .uri("http://card-service/api/credit-cards/balance")
        .bodyValue(request)
        .retrieve()
        .bodyToMono(Void.class)
        .then(Mono.just(request));
  }

  @Override
  public Mono<TransactionResponse> getById(String id) {
    return transactionRepository.findById(id)
        .map(TransactionMapper::toResponse);
  }

  @Override
  public Flux<TransactionResponse> getAll() {
    return transactionRepository.findAll()
        .map(TransactionMapper::toResponse);
  }

  @Override
  public Flux<TransactionResponse> getByAccountId(String accountId) {
    return transactionRepository.findByOriginAccountId(accountId)
        .map(TransactionMapper::toResponse);
  }

  @Override
  public Flux<TransactionResponse> getByCreditId(String creditId) {
    return transactionRepository.findByCreditId(creditId)
        .map(TransactionMapper::toResponse);
  }

  @Override
  public Flux<TransactionResponse> getByCreditCardId(String creditCardId) {
    return transactionRepository.findByCreditCardId(creditCardId)
        .map(TransactionMapper::toResponse);
  }

  @Override
  public Flux<TransactionResponse> getByDebitCardId(String debitCardId) {
    return transactionRepository.findByDebitCardId(debitCardId)
        .map(TransactionMapper::toResponse);
  }
}