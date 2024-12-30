package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.entity.Account;
import com.nttdata.bootcamp.ms.banking.entity.Client;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.model.AccountType;
import com.nttdata.bootcamp.ms.banking.model.ClientType;
import com.nttdata.bootcamp.ms.banking.model.request.AccountRequest;
import com.nttdata.bootcamp.ms.banking.model.response.AccountResponse;
import com.nttdata.bootcamp.ms.banking.repository.AccountRepository;
import com.nttdata.bootcamp.ms.banking.repository.ClientRepository;
import com.nttdata.bootcamp.ms.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    @Override
    public Mono<AccountResponse> createAccount(AccountRequest request) {
        // Generar número de cuenta si no está presente
        request.setAccountNumber(
                Optional.ofNullable(request.getAccountNumber()).orElseGet(this::generateAccountNumber));

        // Establecer hasActiveTransactions como false si no está definido
        request.setHasActiveTransactions(
                Optional.ofNullable(request.getHasActiveTransactions()).orElse(false));

        return clientRepository.findById(request.getClientId())
                .switchIfEmpty(Mono.error(new ApiValidateException("Client not found")))
                .flatMap(client -> validateAccountForClientType(client, request.toEntity()))
                .flatMap(accountRepository::save)
                .doOnError(ex -> log.error("Error creating account: {}", ex.getMessage()))
                .onErrorMap(ex -> new ApiValidateException("Error creating account", ex))
                .map(AccountResponse::fromEntity);
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString();
    }

    private Mono<Account> validateAccountForClientType(Client client, Account account) {
        return switch (client.getType()) {
            case PERSONAL -> validatePersonalClientAccount(account);
            case EMPRESARIAL -> validateBusinessClientAccount(account);
            default -> Mono.error(new ApiValidateException("Invalid client type"));
        };
    }

    private Mono<Account> validatePersonalClientAccount(Account account) {
        return accountRepository.findByClientIdAndType(account.getClientId(), account.getType())
                .hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new ApiValidateException("Client already has this type of account"))
                        : Mono.just(account));
    }

    private Mono<Account> validateBusinessClientAccount(Account account) {
        if (account.getType() == AccountType.AHORRO || account.getType() == AccountType.PLAZO_FIJO) {
            return Mono.error(new ApiValidateException("Business clients cannot have savings or fixed-term accounts"));
        }
        return Mono.just(account);
    }

    @Override
    public Mono<AccountResponse> getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new ApiValidateException("Account not found")))
                .map(AccountResponse::fromEntity);
    }

    @Override
    public Mono<Void> deleteAccountById(String accountId) {
        return accountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new ApiValidateException("Account not found")))
                .flatMap(account -> accountRepository.deleteById(accountId));
    }

    @Override
    public Mono<AccountResponse> updateAccount(String accountId, AccountRequest account) {
        return accountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new ApiValidateException("Account not found")))
                .flatMap(existingAccount -> {
                    existingAccount.setBalance(account.getBalance());
                    existingAccount.setType(account.getType());
                    return accountRepository.save(existingAccount);
                })
                .map(AccountResponse::fromEntity);
    }

    @Override
    public Mono<Double> getAccountBalance(String accountId) {
        return accountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new ApiValidateException("Account not found")))
                .map(Account::getBalance);
    }

    @Override
    public Mono<Account> depositToAccount(String accountId, double amount) {
        return accountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new ApiValidateException("Account not found")))
                .flatMap(account -> {
                    if (amount <= 0) {
                        return Mono.error(new ApiValidateException("Deposit amount must be greater than zero"));
                    }
                    account.setBalance(account.getBalance() + amount);
                    return accountRepository.save(account);
                });
    }

    @Override
    public Mono<Account> withdrawFromAccount(String accountId, double amount) {
        return accountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new ApiValidateException("Account not found")))
                .flatMap(account -> {
                    if (amount <= 0) {
                        return Mono.error(new ApiValidateException("Withdrawal amount must be greater than zero"));
                    }
                    if (account.getBalance() < amount) {
                        return Mono.error(new ApiValidateException("Insufficient balance"));
                    }
                    account.setBalance(account.getBalance() - amount);
                    return accountRepository.save(account);
                });
    }
}
