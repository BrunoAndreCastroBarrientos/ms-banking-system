package com.nttdata.bootcamp.ms.banking.repository;

import com.nttdata.bootcamp.ms.banking.entity.Customer;
import com.nttdata.bootcamp.ms.banking.entity.Wallet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface WalletRepository extends ReactiveCrudRepository<Wallet, Long> {
  Mono<Wallet> findByPhoneNumber(String phoneNumber);
}


