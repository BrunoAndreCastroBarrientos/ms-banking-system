package com.nttdata.bootcamp.ms.banking.service.impl;

import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.JsonNode;
import com.nttdata.bootcamp.ms.banking.dto.AccountInfo;
import com.nttdata.bootcamp.ms.banking.dto.CardInfo;
import com.nttdata.bootcamp.ms.banking.dto.CreditInfo;
import com.nttdata.bootcamp.ms.banking.dto.response.ConsolidatedSummaryResponse;
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

  // URLs de otros microservicios
  @Value("${service.accounts.url}")
  private String accountsServiceUrl;
  @Value("${service.credits.url}")
  private String creditsServiceUrl;
  @Value("${service.cards.url}")
  private String cardsServiceUrl;
  @Value("${service.transactions.url}")
  private String transactionsServiceUrl;

  private final WebClient webClient = WebClient.create();

  @Override
  public Mono<ConsolidatedSummaryResponse> getConsolidatedSummary(String customerId) {
    ConsolidatedSummaryResponse summary = new ConsolidatedSummaryResponse();
    summary.setCustomerId(customerId);

    // 1. Obtener Cuentas
    Mono<List<AccountInfo>> accountsMono = getAccounts(customerId)
        .collectList()
        .doOnNext(summary::setAccounts);

    // 2. Obtener Créditos
    Mono<List<CreditInfo>> creditsMono = getCredits(customerId)
        .collectList()
        .doOnNext(summary::setCredits);

    // 3. Obtener Tarjetas
    Mono<List<CardInfo>> cardsMono = getCards(customerId)
        .collectList()
        .doOnNext(summary::setCards);

    // 4. Verificar deuda vencida (endpoint del microservicio de Créditos)
    Mono<Boolean> overdueMono = hasOverdueDebt(customerId)
        .doOnNext(summary::setHasOverdueDebt);

    return Mono.when(accountsMono, creditsMono, cardsMono, overdueMono)
        .then(Mono.defer(() -> {
          // Calcular los totales
          BigDecimal totalAccountsBalance = summary.getAccounts().stream()
              .map(AccountInfo::getBalance)
              .reduce(BigDecimal.ZERO, BigDecimal::add);

          BigDecimal totalCreditDebt = summary.getCredits().stream()
              .map(CreditInfo::getOutstandingBalance)
              .reduce(BigDecimal.ZERO, BigDecimal::add);

          BigDecimal totalCardBalance = summary.getCards().stream()
              // para tarjetas de crédito, 'balanceOrConsumed' indica cuánto se ha consumido
              .map(CardInfo::getBalanceOrConsumed)
              .filter(b -> b != null)
              .reduce(BigDecimal.ZERO, BigDecimal::add);

          summary.setTotalAccountsBalance(totalAccountsBalance);
          summary.setTotalCreditDebt(totalCreditDebt);
          summary.setTotalCardBalance(totalCardBalance);

          return Mono.just(summary);
        }));
  }

  @Override
  public Mono<Double> getAverageDailyBalance(String customerId, String accountId, String fromDate, String toDate) {
    // Ejemplo muy simple: se podría llamar al microservicio de Transacciones para
    // obtener todas las transacciones de ese periodo, reconstruir el saldo diario
    // y calcular el promedio.
    // Aquí sólo se muestra la estructura básica:
    return webClient.get()
        .uri(transactionsServiceUrl + "/api/transactions/account/{accountId}", accountId)
        .retrieve()
        .bodyToFlux(JsonNode.class)
        .collectList()
        .map(movements -> {
          // Reconstruir saldos diarios a partir de los movimientos
          // Calcular el promedio ...
          return 0.0; // Lógica omitida
        });
  }

  @Override
  public Mono<String> getLastTenMovements(String customerId, String productId, String productType) {
    // Dependiendo de productType = "ACCOUNT", "CREDIT_CARD", "DEBIT_CARD"
    // se podría llamar a transacciones con un endpoint distinto
    // y filtrar los últimos 10
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .scheme("http")
            .host("TRANSACTIONS-SERVICE") // o transactionsServiceUrl
            .path("/api/transactions")
            .queryParam("limit", 10)
            .queryParam("productType", productType)
            .queryParam("productId", productId)
            .build())
        .retrieve()
        .bodyToFlux(JsonNode.class)
        .take(10)
        .collectList()
        .map(list -> list.toString()); // Retorna un JSON con los últimos 10 movimientos
  }

  // ---------------- Métodos internos para consumir otros microservicios ---------------- //

  /**
   * Obtiene la lista de cuentas de un cliente.
   */
  private Flux<AccountInfo> getAccounts(String customerId) {
    // Microservicio de Cuentas: GET /api/accounts/customer/{customerId}
    return webClient.get()
        .uri(accountsServiceUrl + "/api/accounts/customer/{customerId}", customerId)
        .retrieve()
        .bodyToFlux(JsonNode.class)
        .map(json -> {
          AccountInfo info = new AccountInfo();
          info.setAccountId(json.path("id").asText());
          info.setAccountType(json.path("accountType").asText());
          info.setBalance(new BigDecimal(json.path("balance").asText("0")));
          return info;
        });
  }

  /**
   * Obtiene la lista de créditos de un cliente.
   */
  private Flux<CreditInfo> getCredits(String customerId) {
    // Microservicio de Créditos: GET /api/credits/customer/{customerId}
    return webClient.get()
        .uri(creditsServiceUrl + "/api/credits/customer/{customerId}", customerId)
        .retrieve()
        .bodyToFlux(JsonNode.class)
        .map(json -> {
          CreditInfo info = new CreditInfo();
          info.setCreditId(json.path("id").asText());
          info.setOutstandingBalance(new BigDecimal(json.path("outstandingBalance").asText("0")));
          info.setStatus(json.path("status").asText());
          return info;
        });
  }

  /**
   * Obtiene la lista de tarjetas (crédito y/o débito) de un cliente.
   */
  private Flux<CardInfo> getCards(String customerId) {
    // Se podrían hacer dos llamadas: /credit y /debit, o una si el microservicio
    // unifica todo. Aquí se muestra un ejemplo de "tarjetas de crédito" solamente:
    Flux<CardInfo> creditCards = webClient.get()
        .uri(cardsServiceUrl + "/api/cards/credit/customer/{customerId}", customerId)
        .retrieve()
        .bodyToFlux(JsonNode.class)
        .map(json -> {
          CardInfo info = new CardInfo();
          info.setCardId(json.path("id").asText());
          info.setCardType("CREDIT");
          info.setBalanceOrConsumed(new BigDecimal(json.path("balance").asText("0")));
          info.setAvailableLimit(new BigDecimal(json.path("availableLimit").asText("0")));
          return info;
        });

    // Tarjetas de débito:
    Flux<CardInfo> debitCards = webClient.get()
        .uri(cardsServiceUrl + "/api/cards/debit/customer/{customerId}", customerId)
        .retrieve()
        .bodyToFlux(JsonNode.class)
        .map(json -> {
          CardInfo info = new CardInfo();
          info.setCardId(json.path("id").asText());
          info.setCardType("DEBIT");
          // Podrías guardar "balanceOrConsumed" = 0,
          // ya que la lógica del saldo vive en las cuentas asociadas
          info.setBalanceOrConsumed(BigDecimal.ZERO);
          info.setAvailableLimit(null);
          return info;
        });

    return Flux.merge(creditCards, debitCards);
  }

  /**
   * Verifica si el cliente tiene deuda vencida (hay un endpoint en microservicio de Créditos).
   */
  private Mono<Boolean> hasOverdueDebt(String customerId) {
    return webClient.get()
        .uri(creditsServiceUrl + "/api/credits/debts/pending/{customerId}", customerId)
        .retrieve()
        .bodyToMono(Boolean.class)
        .defaultIfEmpty(false);
  }
}