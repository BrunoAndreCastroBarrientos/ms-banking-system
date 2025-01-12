package com.nttdata.bootcamp.ms.banking.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.AccountType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.AccountRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.AccountResponse;
import com.nttdata.bootcamp.ms.banking.entity.Account;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.mapper.AccountMapper;
import com.nttdata.bootcamp.ms.banking.repository.AccountRepository;
import com.nttdata.bootcamp.ms.banking.service.AccountService;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * Implementación del servicio de cuentas. Proporciona operaciones
 * para crear, actualizar, consultar y cerrar cuentas.
 *
 * <p>Esta clase gestiona las cuentas de los clientes, validando
 * antes de la creación si se cumplen ciertos requisitos
 * como tipo de cliente, deuda pendiente o la existencia de una
 * tarjeta de crédito asociada (para clientes VIP o PYME).
 * Además, permite actualizar datos de las cuentas, consultar
 * todas las cuentas de un cliente o todas las cuentas en general,
 * y cerrar cuentas si el saldo es cero.</p>
 *
 * @version 1.1
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private final AccountMapper accountMapper;
  private final WebClient webClient;

  @Value("${service.customers.url}")
  private String customersServiceUrl;

  @Value("${service.credits.url}")
  private String creditsServiceUrl;

  @Value("${service.creditcards.url}")
  private String creditCardsServiceUrl;


  @Override
  public Mono<AccountResponse> createAccount(AccountRequest request) {
    Account account = accountMapper.toEntity(request);
    return validateBeforeCreating(account)
        .then(accountRepository.save(account))
        .map(accountMapper::toResponse);
  }

  @Override
  public Mono<AccountResponse> updateAccount(String accountId, AccountRequest request) {
    return accountRepository.findById(accountId)
        .switchIfEmpty(Mono.error(new ApiValidateException("Account not found: " + accountId)))
        .flatMap(existing -> {
          existing.setTransactionsAllowed(request.getTransactionsAllowed());
          existing.setMaintenanceFee(request.getMaintenanceFee());
          existing.setCutoffDate(request.getCutoffDate());
          return accountRepository.save(existing);
        })
        .map(accountMapper::toResponse);
  }

  @Override
  public Mono<Void> closeAccount(String accountId) {
    return accountRepository.findById(accountId)
        .switchIfEmpty(Mono.error(new ApiValidateException("Account not found: " + accountId)))
        .flatMap(account -> {
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
        .map(accountMapper::toResponse);
  }

  @Override
  public Flux<AccountResponse> getByCustomerId(String customerId) {
    return accountRepository.findByCustomerId(customerId)
        .map(accountMapper::toResponse);
  }

  @Override
  public Flux<AccountResponse> getAll() {
    return accountRepository.findAll()
        .map(accountMapper::toResponse);
  }

  @Override
  public Mono<BigDecimal> debit(String accountId, BigDecimal amount) {
    return accountRepository.findById(accountId)
        .switchIfEmpty(Mono.error(new ApiValidateException("Account not found.")))
        .flatMap(account -> {
          if (account.getBalance().compareTo(amount) < 0) {
            return Mono.error(new ApiValidateException("Insufficient funds."));
          }
          account.setBalance(account.getBalance().subtract(amount));
          return accountRepository.save(account)
              .map(updatedAccount -> updatedAccount.getBalance());
        });
  }


  private Mono<Void> validateBeforeCreating(Account account) {
    return getCustomerTypeAndSubType(account.getCustomerId())
        .flatMap(tuple -> {
          String customerType = tuple.getT1();
          String subType = tuple.getT2();

          if (account.getAccountType() == AccountType.SAVINGS && "ENTERPRISE".equalsIgnoreCase(customerType)) {
            return Mono.error(new ApiValidateException("Business customers cannot open SAVINGS accounts."));
          }
          if (account.getAccountType() == AccountType.TIME_DEPOSIT && "ENTERPRISE".equalsIgnoreCase(customerType)) {
            return Mono.error(new ApiValidateException("TIME_DEPOSIT accounts are only for personal customers."));
          }
          if (account.getAccountType() == AccountType.SAVINGS && "VIP".equalsIgnoreCase(subType)) {
            return verifyCreditCardExists(account.getCustomerId());
          }
          if (account.getAccountType() == AccountType.CHECKING && "PYME".equalsIgnoreCase(subType)) {
            account.setMaintenanceFee(BigDecimal.ZERO);
            return verifyEnterpriseCardExists(account.getCustomerId());
          }
          return Mono.empty();
        })
        .then(checkDebt(account.getCustomerId()));
  }

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

  private Mono<Void> verifyCreditCardExists(String customerId) {
    return webClient.get()
        .uri(creditCardsServiceUrl + "/customer/" + customerId )
        .retrieve()
        .bodyToFlux(String.class)
        .switchIfEmpty(Mono.error(new ApiValidateException("No active credit card found for VIP requirement.")))
        .then();
  }

  private Mono<Void> verifyEnterpriseCardExists(String customerId) {
    return webClient.get()
        .uri(creditCardsServiceUrl + "/customer/" + customerId )
        .retrieve()
        .bodyToFlux(String.class)
        .switchIfEmpty(Mono.error(new ApiValidateException("No active enterprise credit card for PYME requirement.")))
        .then();
  }

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

  public List<Object> getCountries() throws Exception {
    try {
      // Usamos WebClient para hacer la solicitud a la API de países usando la URL completa
      Object[] countries = webClient.get()
          .uri("https://restcountries.com/v3.1/all")  // URL completa para obtener todos los países
          .retrieve()
          .bodyToMono(Object[].class)
          .block();  // Usamos .block() para esperar la respuesta sincrónicamente

      // Retornamos una sublista (de índice 1 a 10)
      return List.of(countries).stream().skip(1).limit(9).collect(Collectors.toList());
    } catch (Exception e) {
      throw new Exception("Failed to fetch countries from the API", e);
    }
  }



}
