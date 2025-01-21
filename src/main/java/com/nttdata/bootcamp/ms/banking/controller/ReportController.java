package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.dto.response.AverageBalanceResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.CardTransactionResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.ClientSummaryResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.TransactionResponse;
import com.nttdata.bootcamp.ms.banking.service.ReportService;
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
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;

  @GetMapping("/average-balance")
  public Flux<AverageBalanceResponse> generateAverageBalanceReport(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
    return reportService.generateAverageBalanceReport(startDate, endDate);
  }

  @GetMapping("/client-summary/{clientId}")
  public Mono<ClientSummaryResponse> generateClientSummary(@PathVariable String clientId) {
    return reportService.generateClientSummary(clientId);
  }

  @GetMapping("/card-transactions/{cardId}/last-10")
  public Mono<CardTransactionResponse> getLast10CardTransactions(@PathVariable String cardId) {
    return reportService.getLast10CardTransactions(cardId);
  }
}
