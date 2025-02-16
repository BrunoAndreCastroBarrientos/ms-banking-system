package com.nttdata.bootcamp.ms.banking.product;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreditControllerTest{

  @Autowired
  protected WebTestClient webTestClient;

  @Test
  @Order(1)
  void testCreateCredit() {
    var request = new Object() {
      public String customerId = "0123456789abcdef01234567";
      public String creditType = "PERSONAL"; // or ENTERPRISE
      public BigDecimal amount = BigDecimal.valueOf(10000);
      public BigDecimal interestRate = BigDecimal.valueOf(5.5);
      public LocalDate dueDate = LocalDate.now().plusMonths(1);
    };

    webTestClient.post()
        .uri("/api/credits")
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk() // o isCreated()
        .expectBody()
        .jsonPath("$.id").exists();
  }

  @Test
  @Order(2)
  void testGetCreditById() {
    String creditId = "1234567890abcdef12345678";

    webTestClient.get()
        .uri("/api/credits/" + creditId)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.id").isEqualTo(creditId);
  }

  @Test
  @Order(3)
  void testUpdateCredit() {
    String creditId = "1234567890abcdef12345678";
    var request = new Object() {
      public String customerId = "0123456789abcdef01234567";
      public String creditType = "ENTERPRISE";
      public BigDecimal amount = BigDecimal.valueOf(20000);
      public BigDecimal interestRate = BigDecimal.valueOf(3.9);
      public LocalDate dueDate = LocalDate.now().plusMonths(2);
    };

    webTestClient.put()
        .uri("/api/credits/" + creditId)
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.creditType").isEqualTo("ENTERPRISE");
  }

  @Test
  @Order(4)
  void testDeleteCredit() {
    String creditId = "1234567890abcdef12345678";

    webTestClient.delete()
        .uri("/api/credits/" + creditId)
        .exchange()
        .expectStatus().isOk(); // o isNoContent()
  }
}


