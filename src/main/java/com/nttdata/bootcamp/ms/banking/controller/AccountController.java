package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.dto.request.AccountRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.AccountResponse;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.service.AccountService;
import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing account-related operations.
 *
 * <p>This controller provides endpoints for creating,
 * updating, retrieving, and closing accounts.</p>
 *
 * @version 1.1
 * @author Bruno Andre Castro Barrientos
 */

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  /**
   * Creates a new account.
   *
   * <p>This endpoint creates a new account by providing
   * the necessary details in the request body.</p>
   *
   * @param request the details of the account to create.
   * @return a {@link Mono} with the response containing the newly created account details.
   * @throws ApiValidateException if the account creation request is invalid.
   */
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Create a new account",
      description = "Creates a new account for a customer.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.CREATED_CODE,
          description = "Account created successfully"),
      @ApiResponse(responseCode = ConstantUtil.ERROR_CODE,
          description = "Invalid input data")
  })
  public Mono<AccountResponse> create(@Valid @RequestBody AccountRequest request) {
    if (request == null || request.getCustomerId() == null || request.getAccountType() == null) {
      throw new ApiValidateException(
          "Customer ID and account type are required to create an account."
      );
    }
    return accountService.createAccount(request);
  }

  /**
   * Updates the details of an existing account.
   *
   * <p>This endpoint allows you to update an existing account
   * by providing the account ID and updated details.</p>
   *
   * @param accountId the unique ID of the account to update.
   * @param request the updated account details.
   * @return a {@link Mono} containing the updated account details.
   * @throws ApiValidateException if the account ID or request is invalid.
   */
  @PutMapping(value = "/{accountId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Update account details",
      description = "Updates the details of an existing account.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = "Account updated successfully"),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Account not found"),
      @ApiResponse(responseCode = ConstantUtil.ERROR_CODE,
          description = "Invalid input data")
  })
  public Mono<AccountResponse> update(@PathVariable String accountId,

@Valid @RequestBody AccountRequest request) {
    if (accountId == null || accountId.isEmpty()) {
      throw new ApiValidateException("Account ID must be provided.");
    }
    if (request == null || request.getCustomerId() == null || request.getAccountType() == null) {
      throw new ApiValidateException(
          "Customer ID and account type are required for account update."
      );
    }
    return accountService.updateAccount(accountId, request);
  }

  /**
   * Closes an account by its ID.
   *
   * <p>This endpoint allows you to close an account by providing its unique account ID.</p>
   *
   * @param accountId the unique ID of the account to close.
   * @return a {@link Mono} with a response indicating the account was closed successfully.
   * @throws ApiValidateException if the account ID is invalid or the account does not exist.
   */
  @DeleteMapping(value = "/{accountId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Close an account",
      description = "Closes an existing account by its unique ID.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.DELETED_CODE,
          description = "Account closed successfully"),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Account not found")
  })
  public Mono<Void> closeAccount(@PathVariable String accountId) {
    if (accountId == null || accountId.isEmpty()) {
      throw new ApiValidateException("Account ID must be provided.");
    }
    return accountService.closeAccount(accountId);
  }

  /**
   * Retrieves the details of a specific account by its ID.
   *
   * <p>This endpoint allows you to fetch the details of an account using its unique account ID.</p>
   *
   * @param accountId the unique ID of the account to retrieve.
   * @return a {@link Mono} containing the account details.
   * @throws ApiValidateException if the account ID is invalid or the account does not exist.
   */
  @GetMapping(value = "/{accountId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get account details by ID",
      description = "Fetches the details of a specific account.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = "Account details retrieved successfully"),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Account not found")
  })
  public Mono<AccountResponse> getById(@PathVariable String accountId) {
    if (accountId == null || accountId.isEmpty()) {
      throw new ApiValidateException("Account ID must be provided.");
    }
    return accountService.getById(accountId);
  }

  /**
   * Retrieves all accounts associated with a specific customer.
   *
   * <p>This endpoint allows you to retrieve all accounts
   * linked to a customer using their customer ID.</p>
   *
   * @param customerId the unique ID of the customer whose accounts are to be retrieved.
   * @return a {@link Flux} containing the list of accounts for the given customer.
   * @throws ApiValidateException if the customer ID is invalid or not provided.
   */
  @GetMapping(value = "/customer/{customerId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get accounts by customer ID",
      description = "Fetches all accounts linked to a specific customer.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = "Accounts retrieved successfully"),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Customer not found")
  })
  public Flux<AccountResponse> getByCustomerId(@PathVariable String customerId) {
    if (customerId == null || customerId.isEmpty()) {
      throw new ApiValidateException("Customer ID must be provided.");
    }
    return accountService.getByCustomerId(customerId);
  }

  /**
   * Retrieves all accounts in the system.
   *
   * <p>This endpoint allows you to retrieve all accounts present in the system.</p>
   *
   * @return a {@link Flux} containing the list of all accounts.
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get all accounts",
      description = "Fetches all accounts in the system.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = "All accounts retrieved successfully")
  })
  public Flux<AccountResponse> getAll() {
    return accountService.getAll();
  }
  /**
   * Realiza un débito en una cuenta específica.
   *
   * <p>Este endpoint permite debitar una cantidad específica del saldo de la cuenta indicada.</p>
   *
   * @param accountId el ID único de la cuenta.
   * @param amount la cantidad a debitar.
   * @return un {@link Mono} que contiene el saldo restante después del débito.
   * @throws ApiValidateException si el ID de la cuenta es inválido o no tiene fondos suficientes.
   */
  @PatchMapping(value = "/{accountId}/debit", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Debit an account",
      description = "Debits a specific amount from the account's balance.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = "Account debited successfully"),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Account not found"),
      @ApiResponse(responseCode = ConstantUtil.ERROR_CODE,
          description = "Insufficient funds or invalid input data")
  })
  public Mono<BigDecimal> debit(@PathVariable String accountId, @RequestParam BigDecimal amount) {
    if (accountId == null || accountId.isEmpty()) {
      throw new ApiValidateException("Account ID must be provided.");
    }
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ApiValidateException("The amount to debit must be greater than zero.");
    }
    return accountService.debit(accountId, amount);
  }
}