package com.nttdata.bootcamp.ms.banking.repository;

import com.nttdata.bootcamp.ms.banking.entity.BootCoinTransaction;
import com.nttdata.bootcamp.ms.banking.entity.Credit;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Repository
public interface BootCoinRepository extends ReactiveMongoRepository<BootCoinTransaction, String> {

    @Query("{ 'phoneNumber': ?0, 'transactionType': 'BUY' }")
    Flux<BootCoinTransaction> findBootCoinBuysByPhoneNumber(String phoneNumber);

    @Query("{ 'phoneNumber': ?0, 'transactionType': 'SELL' }")
    Flux<BootCoinTransaction> findBootCoinSellsByPhoneNumber(String phoneNumber);

    default Mono<BigDecimal> findUserBootCoinBalance(String phoneNumber) {
        return findBootCoinBuysByPhoneNumber(phoneNumber)
            .map(BootCoinTransaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .zipWith(findBootCoinSellsByPhoneNumber(phoneNumber)
                .map(BootCoinTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add), BigDecimal::subtract);
    }
}


