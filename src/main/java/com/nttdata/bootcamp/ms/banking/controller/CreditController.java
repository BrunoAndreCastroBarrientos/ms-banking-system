package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.dto.request.CreditRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditResponse;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.service.CreditService;
import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller for managing credit-related operations.
 *
 * <p>This controller provides endpoints for creating,
 * retrieving, updating, and deleting credits.</p>
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.1
 */
@RestController
@RequestMapping("/api/credits")
@RequiredArgsConstructor
@Tag(name = "Credit Controller", description = "Endpoints for managing credits")
public class CreditController {

  private final CreditService creditService;

  /**
   * Endpoint to create a new credit.
   *
   * @param request the credit request details
   * @return the created credit details
   */
  @Operation(summary = "Create Credit", description = "Creates a new credit based on the provided details.")
  @ApiResponse(responseCode = ConstantUtil.CREATED_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = ConstantUtil.ERROR_MESSAGE)
  @PostMapping
  public Mono<CreditResponse> createCredit(@Valid @RequestBody CreditRequest request) {
    return creditService.createCredit(request);
  }

  /**
   * Endpoint to retrieve a credit by its ID.
   *
   * @param id the credit ID
   * @return the details of the credit
   */
  @Operation(summary = "Get Credit By ID", description = "Retrieves a credit by its unique ID.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = ConstantUtil.NOT_FOUND_MESSAGE)
  @GetMapping("/{id}")
  public Mono<CreditResponse> getCreditById(@PathVariable String id) {
    return creditService.getCreditById(id);
  }

  /**
   * Endpoint to update a credit.
   *
   * @param id      the credit ID
   * @param request the updated credit details
   * @return the updated credit details
   */
  @Operation(summary = "Update Credit", description = "Updates an existing credit with the provided details.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = ConstantUtil.NOT_FOUND_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = ConstantUtil.ERROR_MESSAGE)
  @PutMapping("/{id}")
  public Mono<CreditResponse> updateCredit(@PathVariable String id, @Valid @RequestBody CreditRequest request) {
    return creditService.updateCredit(id, request);
  }

  /**
   * Endpoint to delete a credit.
   *
   * @param id the credit ID
   */
  @Operation(summary = "Delete Credit", description = "Deletes a credit by its unique ID.")
  @ApiResponse(responseCode = ConstantUtil.DELETED_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = ConstantUtil.NOT_FOUND_MESSAGE)
  @DeleteMapping("/{id}")
  public Mono<Void> deleteCredit(@PathVariable String id) {
    return creditService.deleteCredit(id);
  }
}

