package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.dto.response.AverageBalanceResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.CardTransactionResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.ClientSummaryResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.TransactionResponse;
import com.nttdata.bootcamp.ms.banking.service.ReportService;
import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for managing transaction-related operations.
 * Provides endpoints for creating, retrieving, and managing transactions.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@Tag(name = "Report Controller", description = "Endpoints for generating transaction-related reports")
public class ReportController {

  private final ReportService reportService;

  /**
   * Endpoint to generate a report of average balances within a date range.
   *
   * @param startDate the start date of the report range
   * @param endDate   the end date of the report range
   * @return a flux containing the average balance data
   */
  @Operation(summary = "Generate Average Balance Report",
      description = "Generates a report of average balances within the specified date range.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = ConstantUtil.ERROR_MESSAGE)
  @GetMapping("/average-balance")
  public Flux<AverageBalanceResponse> generateAverageBalanceReport(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
    return reportService.generateAverageBalanceReport(startDate, endDate);
  }

  /**
   * Endpoint to generate a summary report for a specific client.
   *
   * @param clientId the ID of the client
   * @return a mono containing the client summary report
   */
  @Operation(summary = "Generate Client Summary",
      description = "Generates a summary report for a specific client by their ID.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = ConstantUtil.NOT_FOUND_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = ConstantUtil.ERROR_MESSAGE)
  @GetMapping("/client-summary/{clientId}")
  public Mono<ClientSummaryResponse> generateClientSummary(@PathVariable String clientId) {
    return reportService.generateClientSummary(clientId);
  }

  /**
   * Endpoint to retrieve the last 10 transactions for a specific card.
   *
   * @param cardId the ID of the card
   * @return a mono containing the last 10 transactions for the specified card
   */
  @Operation(summary = "Get Last 10 Card Transactions",
      description = "Retrieves the last 10 transactions for a specific card by its ID.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = ConstantUtil.NOT_FOUND_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = ConstantUtil.ERROR_MESSAGE)
  @GetMapping("/card-transactions/{cardId}/last-10")
  public Mono<CardTransactionResponse> getLast10CardTransactions(@PathVariable String cardId) {
    return reportService.getLast10CardTransactions(cardId);
  }
}

