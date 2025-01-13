package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.dto.response.AccountResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.ConsolidatedResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.TransactionResponse;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.JsonNode;
import com.nttdata.bootcamp.ms.banking.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementación del servicio de reportes.
 * Proporciona operaciones para obtener resúmenes consolidados,
 * balances promedio diarios y los últimos movimientos de productos del cliente.
 *
 * <p>Este servicio gestiona la obtención de
 * resúmenes financieros del cliente, incluyendo el
 * balance de cuentas, créditos, tarjetas y
 * verifica si existe deuda vencida. Además,
 * ofrece la capacidad de calcular el balance
 * promedio diario de una cuenta en un período
 * y obtener los últimos movimientos de los
 * productos del cliente.</p>
 *
 * @version 1.1
 * @author Bruno Andre Castro Barrientos
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

  private final WebClient webClient;

  @Value("${service.account.url}")
  private String accountsServiceUrl;

  @Value("${service.credit.url}")
  private String creditsServiceUrl;

  @Value("${service.transaction.url}")
  private String transactionsServiceUrl;

  @Override
  public Mono<ConsolidatedResponse> getCustomerSummary(String customerId) {
    Mono<List<AccountResponse>> accounts = webClient.get()
        .uri(accountsServiceUrl + "/customer/{id}", customerId)
        .retrieve()
        .bodyToFlux(AccountResponse.class)
        .collectList();

    Mono<List<CreditResponse>> credits = webClient.get()
        .uri(creditsServiceUrl + "/customer/{id}", customerId)
        .retrieve()
        .bodyToFlux(CreditResponse.class)
        .collectList();

    Mono<List<TransactionResponse>> transactions = webClient.get()
        .uri(transactionsServiceUrl + "/customer/{id}", customerId)
        .retrieve()
        .bodyToFlux(TransactionResponse.class)
        .collectList();

    return Mono.zip(accounts, credits, transactions)
        .map(tuple -> new ConsolidatedResponse(customerId, tuple.getT1(), tuple.getT2(), tuple.getT3()));
  }

  @Override
  public Mono<BigDecimal> getAverageAccountBalance(String customerId) {
    return webClient.get()
        .uri(accountsServiceUrl + "/customer/{id}/average-balance", customerId)
        .retrieve()
        .bodyToMono(BigDecimal.class);
  }

  @Override
  public Mono<List<TransactionResponse>> getAccountTransactions(String accountId) {
    return webClient.get()
        .uri(transactionsServiceUrl + "/account/{id}", accountId)
        .retrieve()
        .bodyToFlux(TransactionResponse.class)
        .collectList();
  }

  @Override
  public Mono<List<TransactionResponse>> getCardLast10Transactions(String cardId) {
    return webClient.get()
        .uri(transactionsServiceUrl + "/card/{id}/last10", cardId)
        .retrieve()
        .bodyToFlux(TransactionResponse.class)
        .collectList();
  }
}