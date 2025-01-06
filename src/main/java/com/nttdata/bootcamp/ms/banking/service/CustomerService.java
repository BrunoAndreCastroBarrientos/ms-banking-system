package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.dto.request.CustomerRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CustomerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
  Mono<CustomerResponse> createCustomer(CustomerRequest request);

  Mono<CustomerResponse> updateCustomer(String id, CustomerRequest request);

  Mono<CustomerResponse> changeStatus(String id, String newStatus);

  Mono<CustomerResponse> getById(String id);

  Flux<CustomerResponse> getAll();

  Flux<CustomerResponse> findByType(String customerType);

  Flux<CustomerResponse> findByStatus(String status);
}

