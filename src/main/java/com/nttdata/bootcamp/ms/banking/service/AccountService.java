package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.AccountType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.AccountRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.AccountResponse;
import com.nttdata.bootcamp.ms.banking.entity.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface AccountService {
  Mono<AccountResponse> createAccount(AccountRequest request);
  Flux<AccountResponse> getAllAccounts();
  Mono<AccountResponse> getAccountById(String id);
  Flux<AccountResponse> getAccountsByCustomerId(String customerId);
  Mono<AccountResponse> updateAccount(String id, AccountRequest request);
  Mono<Void> deleteAccount(String id);
  Flux<AccountResponse> getAccountsByAccountType(AccountType accountType);

  Flux<AccountResponse> getAccountsByBalanceGreaterThan(BigDecimal balance);

  Flux<AccountResponse> getAccountsByOpeningDateBetween(LocalDateTime startDate, LocalDateTime endDate);

  Flux<AccountResponse> getAccountsByStatus(RecordStatus status);
}




