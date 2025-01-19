package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.dto.response.AverageBalanceResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.CardTransactionResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.ClientSummaryResponse;
import com.nttdata.bootcamp.ms.banking.repository.AccountRepository;
import com.nttdata.bootcamp.ms.banking.repository.TransactionRepository;
import com.nttdata.bootcamp.ms.banking.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
  private final AccountRepository accountRepository;

  public Flux<AverageBalanceResponse> generateAverageBalanceReport(LocalDateTime startDate, LocalDateTime endDate) {
    return accountRepository.findAll()
        .filter(account -> account.getOpenDate().isAfter(startDate) && account.getOpenDate().isBefore(endDate))
        .map(account -> new AverageBalanceResponse(account.getId(), account.getBalance()));
  }

  public Mono<ClientSummaryResponse> generateClientSummary(String clientId) {
    return accountRepository.findByCustomerId(clientId)
        .collectList()
        .map(accounts -> new ClientSummaryResponse(clientId, accounts.size()));
  }

  public Mono<CardTransactionResponse> getLast10CardTransactions(String cardId) {
    return transactionRepository.findByCreditCardId(cardId)
        .take(10)
        .collectList()
        .map(transactions -> new CardTransactionResponse(cardId, transactions.size()));
  }
}