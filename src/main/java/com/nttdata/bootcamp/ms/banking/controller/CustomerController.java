package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.dto.request.CustomerRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CustomerResponse;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.service.CustomerService;
import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller for managing customer-related operations.
 * Provides endpoints for creating, updating, retrieving, and modifying customer data.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;

  @PostMapping
  public Mono<CustomerResponse> createCustomer(@RequestBody CustomerRequest request) {
    return customerService.createCustomer(request);
  }

  @GetMapping
  public Flux<CustomerResponse> getAllCustomer() {
    return customerService.getAllCustomer();
  }

  @GetMapping("/{id}")
  public Mono<CustomerResponse> getCustomerById(@PathVariable String id) {
    return customerService.getCustomerById(id);
  }

  @PutMapping("/{id}")
  public Mono<CustomerResponse> updateCustomer(@PathVariable String id, @RequestBody CustomerRequest request) {
    return customerService.updateCustomer(id, request);
  }

  @DeleteMapping("/{id}")
  public Mono<Void> deleteCustomer(@PathVariable String id) {
    return customerService.deleteCustomer(id);
  }
}
