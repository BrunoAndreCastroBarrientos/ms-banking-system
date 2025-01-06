package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.dto.request.AccountRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.AccountResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
  Mono<AccountResponse> createAccount(AccountRequest request);

  Mono<AccountResponse> updateAccount(String accountId, AccountRequest request);

  Mono<Void> closeAccount(String accountId);

  Mono<AccountResponse> getById(String accountId);

  Flux<AccountResponse> getByCustomerId(String customerId);

  Flux<AccountResponse> getAll();
}




