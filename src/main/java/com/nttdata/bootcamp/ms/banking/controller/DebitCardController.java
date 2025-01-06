package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.dto.request.DebitCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.DebitCardResponse;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.service.DebitCardService;
import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller for managing debit card-related operations.
 * Provides endpoints for creating, retrieving,
 * blocking, and performing transactions with debit cards.
 *
 * @version 1.0
 * @author Bruno Andre Castro Barrientos
 */
@RestController
@RequestMapping("/api/cards/debit")
@RequiredArgsConstructor
public class DebitCardController {

  private final DebitCardService debitCardService;

  /**
   * Creates a new debit card for a customer.
   *
   * @param request The request containing the details for the new debit card.
   * @return A {@link Mono} containing the response with the created debit card details.
   * @throws ApiValidateException if the input data is invalid or incomplete.
   */
  @PostMapping
  @Operation(summary = "Create a new debit card",
      description = "Creates a new debit card for the customer with the provided details.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.CREATED_CODE,
          description = "Debit card created successfully"),
      @ApiResponse(responseCode = ConstantUtil.ERROR_CODE,
          description = ConstantUtil.ERROR_MESSAGE)
  })
  public Mono<DebitCardResponse> create(@RequestBody DebitCardRequest request) {
    return debitCardService.createDebitCard(request);
  }

  /**
   * Retrieves a debit card by its unique ID.
   *
   * @param cardId The unique identifier of the debit card.
   * @return A {@link Mono} containing the details of the debit card.
   * @throws ApiValidateException if the card ID is invalid or not found.
   */
  @GetMapping("/{cardId}")
  @Operation(summary = "Get debit card by ID",
      description = "Fetches the details of a debit card by its ID.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = ConstantUtil.OK_MESSAGE),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = "Debit card not found")
  })
  public Mono<DebitCardResponse> getById(@PathVariable String cardId) {
    return debitCardService.getById(cardId);
  }

  /**
   * Retrieves all debit cards associated with a specific customer.
   *
   * @param customerId The unique identifier of the customer whose cards are being retrieved.
   * @return A {@link Flux} containing the details of all debit cards associated with the customer.
   * @throws ApiValidateException if the customer ID is invalid or not found.
   */
  @GetMapping("/customer/{customerId}")
  @Operation(summary = "Get debit cards by customer",
      description = "Fetches all debit cards associated with a customer.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = "Customer not found")
  })
  public Flux<DebitCardResponse> getByCustomer(@PathVariable String customerId) {
    return debitCardService.getByCustomerId(customerId);
  }

  /**
   * Blocks a debit card by its unique ID.
   *
   * @param cardId The unique identifier of the debit card to block.
   * @return A {@link Mono} containing the updated debit card details with the block status.
   * @throws ApiValidateException if the card ID is invalid or the card could not be blocked.
   */
  @PatchMapping("/{cardId}/block")
  @Operation(summary = "Block a debit card",
      description = "Blocks a debit card to prevent further transactions.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = "Debit card blocked successfully"),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Debit card not found"),
      @ApiResponse(responseCode = ConstantUtil.ERROR_CODE,
          description = "Error blocking the debit card")
  })
  public Mono<DebitCardResponse> blockCard(@PathVariable String cardId) {
    return debitCardService.blockCard(cardId);
  }

  /**
   * Withdraws a specified amount from the debit card.
   *
   * @param cardId The unique identifier of the debit card to withdraw from.
   * @param amount The amount to withdraw from the card.
   * @return A {@link Mono} containing the result of the withdrawal operation.
   * @throws ApiValidateException if the card ID or withdrawal amount is invalid.
   */
  @PostMapping("/{cardId}/withdraw/{amount}")
  @Operation(summary = "Withdraw from debit card",
      description = "Withdraws a specified amount from the debit card.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = "Withdrawal successful"),
      @ApiResponse(responseCode = ConstantUtil.ERROR_CODE,
          description = "Invalid withdrawal amount or card ID")
  })
  public Mono<Void> withdraw(@PathVariable String cardId, @PathVariable double amount) {
    return debitCardService.withdraw(cardId, amount);
  }

  /**
   * Retrieves the last N movements from a debit card.
   *
   * @param cardId The unique identifier of the debit card.
   * @param limit The maximum number of movements to retrieve (default is 10).
   * @return A {@link Flux} containing the details of the last N movements.
   * @throws ApiValidateException if the card ID is invalid or not found.
   */
  @GetMapping("/{cardId}/movements")
  @Operation(summary = "Get last movements of a debit card",
      description = "Fetches the last N movements of a debit card.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = ConstantUtil.OK_MESSAGE),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Debit card not found")
  })
  public Flux<String> getLastMovements(@PathVariable String cardId,
                                       @RequestParam(defaultValue = "10") int limit) {
    return debitCardService.getLastMovements(cardId, limit);
  }
}
