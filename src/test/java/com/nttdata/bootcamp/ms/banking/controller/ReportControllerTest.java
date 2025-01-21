package com.nttdata.bootcamp.ms.banking.controller;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class ReportControllerTest extends BaseIntegrationTest {

  @Test
  @Order(1)
  void testGenerateAverageBalanceReport() {
    webTestClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/api/report/average-balance")
            .queryParam("startDate", "2025-01-01T00:00:00")
            .queryParam("endDate", "2025-12-31T23:59:59")
            .build())
        .header("Authorization", authHeader())
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$").isArray();
  }

  @Test
  @Order(2)
  void testGenerateClientSummary() {
    String clientId = "0123456789abcdef01234567";

    webTestClient.get()
        .uri("/api/report/client-summary/" + clientId)
        .header("Authorization", authHeader())
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.clientId").isEqualTo(clientId);
  }

  @Test
  @Order(3)
  void testGetLast10CardTransactions() {
    String cardId = "1234567890abcdef12345678";

    webTestClient.get()
        .uri("/api/report/card-transactions/" + cardId + "/last-10")
        .header("Authorization", authHeader())
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.cardId").isEqualTo(cardId);
  }
}
