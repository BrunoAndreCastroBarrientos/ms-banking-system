package com.nttdata.bootcamp.ms.banking.product.repository;

import com.nttdata.bootcamp.ms.banking.product.entity.DebitCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface DebitCardRepository extends ReactiveMongoRepository<DebitCard, String> {
    Flux<DebitCard> findByCustomerId(String customerId);
}


