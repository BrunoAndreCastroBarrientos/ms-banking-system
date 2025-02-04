package com.nttdata.bootcamp.ms.banking.customer.controller;

import com.nttdata.bootcamp.ms.banking.customer.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.customer.dto.request.CustomerRequest;
import com.nttdata.bootcamp.ms.banking.customer.dto.response.CustomerResponse;
import com.nttdata.bootcamp.ms.banking.customer.entity.Customer;
import com.nttdata.bootcamp.ms.banking.customer.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.customer.mapper.CustomerMapper;
import com.nttdata.bootcamp.ms.banking.customer.repository.CustomerRepository;
import com.nttdata.bootcamp.ms.banking.customer.service.CustomerService;
import com.nttdata.bootcamp.ms.banking.customer.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@Tag(name = "Customer Controller", description = "Endpoints for managing customers")
public class CustomerController {

  private final CustomerService customerService;

  /**
   * Endpoint to create a new customer.
   *
   * @param request the customer request details
   * @return the created customer details
   */
  @Operation(summary = "Create Customer", description = "Creates a new customer based on the provided details.")
  @ApiResponse(responseCode = ConstantUtil.CREATED_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = ConstantUtil.ERROR_MESSAGE)
  @PostMapping
  public Mono<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request) {
    return customerService.createCustomer(request);
  }

  /**
   * Endpoint to retrieve all customers.
   *
   * @return a list of all customers
   */
  @Operation(summary = "Get All Customers", description = "Retrieves a list of all customers.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @GetMapping
  public Flux<CustomerResponse> getAllCustomer() {
    return customerService.getAllCustomer();
  }

  /**
   * Endpoint to retrieve a customer by its ID.
   *
   * @param id the customer ID
   * @return the details of the customer
   */
  @Operation(summary = "Get Customer By ID", description = "Retrieves a customer by its unique ID.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = ConstantUtil.NOT_FOUND_MESSAGE)
  @GetMapping("/{id}")
  public Mono<CustomerResponse> getCustomerById(@PathVariable String id) {
    return customerService.getCustomerById(id);
  }

  /**
   * Endpoint to update a customer.
   *
   * @param id      the customer ID
   * @param request the updated customer details
   * @return the updated customer details
   */
  @Operation(summary = "Update Customer", description = "Updates an existing customer with the provided details.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = ConstantUtil.NOT_FOUND_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = ConstantUtil.ERROR_MESSAGE)
  @PutMapping("/{id}")
  public Mono<CustomerResponse> updateCustomer(@PathVariable String id, @Valid @RequestBody CustomerRequest request) {
    return customerService.updateCustomer(id, request);
  }

  /**
   * Endpoint to delete a customer.
   *
   * @param id the customer ID
   */
  @Operation(summary = "Delete Customer", description = "Deletes a customer by its unique ID.")
  @ApiResponse(responseCode = ConstantUtil.DELETED_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = ConstantUtil.NOT_FOUND_MESSAGE)
  @DeleteMapping("/{id}")
  public Mono<Void> deleteCustomer(@PathVariable String id) {
    return customerService.deleteCustomer(id);
  }
}

