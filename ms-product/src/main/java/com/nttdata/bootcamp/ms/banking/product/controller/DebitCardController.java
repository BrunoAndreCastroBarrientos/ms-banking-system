package com.nttdata.bootcamp.ms.banking.product.controller;
import com.nttdata.bootcamp.ms.banking.product.dto.request.DebitCardRequest;
import com.nttdata.bootcamp.ms.banking.product.dto.response.DebitCardResponse;
import com.nttdata.bootcamp.ms.banking.product.service.DebitCardService;
import com.nttdata.bootcamp.ms.banking.product.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Controller for managing debit card-related operations.
 * Provides endpoints for creating, retrieving,
 * blocking, and performing transactions with debit cards.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@RestController
@RequestMapping("/api/cards/debit")
@RequiredArgsConstructor
@Tag(name = "Debit Card Controller", description = "Endpoints for managing debit cards")
public class DebitCardController {

  private final DebitCardService debitCardService;

  /**
   * Endpoint to create a new debit card.
   *
   * @param request the debit card request details
   * @return the created debit card details
   */
  @Operation(summary = "Create Debit Card", description = "Creates a new debit card based on the provided details.")
  @ApiResponse(responseCode = ConstantUtil.CREATED_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = ConstantUtil.ERROR_MESSAGE)
  @PostMapping
  public Mono<DebitCardResponse> createDebitCard(@Valid @RequestBody DebitCardRequest request) {
    return debitCardService.createDebitCard(request);
  }

  /**
   * Endpoint to retrieve a debit card by its ID.
   *
   * @param id the debit card ID
   * @return the details of the debit card
   */
  @Operation(summary = "Get Debit Card By ID", description = "Retrieves a debit card by its unique ID.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = ConstantUtil.NOT_FOUND_MESSAGE)
  @GetMapping("/{id}")
  public Mono<DebitCardResponse> getDebitCardById(@PathVariable String id) {
    return debitCardService.getDebitCardById(id);
  }

  /**
   * Endpoint to update a debit card.
   *
   * @param id      the debit card ID
   * @param request the updated debit card details
   * @return the updated debit card details
   */
  @Operation(summary = "Update Debit Card", description = "Updates an existing debit card with the provided details.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = ConstantUtil.NOT_FOUND_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = ConstantUtil.ERROR_MESSAGE)
  @PutMapping("/{id}")
  public Mono<DebitCardResponse> updateDebitCard(@PathVariable String id, @Valid @RequestBody DebitCardRequest request) {
    return debitCardService.updateDebitCard(id, request);
  }

  /**
   * Endpoint to delete a debit card.
   *
   * @param id the debit card ID
   */
  @Operation(summary = "Delete Debit Card", description = "Deletes a debit card by its unique ID.")
  @ApiResponse(responseCode = ConstantUtil.DELETED_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = ConstantUtil.NOT_FOUND_MESSAGE)
  @DeleteMapping("/{id}")
  public Mono<Void> deleteDebitCard(@PathVariable String id) {
    return debitCardService.deleteDebitCard(id);
  }
}
