package com.nttdata.bootcamp.ms.banking.repository;

import com.nttdata.bootcamp.ms.banking.entity.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CreditRepository extends ReactiveMongoRepository<Credit, String> {
    Mono<Credit> findByClientId(String clientId);
}



