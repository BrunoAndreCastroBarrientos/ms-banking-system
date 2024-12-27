package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.entity.Account;
import com.nttdata.bootcamp.ms.banking.entity.Client;
import com.nttdata.bootcamp.ms.banking.model.request.AccountRequest;
import com.nttdata.bootcamp.ms.banking.model.response.AccountResponse;
import reactor.core.publisher.Mono;

public interface AccountService {
    Mono<AccountResponse> createAccount(AccountRequest request);

    Mono<AccountResponse> getAccountByNumber(String accountNumber);

    Mono<Void> deleteAccountById(String accountId);

    Mono<AccountResponse> updateAccount(String accountId, AccountRequest request);

    Mono<Double> getAccountBalance(String accountId);

    Mono<Account> depositToAccount(String accountId, double amount);

    Mono<Account> withdrawFromAccount(String accountId, double amount);
}




