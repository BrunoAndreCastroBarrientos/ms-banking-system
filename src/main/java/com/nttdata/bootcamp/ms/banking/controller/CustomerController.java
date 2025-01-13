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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

  /**
   * Creates a new customer based on the provided request.
   *
   * @param request The customer creation request containing customer details.
   * @return A {@link Mono} containing the response with the created customer details.
   * @throws ApiValidateException if the input data is invalid or incomplete.
   */
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Create a new customer",
      description = "Creates a new customer with the given details.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.CREATED_CODE,
          description = ConstantUtil.OK_MESSAGE),
      @ApiResponse(responseCode = ConstantUtil.ERROR_CODE,
          description = ConstantUtil.ERROR_MESSAGE)
  })
  public Mono<CustomerResponse> create(
      @Valid @RequestBody CustomerRequest request) {
    return customerService.createCustomer(request);
  }

  /**
   * Updates an existing customer's details by their ID.
   *
   * @param id      The unique identifier of the customer to update.
   * @param request The request containing the updated customer details.
   * @return A {@link Mono} containing the updated customer details.
   * @throws ApiValidateException if the customer ID or update data is invalid.
   */
  @PutMapping(value = "/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Update customer",
      description = "Updates an existing customer with the given ID and details.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = ConstantUtil.OK_MESSAGE),
      @ApiResponse(responseCode = ConstantUtil.ERROR_CODE,
          description = ConstantUtil.ERROR_MESSAGE),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Customer not found")
  })
  public Mono<CustomerResponse> update(@PathVariable String id,

                                       @Valid @RequestBody CustomerRequest request) {
    return customerService.updateCustomer(id, request);
  }

  /**
   * Changes the status of an existing customer by their ID.
   *
   * @param id        The unique identifier of the customer whose status needs to be changed.
   * @param newStatus The new status to assign to the customer.
   * @return A {@link Mono} containing the updated customer details with the new status.
   * @throws ApiValidateException if the status change request
   *                              is invalid or the customer is not found.
   */
  @PatchMapping(value = "/{id}/status/{newStatus}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Change customer status",
      description = "Changes the status of a customer by their ID.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = ConstantUtil.OK_MESSAGE),
      @ApiResponse(responseCode = ConstantUtil.ERROR_CODE,
          description = ConstantUtil.ERROR_MESSAGE),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Customer not found")
  })
  public Mono<CustomerResponse> changeStatus(@PathVariable String id,
                                             @PathVariable String newStatus) {
    return customerService.changeStatus(id, newStatus);
  }

  /**
   * Retrieves a customer by their unique ID.
   *
   * @param id The unique identifier of the customer.
   * @return A {@link Mono} containing the customer's details.
   * @throws ApiValidateException if the customer ID is invalid or not found.
   */
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get customer by ID",
      description = "Fetches the details of a customer by their ID.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = ConstantUtil.OK_MESSAGE),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Customer not found")
  })
  public Mono<CustomerResponse> getById(@PathVariable String id) {
    return customerService.getById(id);
  }

  /**
   * Retrieves all customers in the system.
   *
   * @return A {@link Flux} containing a list of all customer details.
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get all customers",
      description = "Fetches all customers in the system.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = ConstantUtil.OK_MESSAGE)
  })
  public Flux<CustomerResponse> getAll() {
    return customerService.getAll();
  }

  /**
   * Retrieves customers by their type.
   *
   * @param customerType The type of the customer (e.g., "regular", "premium").
   * @return A {@link Flux} containing a list of customers with the specified type.
   * @throws ApiValidateException if the customer type is invalid.
   */
  @GetMapping(value = "/type/{customerType}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get customers by type", description = "Fetches all customers with the specified type.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE),
      @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = "Invalid customer type")
  })
  public Flux<CustomerResponse> findByType(@PathVariable String customerType) {
    return customerService.findByType(customerType);
  }

  /**
   * Retrieves customers by their status (e.g., "active", "inactive").
   *
   * @param status The status of the customers to retrieve.
   * @return A {@link Flux} containing a list of customers with the specified status.
   * @throws ApiValidateException if the status is invalid.
   */
  @GetMapping(value = "/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get customers by status", description = "Fetches all customers with the specified status.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE),
      @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = "Invalid status")
  })
  public Flux<CustomerResponse> findByStatus(@PathVariable String status) {
    return customerService.findByStatus(status);
  }
}
