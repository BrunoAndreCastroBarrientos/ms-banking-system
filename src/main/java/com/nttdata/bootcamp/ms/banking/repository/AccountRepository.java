package com.nttdata.bootcamp.ms.banking.repository;

import com.nttdata.bootcamp.ms.banking.entity.Account;
import com.nttdata.bootcamp.ms.banking.model.AccountType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
    Mono<Account> findByAccountNumber(String accountNumber);
    Flux<Account> findByClientId(String clientId);
    Mono<Account> findByClientIdAndType(String clientId, AccountType type);
}


