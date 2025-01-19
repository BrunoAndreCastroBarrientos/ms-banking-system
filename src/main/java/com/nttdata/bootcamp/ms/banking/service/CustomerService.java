package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.dto.request.CustomerRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CustomerResponse;
import com.nttdata.bootcamp.ms.banking.entity.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
  Mono<CustomerResponse> createCustomer(CustomerRequest request);
  Flux<CustomerResponse> getAllCustomer();
  Mono<CustomerResponse> getCustomerById(String id);
  Mono<CustomerResponse> updateCustomer(String id, CustomerRequest request);
  Mono<Void> deleteCustomer(String id);
}

