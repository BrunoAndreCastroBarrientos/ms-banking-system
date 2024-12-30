package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.entity.Account;
import com.nttdata.bootcamp.ms.banking.entity.Client;
import com.nttdata.bootcamp.ms.banking.model.request.AccountRequest;
import com.nttdata.bootcamp.ms.banking.model.response.AccountResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface AccountService {
    Mono<AccountResponse> createAccount(AccountRequest accountRequest);
    Mono<AccountResponse> updateAccount(String accountId, AccountRequest accountRequest);
    Mono<Void> deleteAccount(String accountId);
    Mono<AccountResponse> getAccountById(String accountId);
    Flux<AccountResponse> getAccountsByClientId(String clientId);

    Mono<AccountResponse> deposit(String accountId, BigDecimal amount);
    Mono<AccountResponse> withdraw(String accountId, BigDecimal amount);
    Mono<Void> transfer(String fromAccountId, String toAccountId, BigDecimal amount);
}




