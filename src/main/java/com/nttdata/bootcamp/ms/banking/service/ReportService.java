package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.dto.response.AverageBalanceResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.CardTransactionResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.ClientSummaryResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.TransactionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {
  Flux<AverageBalanceResponse> generateAverageBalanceReport(LocalDateTime startDate, LocalDateTime endDate);
  Mono<ClientSummaryResponse> generateClientSummary(String clientId);
  Mono<CardTransactionResponse> getLast10CardTransactions(String cardId);
}

