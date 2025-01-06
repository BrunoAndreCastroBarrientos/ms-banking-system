package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.dto.request.TransactionRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.TransactionResponse;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.service.TransactionService;
import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller for managing transaction-related operations.
 * Provides endpoints for creating, retrieving, and managing transactions.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionService transactionService;

  /**
   * Creates a new transaction based on the provided request.
   *
   * @param request The transaction request containing transaction details.
   * @return A {@link Mono} containing the response with the processed transaction details.
   * @throws ApiValidateException if the transaction request data is invalid.
   */
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Create a new transaction",
      description = "Processes a new transaction based on the provided details.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.CREATED_CODE,
          description = "Transaction created successfully"),
      @ApiResponse(responseCode = ConstantUtil.ERROR_CODE,
          description = ConstantUtil.ERROR_MESSAGE)
  })
  public Mono<TransactionResponse> createTransaction(@RequestBody TransactionRequest request) {
    return transactionService.processTransaction(request);
  }

  /**
   * Retrieves a transaction by its unique ID.
   *
   * @param id The unique identifier of the transaction.
   * @return A {@link Mono} containing the details of the transaction.
   * @throws ApiValidateException if the transaction ID is invalid or not found.
   */
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get transaction by ID",
      description = "Fetches the details of a transaction by its ID.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = ConstantUtil.OK_MESSAGE),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Transaction not found")
  })
  public Mono<TransactionResponse> getById(@PathVariable String id) {
    return transactionService.getById(id);
  }

  /**
   * Retrieves all transactions in the system.
   *
   * @return A {@link Flux} containing a list of all transaction details.
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get all transactions",
      description = "Fetches all transactions in the system.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = ConstantUtil.OK_MESSAGE)
  })
  public Flux<TransactionResponse> getAll() {
    return transactionService.getAll();
  }

  /**
   * Retrieves all transactions associated with a specific account.
   *
   * @param accountId The unique identifier of the account whose transactions are being retrieved.
   * @return A {@link Flux} containing the details of all transactions associated with the account.
   * @throws ApiValidateException if the account ID is invalid or not found.
   */
  @GetMapping(value = "/account/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get transactions by account",
      description = "Fetches all transactions associated with the specified account.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = ConstantUtil.OK_MESSAGE),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Account not found")
  })
  public Flux<TransactionResponse> getByAccount(@PathVariable String accountId) {
    return transactionService.getByAccountId(accountId);
  }

  /**
   * Retrieves all transactions associated with a specific credit.
   *
   * @param creditId The unique identifier of the credit whose transactions are being retrieved.
   * @return A {@link Flux} containing the details of all transactions associated with the credit.
   * @throws ApiValidateException if the credit ID is invalid or not found.
   */
  @GetMapping(value = "/credit/{creditId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get transactions by credit",
      description = "Fetches all transactions associated with the specified credit.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = ConstantUtil.OK_MESSAGE),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Credit not found")
  })
  public Flux<TransactionResponse> getByCredit(@PathVariable String creditId) {
    return transactionService.getByCreditId(creditId);
  }

  /**
   * Retrieves all transactions associated with a specific credit card.
   *
   * @param creditCardId The unique identifier of the credit card whose
   *                     transactions are being retrieved.
   * @return A {@link Flux} containing the details of
   *                      all transactions associated with the credit card.
   * @throws ApiValidateException if the credit card ID is invalid or not found.
   */
  @GetMapping(value = "/creditCard/{creditCardId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get transactions by credit card",
      description = "Fetches all transactions associated with the specified credit card.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = ConstantUtil.OK_MESSAGE),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Credit card not found")
  })
  public Flux<TransactionResponse> getByCreditCard(@PathVariable String creditCardId) {
    return transactionService.getByCreditCardId(creditCardId);
  }

  /**
   * Retrieves all transactions associated with a specific debit card.
   *
   * @param debitCardId The unique identifier of the debit
   *                    card whose transactions are being retrieved.
   * @return A {@link Flux} containing the details of all
   *                    transactions associated with the debit card.
   * @throws ApiValidateException if the debit card ID is invalid or not found.
   */
  @GetMapping(value = "/debitCard/{debitCardId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get transactions by debit card",
      description = "Fetches all transactions associated with the specified debit card.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = ConstantUtil.OK_MESSAGE),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Debit card not found")
  })
  public Flux<TransactionResponse> getByDebitCard(@PathVariable String debitCardId) {
    return transactionService.getByDebitCardId(debitCardId);
  }
}
