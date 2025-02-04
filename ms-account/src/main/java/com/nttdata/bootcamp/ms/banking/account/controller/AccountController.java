package com.nttdata.bootcamp.ms.banking.account.controller;

import com.nttdata.bootcamp.ms.banking.account.dto.request.AccountRequest;
import com.nttdata.bootcamp.ms.banking.account.dto.response.AccountResponse;
import com.nttdata.bootcamp.ms.banking.account.service.AccountService;
import com.nttdata.bootcamp.ms.banking.account.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
@Tag(name = "Account Controller", description = "Endpoints for managing accounts")
public class AccountController {

  private final AccountService accountService;

  /**
   * Create a new account.
   *
   * @param request the account creation request
   * @return the created account details
   */
  @Operation(summary = "Create Account", description = "Creates a new account based on the provided request.")
  @ApiResponse(responseCode = ConstantUtil.CREATED_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = ConstantUtil.ERROR_MESSAGE)
  @PostMapping
  public Mono<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request) {
    return accountService.createAccount(request);
  }

  /**
   * Retrieve all accounts.
   *
   * @return a list of all accounts
   */
  @Operation(summary = "Get All Accounts", description = "Retrieves a list of all accounts.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @GetMapping
  public Flux<AccountResponse> getAllAccounts() {
    return accountService.getAllAccounts();
  }

  /**
   * Retrieve an account by its ID.
   *
   * @param id the account ID
   * @return the account details
   */
  @Operation(summary = "Get Account By ID", description = "Retrieves an account by its unique ID.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = ConstantUtil.NOT_FOUND_MESSAGE)
  @GetMapping("/{id}")
  public Mono<AccountResponse> getAccountById(@PathVariable String id) {
    return accountService.getAccountById(id);
  }

  /**
   * Retrieve accounts by customer ID.
   *
   * @param customerId the customer ID
   * @return a list of accounts associated with the customer
   */
  @Operation(summary = "Get Accounts By Customer ID", description = "Retrieves all accounts associated with a given customer ID.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @GetMapping("/customer/{customerId}")
  public Flux<AccountResponse> getAccountsByCustomerId(@PathVariable String customerId) {
    return accountService.getAccountsByCustomerId(customerId);
  }

  /**
   * Update an existing account.
   *
   * @param id      the account ID
   * @param request the updated account details
   * @return the updated account
   */
  @Operation(summary = "Update Account", description = "Updates an existing account with the provided details.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = ConstantUtil.NOT_FOUND_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = ConstantUtil.ERROR_MESSAGE)
  @PutMapping("/{id}")
  public Mono<AccountResponse> updateAccount(@PathVariable String id, @Valid @RequestBody AccountRequest request) {
    return accountService.updateAccount(id, request);
  }

  /**
   * Delete an account by its ID.
   *
   * @param id the account ID
   */
  @Operation(summary = "Delete Account", description = "Deletes an account by its unique ID.")
  @ApiResponse(responseCode = ConstantUtil.DELETED_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = ConstantUtil.NOT_FOUND_MESSAGE)
  @DeleteMapping("/{id}")
  public Mono<Void> deleteAccount(@PathVariable String id) {
    return accountService.deleteAccount(id);
  }
}
