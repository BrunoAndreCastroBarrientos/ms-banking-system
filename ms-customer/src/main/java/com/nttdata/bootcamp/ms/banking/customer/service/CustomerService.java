package com.nttdata.bootcamp.ms.banking.customer.service;

import com.nttdata.bootcamp.ms.banking.customer.dto.request.CustomerRequest;
import com.nttdata.bootcamp.ms.banking.customer.dto.response.CustomerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
  Mono<CustomerResponse> createCustomer(CustomerRequest request);
  Flux<CustomerResponse> getAllCustomer();
  Mono<CustomerResponse> getCustomerById(String id);
  Mono<CustomerResponse> updateCustomer(String id, CustomerRequest request);
  Mono<Void> deleteCustomer(String id);
}

