package com.nttdata.bootcamp.ms.banking.repository;

import com.nttdata.bootcamp.ms.banking.entity.Client;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ClientRepository extends ReactiveMongoRepository<Client, String> {
    Mono<Client> findByEmail(String email);
}


