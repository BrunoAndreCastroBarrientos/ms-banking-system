package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.dto.response.ConsolidatedResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.TransactionResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

public interface ReportService {
  Mono<ConsolidatedResponse> getCustomerSummary(String customerId);
  Mono<BigDecimal> getAverageAccountBalance(String customerId);
  Mono<List<TransactionResponse>> getAccountTransactions(String accountId);
  Mono<List<TransactionResponse>> getCardLast10Transactions(String cardId);
}

