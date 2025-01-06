package com.nttdata.bootcamp.ms.banking.controller;


import com.nttdata.bootcamp.ms.banking.dto.request.CreditCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditCardResponse;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.service.CreditCardService;
import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller for managing credit card related operations.
 *
 * <p>This controller provides endpoints for creating,
 * retrieving, updating, and blocking credit cards.</p>
 *
 * @version 1.1
 * @author Bruno Andre Castro Barrientos
 */
@RestController
@RequestMapping("/api/cards/credit")
@RequiredArgsConstructor
public class CreditCardController {

  private final CreditCardService creditCardService;

  /**
   * Creates a new credit card.
   *
   * <p>This endpoint creates a new credit card by
   * providing the necessary information in the request body.</p>
   *
   * @param request the details of the credit card to create.
   * @return a {@link Mono} with the response containing the newly created credit card details.
   * @throws ApiValidateException if the card creation request is invalid.
   */
  @PostMapping
  @Operation(summary = "Create a new credit card",
      description = "Creates a new credit card for a customer.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.CREATED_CODE,
          description = "Card created successfully"),
      @ApiResponse(responseCode = ConstantUtil.ERROR_CODE,
          description = "Invalid input data")
  })
  public Mono<CreditCardResponse> create(@RequestBody CreditCardRequest request) {
    // Validar request si es necesario
    if (request == null || request.getCustomerId() == null) {
      throw new ApiValidateException("Customer ID is required for card creation.");
    }
    return creditCardService.createCreditCard(request);
  }

  /**
   * Retrieves the details of a specific credit card by its ID.
   *
   * <p>This endpoint allows you to fetch the details of a credit card using its unique ID.</p>
   *
   * @param cardId the unique ID of the credit card to retrieve.
   * @return a {@link Mono} containing the credit card details.
   * @throws ApiValidateException if the card ID does not exist.
   */
  @GetMapping("/{cardId}")
  @Operation(summary = "Get credit card by ID",
      description = "Fetches the details of a credit card by its ID.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = "Card details retrieved successfully"),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = "Card not found")
  })
  public Mono<CreditCardResponse> getById(
      @Parameter(description = "Unique ID of the credit card") @PathVariable String cardId) {
    if (cardId == null || cardId.isEmpty()) {
      throw new ApiValidateException("Card ID must be provided.");
    }
    return creditCardService.getById(cardId);
  }

  /**
   * Retrieves all credit cards associated with a specific customer.
   *
   * <p>This endpoint allows you to retrieve all credit cards linked
   * to a customer using their customer ID.</p>
   *
   * @param customerId the unique ID of the customer whose credit cards are to be fetched.
   * @return a {@link Flux} containing the list of credit cards for the given customer.
   * @throws ApiValidateException if the customer ID does not exist.
   */
  @GetMapping("/customer/{customerId}")
  @Operation(summary = "Get credit cards by customer ID",
      description = "Fetches all credit cards linked to a specific customer.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = "Cards retrieved successfully"),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Customer not found")
  })
  public Flux<CreditCardResponse> getByCustomer(
      @Parameter(description = "Unique ID of the customer")
      @PathVariable String customerId) {
    if (customerId == null || customerId.isEmpty()) {
      throw new ApiValidateException("Customer ID must be provided.");
    }
    return creditCardService.getByCustomerId(customerId);
  }

  /**
   * Blocks a specific credit card.
   *
   * <p>This endpoint allows you to block a credit card by providing its unique ID.</p>
   *
   * @param cardId the unique ID of the credit card to block.
   * @return a {@link Mono} with the response containing the updated credit card status.
   * @throws ApiValidateException if the card does not exist.
   */
  @PatchMapping("/{cardId}/block")
  @Operation(summary = "Block a credit card",
      description = "Blocks a specific credit card by its ID.")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = "Card blocked successfully"),
      @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE,
          description = "Card not found")
  })
  public Mono<CreditCardResponse> blockCard(@PathVariable String cardId) {
    if (cardId == null || cardId.isEmpty()) {
      throw new ApiValidateException("Card ID must be provided.");
    }
    return creditCardService.blockCard(cardId);
  }

  /**
   * Updates the balance of a credit card (either a payment or a consumption).
   *
   * <p>This endpoint allows you to update the balance of a card by either paying (isPayment=true)
   * or consuming (isPayment=false) an amount. The amount should be positive for both actions.</p>
   *
   * @param cardId the unique ID of the credit card whose balance is to be updated.
   * @param amount the amount to be added or deducted from the balance.
   * @param isPayment a flag indicating whether the operation
   *                  is a payment (true) or a consumption (false).
   * @return a {@link Mono} containing the updated credit card details.
   * @throws ApiValidateException if the amount is invalid or negative.
   */
  @PatchMapping("/{cardId}/balance/{amount}/{isPayment}")
  @Operation(
      summary = "Update credit card balance",
      description = "Updates the balance of a credit card (either payment or consumption).")
  @ApiResponses({
      @ApiResponse(responseCode = ConstantUtil.OK_CODE,
          description = "Card balance updated successfully"),
      @ApiResponse(responseCode = ConstantUtil.ERROR_CODE,
          description = "Invalid input data")
  })
  public Mono<CreditCardResponse> updateBalance(
      @Parameter(description = "Unique ID of the credit card")
      @PathVariable String cardId,
      @Parameter(description = "Amount to be added or deducted from the balance")
      @PathVariable double amount,
      @Parameter(description = "Flag to indicate if it's a payment (true) or consumption (false)")
      @PathVariable boolean isPayment) {

    if (amount <= 0) {
      throw new ApiValidateException("Amount must be positive.");
    }

    return creditCardService.updateBalance(cardId, amount, isPayment);
  }
}