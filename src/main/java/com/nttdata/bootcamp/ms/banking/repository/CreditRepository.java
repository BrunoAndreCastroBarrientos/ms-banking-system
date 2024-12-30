package com.nttdata.bootcamp.ms.banking.repository;

import com.nttdata.bootcamp.ms.banking.entity.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditRepository extends ReactiveMongoRepository<Credit, String> {
    Flux<Credit> findByClientId(String clientId);
    Boolean existsByClientId(String clientId);
    Mono<Credit> save(Credit credit);  // Asegúrate de que save devuelva Mono<Credit>
}



