package com.nttdata.bootcamp.ms.banking.product;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

public class DebitCardControllerTest{

  @Autowired
  protected WebTestClient webTestClient;

  @Test
  @Order(1)
  void testCreateDebitCard() {
    var request = new Object() {
      public String customerId = "0123456789abcdef01234567";
      public String type = "DEBIT"; // forzamos DEBIT
      public String cardType = "PERSONAL"; // o ENTERPRISE
      public String[] associatedAccounts = {
          "1234567890abcdef12345678",
          "0123456789abcdef01234567"
      };
    };

    webTestClient.post()
        .uri("/api/cards/debit")
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk() // o isCreated()
        .expectBody()
        .jsonPath("$.id").exists();
  }

  @Test
  @Order(2)
  void testGetDebitCardById() {
    String cardId = "1234567890abcdef12345678";

    webTestClient.get()
        .uri("/api/cards/debit/" + cardId)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.id").isEqualTo(cardId);
  }

  @Test
  @Order(3)
  void testUpdateDebitCard() {
    String cardId = "1234567890abcdef12345678";
    var request = new Object() {
      public String customerId = "0123456789abcdef01234567";
      public String type = "DEBIT";
      public String cardType = "ENTERPRISE";
      public String[] associatedAccounts = {
          "1234567890abcdef12345678"
      };
    };

    webTestClient.put()
        .uri("/api/cards/debit/" + cardId)
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.cardType").isEqualTo("ENTERPRISE");
  }

  @Test
  @Order(4)
  void testDeleteDebitCard() {
    String cardId = "1234567890abcdef12345678";

    webTestClient.delete()
        .uri("/api/cards/debit/" + cardId)
        .exchange()
        .expectStatus().isOk(); // o isNoContent()
  }
}
