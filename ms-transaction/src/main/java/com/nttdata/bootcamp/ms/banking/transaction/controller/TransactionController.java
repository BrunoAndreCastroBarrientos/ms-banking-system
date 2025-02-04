package com.nttdata.bootcamp.ms.banking.transaction.controller;

import com.nttdata.bootcamp.ms.banking.transaction.dto.request.TransactionRequest;
import com.nttdata.bootcamp.ms.banking.transaction.dto.response.TransactionResponse;
import com.nttdata.bootcamp.ms.banking.transaction.service.TransactionService;
import com.nttdata.bootcamp.ms.banking.transaction.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@Tag(name = "Transaction Controller", description = "Endpoints for managing transactions")
public class TransactionController {

  private final TransactionService transactionService;

  /**
   * Endpoint to process a transaction.
   *
   * @param request the transaction request details
   * @return the response containing the transaction details
   */
  @Operation(summary = "Process Transaction",
      description = "Processes a transaction based on the provided details.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = ConstantUtil.ERROR_MESSAGE)
  @PostMapping("/process")
  public Mono<TransactionResponse> processTransaction(@Valid @RequestBody TransactionRequest request) {
    return transactionService.processTransaction(request);
  }
}

