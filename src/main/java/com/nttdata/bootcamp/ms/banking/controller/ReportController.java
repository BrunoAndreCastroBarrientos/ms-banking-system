package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.dto.request.TransactionRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.ConsolidatedResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.TransactionResponse;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.service.ReportService;
import com.nttdata.bootcamp.ms.banking.service.TransactionService;
import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
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


  @GetMapping("/customer/{id}/summary")
  public Mono<ConsolidatedResponse> getCustomerSummary(@PathVariable String id) {
    return reportService.getCustomerSummary(id);
  }

  @GetMapping("/customer/{id}/average-balance")
  public Mono<BigDecimal> getAverageAccountBalance(@PathVariable String id) {
    return reportService.getAverageAccountBalance(id);
  }

  @GetMapping("/accounts/{accountId}/transactions")
  public Mono<List<TransactionResponse>> getAccountTransactions(@PathVariable String accountId) {
    return reportService.getAccountTransactions(accountId);
  }

  @GetMapping("/cards/{cardId}/last10")
  public Mono<List<TransactionResponse>> getCardLast10Transactions(@PathVariable String cardId) {
    return reportService.getCardLast10Transactions(cardId);
  }
}
