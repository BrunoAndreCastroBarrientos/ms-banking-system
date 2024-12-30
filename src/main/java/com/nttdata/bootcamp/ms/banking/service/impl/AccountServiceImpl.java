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

    public String generateAccountNumber() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").substring(0, 20).toUpperCase();
    }

    private Mono<AccountResponse> validatePersonalAccount(AccountRequest accountRequest) {
        Account account = accountRequest.toEntity();
        account.setAccountNumber(generateAccountNumber());
        return accountRepository.findByClientId(accountRequest.getClientId())
                .collectList()
                .flatMap(accounts -> {
                    if (accountRequest.getType() == AccountType.PLAZO_FIJO || accounts.size() < 1) {
                        return accountRepository.save(account)
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
        Account account = accountRequest.toEntity();
        account.setAccountNumber(generateAccountNumber());
        return accountRepository.save(account)
                .map(AccountResponse::fromEntity);  // Convertir la entidad a AccountResponse
    }

    @Override
    public Mono<AccountResponse> updateAccount(String accountId, AccountRequest accountRequest) {
        return accountRepository.findById(accountId)
                .flatMap(existingAccount -> {
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
                .switchIfEmpty(Mono.error(new ApiValidateException("Cuenta no encontrada"))) // Validamos si la cuenta existe
                .flatMap(account -> accountRepository.delete(account));
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
                    if (account.getTransactionCount() > account.getMovementLimit()) {
                        account.setBalance(account.getBalance().add(amount));
                        // Agregar comisión por transacción adicional
                        BigDecimal commission = new BigDecimal("2.50");  // Ejemplo de comisión
                        account.setBalance(account.getBalance().subtract(commission));
                    } else {
                        account.setBalance(account.getBalance().add(amount));
                    }
                    account.setTransactionCount(account.getTransactionCount() + 1);
                    return accountRepository.save(account)
                            .map(AccountResponse::fromEntity);  // Convertir la entidad a AccountResponse
                })
                .switchIfEmpty(Mono.error(new ApiValidateException("Cuenta no encontrada")));
    }

    @Override
    public Mono<AccountResponse> withdraw(String accountId, BigDecimal amount) {
        return accountRepository.findById(accountId)
                .flatMap(account -> {
                    if (account.getTransactionCount() > account.getMovementLimit()) {
                        if (account.getBalance().compareTo(amount.add(new BigDecimal("2.50"))) < 0) {
                            return Mono.error(new ApiValidateException("Fondos insuficientes para cubrir la comisión"));
                        }
                        account.setBalance(account.getBalance().subtract(amount).subtract(new BigDecimal("2.50")));
                    } else {
                        if (account.getBalance().compareTo(amount) < 0) {
                            return Mono.error(new ApiValidateException("Fondos insuficientes"));
                        }
                        account.setBalance(account.getBalance().subtract(amount));
                    }
                    account.setTransactionCount(account.getTransactionCount() + 1);
                    return accountRepository.save(account)
                            .map(AccountResponse::fromEntity);  // Convertir la entidad a AccountResponse
                })
                .switchIfEmpty(Mono.error(new ApiValidateException("Cuenta no encontrada")));
    }
    @Override
    public Mono<Void> transfer(String fromAccountId, String toAccountId, BigDecimal amount) {
        return accountRepository.findById(fromAccountId)
                .flatMap(fromAccount -> {
                    if (fromAccount.getBalance().compareTo(amount) < 0) {
                        return Mono.error(new ApiValidateException("Fondos insuficientes"));
                    }

                    // Verificar si las cuentas pertenecen al mismo cliente o a un tercero
                    return accountRepository.findById(toAccountId)
                            .flatMap(toAccount -> {
                                fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
                                toAccount.setBalance(toAccount.getBalance().add(amount));
                                return Mono.zip(
                                        accountRepository.save(fromAccount),
                                        accountRepository.save(toAccount)
                                ).then();
                            });
                })
                .switchIfEmpty(Mono.error(new ApiValidateException("Cuenta no encontrada")));
    }

}
