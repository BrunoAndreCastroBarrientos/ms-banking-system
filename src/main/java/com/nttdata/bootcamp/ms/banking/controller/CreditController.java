package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.dto.request.CreditRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditResponse;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.service.CreditService;
import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller for managing client-related operations.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.1
 */
@RestController
@RequestMapping("/api/credits")
@RequiredArgsConstructor
public class CreditController {

  private final CreditService creditService;

  /**
   * Creates a new credit for a customer.
   *
   * @param request The credit creation request containing customer information and credit details.
   * @return A {@link Mono} containing the response with the created credit details.
   * @throws ApiValidateException if the customer ID is not provided in the request.
   */
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Create a new credit", description = "Creates a new credit for a customer.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.CREATED_CODE,
          description = "Credit created successfully"),
      @ApiResponse(responseCode = ConstantUtil.ERROR_CODE,
          description = "Invalid input data")
  })
  public Mono<CreditResponse> create(
      @Valid @RequestBody CreditRequest request) {
    if (request == null || request.getCustomerId() == null) {
      throw new ApiValidateException("Customer ID is required to create a credit.");
    }
    return creditService.createCredit(request);
  }

  /**
   * Retrieves credit details by its ID.
   *
   * @param creditId The unique identifier of the credit.
   * @return A {@link Mono} containing the credit details.
   * @throws ApiValidateException if the credit ID is invalid or empty.
   */
  @GetMapping(value = "/{creditId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get credit by ID",
      description = "Fetches the details of a credit by its ID.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = "Credit details retrieved successfully"),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Credit not found")
  })
  public Mono<CreditResponse> getById(@PathVariable String creditId) {
    if (creditId == null || creditId.isEmpty()) {
      throw new ApiValidateException("Credit ID must be provided.");
    }
    return creditService.getCreditById(creditId);
  }

  /**
   * Retrieves all credits linked to a specific customer.
   *
   * @param customerId The unique identifier of the customer.
   * @return A {@link Flux} containing the list of credits associated with the given customer.
   * @throws ApiValidateException if the customer ID is invalid or empty.
   */
  @GetMapping(value = "/customer/{customerId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get credits by customer ID",
      description = "Fetches all credits linked to a specific customer.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = "Credits retrieved successfully"),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Customer not found")
  })
  public Flux<CreditResponse> getByCustomerId(@PathVariable String customerId) {
    if (customerId == null || customerId.isEmpty()) {
      throw new ApiValidateException("Customer ID must be provided.");
    }
    return creditService.getCreditsByCustomerId(customerId);
  }

  /**
   * Cancels a credit by its ID.
   *
   * @param creditId The unique identifier of the credit to be canceled.
   * @return A {@link Mono} indicating the completion of the cancellation process.
   * @throws ApiValidateException if the credit ID is invalid or empty.
   */
  @DeleteMapping(value = "/{creditId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Cancel credit", description = "Cancels a credit by its ID.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.DELETED_CODE,
          description = "Credit canceled successfully"),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Credit not found")
  })
  public Mono<Void> cancelCredit(@PathVariable String creditId) {
    if (creditId == null || creditId.isEmpty()) {
      throw new ApiValidateException("Credit ID must be provided.");
    }
    return creditService.cancelCredit(creditId);
  }

  /**
   * Checks if a customer has any overdue debts.
   *
   * @param customerId The unique identifier of the customer.
   * @return A {@link Mono} containing a boolean indicating whether the customer has overdue debts.
   * @throws ApiValidateException if the customer ID is invalid or empty.
   */
  @GetMapping(value = "/debts/pending/{customerId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Check if customer has overdue debts",
      description = "Checks if the customer has overdue debts.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = "Debt status retrieved successfully"),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Customer not found")
  })
  public Mono<Boolean> hasOverdueDebt(@PathVariable String customerId) {
    if (customerId == null || customerId.isEmpty()) {
      throw new ApiValidateException("Customer ID must be provided.");
    }
    return creditService.hasOverdueDebt(customerId);
  }

  /**
   * Applies a payment to a specific credit.
   *
   * @param creditId The unique identifier of the credit.
   * @param amount   The payment amount to be applied.
   * @return A {@link Mono} containing the updated credit details after the payment.
   * @throws ApiValidateException if the credit ID is invalid or empty,
   *                              or if the payment amount is invalid.
   */
  @PostMapping(value = "/{creditId}/payments/{amount}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Apply payment to credit",
      description = "Applies a payment to a specific credit.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = "Payment applied successfully"),
      @ApiResponse(responseCode = ConstantUtil.ERROR_CODE,
          description = "Invalid payment amount")
  })
  public Mono<CreditResponse> applyPayment(@PathVariable String creditId,
                                           @PathVariable Double amount) {
    if (creditId == null || creditId.isEmpty()) {
      throw new ApiValidateException("Credit ID must be provided.");
    }
    if (amount == null || amount <= 0) {
      throw new ApiValidateException("Amount must be a positive value.");
    }
    return creditService.applyPayment(creditId, amount);
  }
}
