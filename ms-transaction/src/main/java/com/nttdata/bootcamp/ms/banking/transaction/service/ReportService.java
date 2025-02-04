package com.nttdata.bootcamp.ms.banking.transaction.service;

import com.nttdata.bootcamp.ms.banking.transaction.dto.response.AverageBalanceResponse;
import com.nttdata.bootcamp.ms.banking.transaction.dto.response.CardTransactionResponse;
import com.nttdata.bootcamp.ms.banking.transaction.dto.response.ClientSummaryResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface ReportService {
  Flux<AverageBalanceResponse> generateAverageBalanceReport(LocalDateTime startDate, LocalDateTime endDate);
  Mono<ClientSummaryResponse> generateClientSummary(String clientId);
  Mono<CardTransactionResponse> getLast10CardTransactions(String cardId);
}

