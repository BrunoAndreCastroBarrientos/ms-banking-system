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

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    @Override
    public Mono<AccountResponse> createAccount(AccountRequest accountRequest) {
        return clientRepository.findById(accountRequest.getClientId())
                .flatMap(client -> {
                    // Validación de reglas de negocio para clientes personales y empresariales
                    if (client.getType() == ClientType.PERSONAL) {
                        // Validación de cuentas permitidas para clientes personales
                        return validatePersonalAccount(accountRequest);
                    } else if (client.getType() == ClientType.EMPRESARIAL) {
                        // Validación de cuentas permitidas para clientes empresariales
                        return validateBusinessAccount(accountRequest);
                    } else {
                        return Mono.error(new ApiValidateException("Tipo de cliente no permitido"));
                    }
                })
                .switchIfEmpty(Mono.error(new ApiValidateException("Cliente no encontrado")));
    }

    private Mono<AccountResponse> validatePersonalAccount(AccountRequest accountRequest) {
        return accountRepository.findByClientId(accountRequest.getClientId())
                .collectList()
                .flatMap(accounts -> {
                    if (accountRequest.getType() == AccountType.PLAZO_FIJO || accounts.size() < 1) {
                        return accountRepository.save(accountRequest.toEntity())
                                .map(AccountResponse::fromEntity);  // Convertir la entidad a AccountResponse
                    } else {
                        return Mono.error(new ApiValidateException("El cliente personal solo puede tener una cuenta"));
                    }
                });
    }

    private Mono<AccountResponse> validateBusinessAccount(AccountRequest accountRequest) {
        if (accountRequest.getType() == AccountType.AHORRO || accountRequest.getType() == AccountType.PLAZO_FIJO) {
            return Mono.error(new ApiValidateException("El cliente empresarial no puede tener cuentas de ahorro o plazo fijo"));
        }
        return accountRepository.save(accountRequest.toEntity())
                .map(AccountResponse::fromEntity);  // Convertir la entidad a AccountResponse
    }

    @Override
    public Mono<AccountResponse> updateAccount(String accountId, AccountRequest accountRequest) {
        return accountRepository.findById(accountId)
                .flatMap(existingAccount -> {
                    existingAccount.setAccountNumber(accountRequest.getAccountNumber());
                    existingAccount.setType(accountRequest.getType());
                    existingAccount.setBalance(accountRequest.getBalance());
                    existingAccount.setMaintenanceFee(accountRequest.getMaintenanceFee());
                    existingAccount.setMovementLimit(accountRequest.getMovementLimit());
                    existingAccount.setUpdatedAt(accountRequest.getUpdatedAt());
                    return accountRepository.save(existingAccount)
                            .map(AccountResponse::fromEntity);  // Convertir la entidad a AccountResponse
                })
                .switchIfEmpty(Mono.error(new ApiValidateException("Cuenta no encontrada")));
    }

    @Override
    public Mono<Void> deleteAccount(String accountId) {
        return accountRepository.findById(accountId)
                .flatMap(account -> accountRepository.delete(account))
                .switchIfEmpty(Mono.error(new ApiValidateException("Cuenta no encontrada")));
    }

    @Override
    public Mono<AccountResponse> getAccountById(String accountId) {
        return accountRepository.findById(accountId)
                .map(AccountResponse::fromEntity)  // Convertir la entidad a AccountResponse
                .switchIfEmpty(Mono.error(new ApiValidateException("Cuenta no encontrada")));
    }

    @Override
    public Flux<AccountResponse> getAccountsByClientId(String clientId) {
        return accountRepository.findByClientId(clientId)
                .map(AccountResponse::fromEntity);  // Convertir la entidad a AccountResponse
    }

    @Override
    public Mono<AccountResponse> deposit(String accountId, BigDecimal amount) {
        return accountRepository.findById(accountId)
                .flatMap(account -> {
                    account.setBalance(account.getBalance().add(amount));
                    return accountRepository.save(account)
                            .map(AccountResponse::fromEntity);  // Convertir la entidad a AccountResponse
                })
                .switchIfEmpty(Mono.error(new ApiValidateException("Cuenta no encontrada")));
    }

    @Override
    public Mono<AccountResponse> withdraw(String accountId, BigDecimal amount) {
        return accountRepository.findById(accountId)
                .flatMap(account -> {
                    if (account.getBalance().compareTo(amount) < 0) {
                        return Mono.error(new ApiValidateException("Fondos insuficientes"));
                    }
                    account.setBalance(account.getBalance().subtract(amount));
                    return accountRepository.save(account)
                            .map(AccountResponse::fromEntity);  // Convertir la entidad a AccountResponse
                })
                .switchIfEmpty(Mono.error(new ApiValidateException("Cuenta no encontrada")));
    }
}
