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
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    @Override
    public Mono<AccountResponse> createAccount(AccountRequest request) {
        // Validar si el accountNumber es null y generar uno único si es necesario
        if (request.getAccountNumber() == null) {
            request.setAccountNumber(generateAccountNumber()); // Método para generar el número de cuenta
        }

        // Si 'hasActiveTransactions' es null, se establece como false
        if (request.getHasActiveTransactions() == null) {
            request.setHasActiveTransactions(false);
        }

        return clientRepository.findById(request.getClientId())
                .switchIfEmpty(Mono.error(new ApiValidateException("Client not found"))) // Si no se encuentra el cliente, lanzar un error
                .flatMap(client -> validateAccountForClientType(client, request.toEntity())) // Validaciones dependiendo del tipo de cliente
                .flatMap(accountRepository::save) // Si las validaciones pasan, guardar la cuenta
                .doOnError(ex -> System.out.println("Error details: " + ex.getMessage())) // Captura de error detallado
                .onErrorMap(ex -> new ApiValidateException("Error creating account", ex))
                .map(AccountResponse::fromEntity); // Manejo global de excepciones
    }

    // Método para generar un número de cuenta único (puedes implementarlo según tu lógica)
    private String generateAccountNumber() {
        return UUID.randomUUID().toString();
    }


    // Validar la cuenta según el tipo de cliente
    private Mono<Account> validateAccountForClientType(Client client, Account account) {
        if (client.getType() == ClientType.PERSONAL) {
            return validatePersonalClientAccount(account);
        } else if (client.getType() == ClientType.EMPRESARIAL) {
            return validateBusinessClientAccount(account);
        } else {
            return Mono.error(new ApiValidateException("Invalid client type"));
        }
    }

    // Validaciones para clientes personales
    private Mono<Account> validatePersonalClientAccount(Account account) {
        return accountRepository.findByClientIdAndType(account.getClientId(), account.getType())
                .hasElement() // Verifica si ya existe una cuenta del mismo tipo para el cliente
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new ApiValidateException("Client already has this type of account"));
                    }
                    return Mono.just(account); // Si no existe, se permite la creación de la cuenta
                });
    }

    // Validaciones para clientes empresariales
    private Mono<Account> validateBusinessClientAccount(Account account) {
        // No permiten cuentas de ahorro o plazo fijo
        if (account.getType() == AccountType.AHORRO || account.getType() == AccountType.PLAZO_FIJO) {
            return Mono.error(new ApiValidateException("Business clients cannot have savings or fixed-term accounts"));
        }
        return Mono.just(account); // Si es una cuenta válida, la devuelve
    }

    // Obtener cuenta por número de cuenta
    @Override
    public Mono<AccountResponse> getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new ApiValidateException("Account not found")))
                .map(AccountResponse::fromEntity);
    }

    // Eliminar cuenta por id
    @Override
    public Mono<Void> deleteAccountById(String accountId) {
        return accountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new ApiValidateException("Account not found")))
                .flatMap(account -> accountRepository.deleteById(accountId));
    }

    // Actualizar cuenta
    @Override
    public Mono<AccountResponse> updateAccount(String accountId, AccountRequest account) {
        return accountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new ApiValidateException("Account not found")))
                .flatMap(existingAccount -> {
                    existingAccount.setBalance(account.getBalance());
                    existingAccount.setType(account.getType());
                    return accountRepository.save(existingAccount)
                            .map(AccountResponse::fromEntity);
                });
    }

    // Consultar saldo de cuenta
    @Override
    public Mono<Double> getAccountBalance(String accountId) {
        return accountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new ApiValidateException("Account not found")))
                .map(Account::getBalance);
    }

    // Realizar un depósito en una cuenta
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

    // Realizar un retiro de una cuenta
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

