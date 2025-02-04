package com.nttdata.bootcamp.ms.banking.product.repository;

import com.nttdata.bootcamp.ms.banking.product.entity.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CreditRepository extends ReactiveMongoRepository<Credit, String> {
    Flux<Credit> findByCustomerId(String customerId);
    // Podrías añadir búsquedas por estado, tipo, etc. de ser necesario
}


