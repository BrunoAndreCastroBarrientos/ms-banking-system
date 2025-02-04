package com.nttdata.bootcamp.ms.banking.bootcoin.repository;

import com.nttdata.bootcamp.ms.banking.bootcoin.entity.Wallet;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface WalletRepository extends ReactiveCrudRepository<Wallet, Long> {
  Mono<Wallet> findByPhoneNumber(String phoneNumber);
}


