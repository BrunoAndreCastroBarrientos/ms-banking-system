package com.nttdata.bootcamp.ms.banking.customer.repository;

import com.nttdata.bootcamp.ms.banking.customer.entity.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
  Mono<Customer> findByIdentificationNumber (String identificationNumber);
}


