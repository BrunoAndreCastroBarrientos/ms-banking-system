package com.nttdata.bootcamp.ms.banking.controller;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CreditCardControllerTest extends BaseIntegrationTest {

  @Test
  @Order(1)
  void testCreateCreditCard() {
    var request = new Object() {
      public String cardNumber = "1234567890123456";
      public String customerId = "0123456789abcdef01234567";
      public String type = "CREDIT"; // forzamos a "CREDIT"
      public String cardType = "PERSONAL"; // PERSONAL/ENTERPRISE
      public BigDecimal creditLimit = BigDecimal.valueOf(5000);
      public BigDecimal balance = BigDecimal.ZERO;
      public LocalDate cutoffDate = LocalDate.now().plusDays(5);
    };

    webTestClient.post()
        .uri("/api/cards/credit")
        .header("Authorization", authHeader())
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk() // o isCreated()
        .expectBody()
        .jsonPath("$.id").exists();
  }

  @Test
  @Order(2)
  void testGetCreditCardById() {
    String cardId = "1234567890abcdef12345678";

    webTestClient.get()
        .uri("/api/cards/credit/" + cardId)
        .header("Authorization", authHeader())
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.id").isEqualTo(cardId);
  }

  @Test
  @Order(3)
  void testUpdateCreditCard() {
    String cardId = "1234567890abcdef12345678";
    var request = new Object() {
      public String cardNumber = "1234567890123456";
      public String customerId = "0123456789abcdef01234567";
      public String type = "CREDIT";
      public String cardType = "ENTERPRISE";
      public BigDecimal creditLimit = BigDecimal.valueOf(10000);
      public BigDecimal balance = BigDecimal.valueOf(2000);
      public LocalDate cutoffDate = LocalDate.now().plusDays(10);
    };

    webTestClient.put()
        .uri("/api/cards/credit/" + cardId)
        .header("Authorization", authHeader())
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.cardType").isEqualTo("ENTERPRISE");
  }

  @Test
  @Order(4)
  void testDeleteCreditCard() {
    String cardId = "1234567890abcdef12345678";

    webTestClient.delete()
        .uri("/api/cards/credit/" + cardId)
        .header("Authorization", authHeader())
        .exchange()
        .expectStatus().isOk(); // o isNoContent()
  }
}

