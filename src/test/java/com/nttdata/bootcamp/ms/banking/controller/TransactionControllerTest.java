package com.nttdata.bootcamp.ms.banking.controller;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionControllerTest extends BaseIntegrationTest {

  @Test
  @Order(1)
  void testProcessTransaction() {
    var request = new Object() {
      public String transactionType = "DEPOSIT"; // DEPOSIT, WITHDRAWAL, TRANSFER, ...
      public String originAccountId = null;
      public String destinationAccountId = "1234567890abcdef12345678";
      public String creditId = null;
      public String creditCardId = null;
      public String debitCardId = null;
      public BigDecimal amount = BigDecimal.valueOf(200.00);
      public LocalDateTime transactionDate = LocalDateTime.now();
    };

    webTestClient.post()
        .uri("/api/transactions/process")
        .header("Authorization", authHeader())
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.transactionId").exists();
  }
}

