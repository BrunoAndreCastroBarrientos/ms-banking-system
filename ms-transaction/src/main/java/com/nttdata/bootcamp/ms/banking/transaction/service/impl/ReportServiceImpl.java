package com.nttdata.bootcamp.ms.banking.transaction.service.impl;

import com.nttdata.bootcamp.ms.banking.transaction.dto.response.AccountResponse;
import com.nttdata.bootcamp.ms.banking.transaction.dto.response.AverageBalanceResponse;
import com.nttdata.bootcamp.ms.banking.transaction.dto.response.CardTransactionResponse;
import com.nttdata.bootcamp.ms.banking.transaction.dto.response.ClientSummaryResponse;
import com.nttdata.bootcamp.ms.banking.transaction.entity.Account;
import com.nttdata.bootcamp.ms.banking.transaction.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.transaction.mapper.AccountMapper;
import com.nttdata.bootcamp.ms.banking.transaction.mapper.CreditCardMapper;
import com.nttdata.bootcamp.ms.banking.transaction.mapper.CreditMapper;
import com.nttdata.bootcamp.ms.banking.transaction.mapper.TransactionMapper;
import com.nttdata.bootcamp.ms.banking.transaction.repository.TransactionRepository;
import com.nttdata.bootcamp.ms.banking.transaction.service.KafkaService;
import com.nttdata.bootcamp.ms.banking.transaction.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

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

  private final TransactionRepository transactionRepository;
  private final WebClient accountWebClient;
  private final AccountMapper accountMapper;

  public Flux<AverageBalanceResponse> generateAverageBalanceReport(LocalDateTime startDate, LocalDateTime endDate) {
    return this.getAllAccounts()
        .filter(account -> account.getOpenDate().isAfter(startDate) && account.getOpenDate().isBefore(endDate))
        .map(account -> new AverageBalanceResponse(account.getId(), account.getBalance()));
  }

  public Mono<ClientSummaryResponse> generateClientSummary(String clientId) {
    return this.getAccountsByCustomerId(clientId)
        .collectList()
        .map(accounts -> new ClientSummaryResponse(clientId, accounts.size()));
  }

  public Mono<CardTransactionResponse> getLast10CardTransactions(String cardId) {
    return transactionRepository.findByCreditCardId(cardId)
        .take(10)
        .collectList()
        .map(transactions -> new CardTransactionResponse(cardId, transactions.size()));
  }

  private Flux<Account> getAccountsByCustomerId(String customerId) {
    return accountWebClient.get()
        .uri("/customers/{customerId}", customerId)
        .retrieve()
        .bodyToFlux(AccountResponse.class)
        .map(accountMapper::responseToEntity)
        .onErrorResume(e -> Flux.error(new ApiValidateException("Error retrieving accounts for customer " + customerId)));
  }

  private Flux<Account> getAllAccounts() {
    return accountWebClient.get()
        .retrieve()
        .bodyToFlux(AccountResponse.class)
        .map(accountMapper::responseToEntity)
        .onErrorResume(e -> Flux.error(new ApiValidateException("Error retrieving all accounts")));
  }


}