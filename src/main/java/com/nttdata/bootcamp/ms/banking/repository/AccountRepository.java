package com.nttdata.bootcamp.ms.banking.repository;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.AccountType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.entity.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
  Flux<Account> findByAccountType(AccountType accountType);

  Flux<Account> findByCustomerId(String customerId);

  Flux<Account> findByBalanceGreaterThan(BigDecimal balance);

  Flux<Account> findByOpenDateBetween(LocalDateTime startDate, LocalDateTime endDate);

  Flux<Account> findByStatus(RecordStatus status);

}


