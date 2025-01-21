package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.AccountType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.AccountRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.AccountResponse;
import com.nttdata.bootcamp.ms.banking.entity.Account;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.mapper.AccountMapper;
import com.nttdata.bootcamp.ms.banking.repository.AccountRepository;
import com.nttdata.bootcamp.ms.banking.repository.CustomerRepository;
import com.nttdata.bootcamp.ms.banking.service.AccountService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
  private final CustomerRepository customerRepository;

  public Mono<AccountResponse> createAccount(AccountRequest request) {
    return customerRepository.findById(request.getCustomerId()) // Busca al cliente por ID
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE))) // Lanza error si no existe
        .flatMap(customer -> {
          Account account = accountMapper.toEntity(request);
          return accountRepository.save(account)
              .map(accountMapper::toResponse);
        });
  }

  public Flux<AccountResponse> getAllAccounts() {
    return accountRepository.findAll()
        .map(accountMapper::toResponse);
  }

  @Cacheable(value = "accounts", key = "#id")
  public Mono<AccountResponse> getAccountById(String id) {
    return accountRepository.findById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .map(accountMapper::toResponse);
  }

  @CachePut(value = "accounts", key = "#id")
  public Mono<AccountResponse> updateAccount(String id, AccountRequest request) {
    return accountRepository.findById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException("Account not found.")))
        .flatMap(existing -> {
          Account updated = accountMapper.toEntity(request);
          updated.setId(existing.getId());
          return accountRepository.save(updated);
        })
        .map(accountMapper::toResponse);
  }

  @CachePut(value = "accounts", key = "#id")
  public Mono<Void> deleteAccount(String id) {
    accountRepository.findById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .flatMap(existing -> {
          existing.setStatus(RecordStatus.INACTIVE);
          accountRepository.save(existing);
          return null;
        });
    return null;
  }

  @Override
  public Flux<AccountResponse> getAccountsByAccountType(AccountType accountType) {
    return accountRepository.findByAccountType(accountType)
        .map(AccountMapper::toResponseStatic);
  }

  @Override
  public Flux<AccountResponse> getAccountsByCustomerId(String customerId) {
    return accountRepository.findByCustomerId(customerId)
        .map(AccountMapper::toResponseStatic);
  }

  @Override
  public Flux<AccountResponse> getAccountsByBalanceGreaterThan(BigDecimal balance) {
    return accountRepository.findByBalanceGreaterThan(balance)
        .map(AccountMapper::toResponseStatic);
  }

  @Override
  public Flux<AccountResponse> getAccountsByOpeningDateBetween(LocalDateTime   startDate, LocalDateTime endDate) {
    return accountRepository.findByOpenDateBetween(startDate, endDate)
        .map(AccountMapper::toResponseStatic);
  }

  @Override
  public Flux<AccountResponse> getAccountsByStatus(RecordStatus status) {
    return accountRepository.findByStatus(status)
        .map(AccountMapper::toResponseStatic);
  }

}
