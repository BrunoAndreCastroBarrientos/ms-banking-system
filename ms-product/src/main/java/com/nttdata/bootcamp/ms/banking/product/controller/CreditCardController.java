package com.nttdata.bootcamp.ms.banking.product.controller;


import com.nttdata.bootcamp.ms.banking.product.dto.request.CreditCardRequest;
import com.nttdata.bootcamp.ms.banking.product.dto.response.CreditCardResponse;
import com.nttdata.bootcamp.ms.banking.product.service.CreditCardService;
import com.nttdata.bootcamp.ms.banking.product.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Controller for managing credit card related operations.
 *
 * <p>This controller provides endpoints for creating,
 * retrieving, updating, and blocking credit cards.</p>
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.1
 */
@RestController
@RequestMapping("/api/cards/credit")
@RequiredArgsConstructor
@Tag(name = "Credit Card Controller", description = "Endpoints for managing credit cards")
public class CreditCardController {

  private final CreditCardService creditCardService;

  /**
   * Endpoint to create a new credit card.
   *
   * @param request the credit card request details
   * @return the created credit card details
   */
  @Operation(summary = "Create Credit Card", description = "Creates a new credit card based on the provided details.")
  @ApiResponse(responseCode = ConstantUtil.CREATED_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = ConstantUtil.ERROR_MESSAGE)
  @PostMapping
  public Mono<CreditCardResponse> createCreditCard(@Valid @RequestBody CreditCardRequest request) {
    return creditCardService.createCreditCard(request);
  }

  /**
   * Endpoint to retrieve a credit card by its ID.
   *
   * @param id the credit card ID
   * @return the details of the credit card
   */
  @Operation(summary = "Get Credit Card By ID", description = "Retrieves a credit card by its unique ID.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = ConstantUtil.NOT_FOUND_MESSAGE)
  @GetMapping("/{id}")
  public Mono<CreditCardResponse> getCreditCardById(@PathVariable String id) {
    return creditCardService.getCreditCardById(id);
  }

  /**
   * Endpoint to update a credit card.
   *
   * @param id      the credit card ID
   * @param request the updated credit card details
   * @return the updated credit card details
   */
  @Operation(summary = "Update Credit Card", description = "Updates an existing credit card with the provided details.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = ConstantUtil.NOT_FOUND_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = ConstantUtil.ERROR_MESSAGE)
  @PutMapping("/{id}")
  public Mono<CreditCardResponse> updateCreditCard(@PathVariable String id, @Valid @RequestBody CreditCardRequest request) {
    return creditCardService.updateCreditCard(id, request);
  }

  /**
   * Endpoint to delete a credit card.
   *
   * @param id the credit card ID
   */
  @Operation(summary = "Delete Credit Card", description = "Deletes a credit card by its unique ID.")
  @ApiResponse(responseCode = ConstantUtil.DELETED_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = ConstantUtil.NOT_FOUND_MESSAGE)
  @DeleteMapping("/{id}")
  public Mono<Void> deleteCreditCard(@PathVariable String id) {
    return creditCardService.deleteCreditCard(id);
  }
}
