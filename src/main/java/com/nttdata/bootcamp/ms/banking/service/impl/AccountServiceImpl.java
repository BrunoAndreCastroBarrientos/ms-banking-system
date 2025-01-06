package com.nttdata.bootcamp.ms.banking.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.nttdata.bootcamp.ms.banking.entity.Account;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.mapper.AccountMapper;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.AccountType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.AccountRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.AccountResponse;
import com.nttdata.bootcamp.ms.banking.repository.AccountRepository;
import com.nttdata.bootcamp.ms.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private final AccountMapper accountMapper;

  // URLs de otros microservicios (Clientes, Créditos, Tarjetas)
  @Value("${service.customers.url}")
  private String customersServiceUrl;
  @Value("${service.credits.url}")
  private String creditsServiceUrl;
  @Value("${service.cards.url}")
  private String cardsServiceUrl;

  private final WebClient webClient = WebClient.create();

  @Override
  public Mono<AccountResponse> createAccount(AccountRequest request) {

    // 1. Convertir request a entidad
    Account account = accountMapper.requestToEntity(request);

    // 2. Validar si el cliente puede abrir esta cuenta (tipo, subTipo, etc.)
    return validateBeforeCreating(account)
        // 3. Guardar en DB
        .then(accountRepository.save(account))
        // 4. Convertir a Response
        .map(accountMapper::entityToResponse);
  }

  @Override
  public Mono<AccountResponse> updateAccount(String accountId, AccountRequest request) {
    return accountRepository.findById(accountId)
        .switchIfEmpty(Mono.error(new ApiValidateException("Account not found: " + accountId)))
        .flatMap(existing -> {
          // Actualizar campos permitidos
          existing.setTransactionsAllowed(request.getTransactionsAllowed());
          existing.setMaintenanceFee(request.getMaintenanceFee());
          // cutoffDate, currency, etc. podrían o no permitirse cambiar, según la regla
          existing.setCutoffDate(request.getCutoffDate());

          return accountRepository.save(existing);
        })
        .map(accountMapper::entityToResponse);
  }

  @Override
  public Mono<Void> closeAccount(String accountId) {
    return accountRepository.findById(accountId)
        .switchIfEmpty(Mono.error(new ApiValidateException("Account not found: " + accountId)))
        .flatMap(account -> {
          // Reglas para cierre: Ej. verificar que balance = 0, etc.
          if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            return Mono.error(new ApiValidateException("Cannot close account with non-zero balance."));
          }
          account.setStatus(RecordStatus.CLOSED);
          return accountRepository.save(account);
        })
        .then();
  }

  @Override
  public Mono<AccountResponse> getById(String accountId) {
    return accountRepository.findById(accountId)
        .map(accountMapper::entityToResponse);
  }

  @Override
  public Flux<AccountResponse> getByCustomerId(String customerId) {
    return accountRepository.findAll()
        .filter(a -> a.getCustomerId().equals(customerId))
        .map(accountMapper::entityToResponse);
    // Podría usarse un método en el repositorio, ej: findByCustomerId
  }

  @Override
  public Flux<AccountResponse> getAll() {
    return accountRepository.findAll()
        .map(accountMapper::entityToResponse);
  }

  /**
   * Valida las reglas antes de crear la cuenta:
   * - Tipo/subtipo de cliente (obtenido del microservicio de Clientes).
   * - Deuda vencida (microservicio de Créditos).
   * - Disponibilidad de tarjeta (microservicio de Tarjetas) si es VIP, etc.
   */
  private Mono<Void> validateBeforeCreating(Account account) {
    return getCustomerTypeAndSubType(account.getCustomerId())
        .flatMap(tuple -> {
          String customerType = tuple.getT1();  // "PERSONAL" o "ENTERPRISE"
          String subType = tuple.getT2();       // "STANDARD", "VIP", "PYME"

          // Validar restricciones de tipo de cuenta
          if (account.getAccountType() == AccountType.SAVINGS
              && "ENTERPRISE".equalsIgnoreCase(customerType)) {
            return Mono.error(new ApiValidateException(
                "Business customers cannot open SAVINGS accounts."));
          }
          if (account.getAccountType() == AccountType.TIME_DEPOSIT
              && "ENTERPRISE".equalsIgnoreCase(customerType)) {
            return Mono.error(new ApiValidateException(
                "TIME_DEPOSIT accounts are only for personal customers."));
          }

          // Validar cuenta VIP
          if (account.getAccountType() == AccountType.SAVINGS
              && "VIP".equalsIgnoreCase(subType)) {
            // Verificar tarjeta de crédito
            return verifyCreditCardExists(account.getCustomerId());
          }

          // Validar cuenta corriente para PYME (exenta de comisión)
          if (account.getAccountType() == AccountType.CHECKING
              && "PYME".equalsIgnoreCase(subType)) {
            account.setMaintenanceFee(BigDecimal.ZERO); // Sin comisión
            // Verificar si ya tiene tarjeta empresarial
            return verifyEnterpriseCardExists(account.getCustomerId());
          }

          return Mono.empty();
        })
        // Validar deuda vencida
        .then(checkDebt(account.getCustomerId()));
  }

  /**
   * Llama al microservicio de Clientes para obtener su CustomerType y Profile.
   */
  private Mono<Tuple2<String, String>> getCustomerTypeAndSubType(String customerId) {
    return webClient.get()
        .uri(customersServiceUrl + "/api/customers/" + customerId)
        .retrieve()
        .bodyToMono(JsonNode.class)
        .map(response -> {
          String customerType = response.path("customerType").asText();
          String subType = response.path("subType").asText();
          return Tuples.of(customerType, subType);
        });
  }


  /**
   * Llama al microservicio de Tarjetas para verificar si existe al menos una tarjeta activa,
   * cuando la cuenta a abrir es VIP (Personal).
   */
  private Mono<Void> verifyCreditCardExists(String customerId) {
    return webClient.get()
        .uri(cardsServiceUrl + "/cards/customer/" + customerId + "/active")
        .retrieve()
        .bodyToFlux(String.class) // Suponiendo que retorne IDs de tarjetas
        .switchIfEmpty(Mono.error(new ApiValidateException("No active credit card found for VIP requirement")))
        .then();
  }

  /**
   * Para cliente empresarial PYME, verificar que tenga tarjeta empresarial activa.
   */
  private Mono<Void> verifyEnterpriseCardExists(String customerId) {
    return webClient.get()
        .uri(cardsServiceUrl + "/cards/customer/" + customerId + "/active?type=ENTERPRISE")
        .retrieve()
        .bodyToFlux(String.class)
        .switchIfEmpty(Mono.error(new ApiValidateException("No active enterprise credit card for PYME requirement")))
        .then();
  }

  /**
   * Consulta al microservicio de Créditos si existe deuda vencida (true/false).
   * Si es true, se lanza excepción para rechazar la apertura de cuenta.
   */
  private Mono<Void> checkDebt(String customerId) {
    return webClient.get()
        .uri(creditsServiceUrl + "/credits/debts/pending/" + customerId)
        .retrieve()
        .bodyToMono(Boolean.class)
        .flatMap(hasDebt -> {
          if (Boolean.TRUE.equals(hasDebt)) {
            return Mono.error(new ApiValidateException("Customer has an overdue debt. Cannot open account."));
          }
          return Mono.empty();
        });
  }
}