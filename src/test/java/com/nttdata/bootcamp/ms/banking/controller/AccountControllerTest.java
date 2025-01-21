package com.nttdata.bootcamp.ms.banking.controller;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountControllerTest extends BaseIntegrationTest {

  @Test
  @Order(1)
  void testCreateAccount() {
    var request = new Object() {
      public String customerId = "678d0fe85042e5003358d58b";
      public String accountType = "SAVINGS"; // (SAVINGS, CHECKING, TIME_DEPOSIT)
      public BigDecimal balance = BigDecimal.valueOf(1000.00);
      public String currency = "USD";
      public LocalDateTime openDate = LocalDateTime.now(); // o futuro/presente
    };

    webTestClient.post()
        .uri("/api/accounts")
        .header("Authorization", authHeader())
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk() // o isCreated(), según tu implementación
        .expectBody()
        .jsonPath("$.id").exists();
  }

  @Test
  @Order(2)
  void testGetAllAccounts() {
    webTestClient.get()
        .uri("/api/accounts")
        .header("Authorization", authHeader())
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$").isArray();
  }

  @Test
  @Order(3)
  void testGetAccountById() {
    String accountId = "1234567890abcdef12345678";

    webTestClient.get()
        .uri("/api/accounts/" + accountId)
        .header("Authorization", authHeader())
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.id").isEqualTo(accountId);
  }

  @Test
  @Order(4)
  void testGetAccountsByCustomerId() {
    String customerId = "678d0fe85042e5003358d58b";

    webTestClient.get()
        .uri("/api/accounts/customer/" + customerId)
        .header("Authorization", authHeader())
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$").isArray();
  }

  @Test
  @Order(5)
  void testUpdateAccount() {
    String accountId = "1234567890abcdef12345678";
    var request = new Object() {
      public String customerId = "0123456789abcdef01234567";
      public String accountType = "CHECKING";
      public BigDecimal balance = BigDecimal.valueOf(5000.00);
      public String currency = "EUR";
      public LocalDateTime openDate = LocalDateTime.now().plusDays(1);
    };

    webTestClient.put()
        .uri("/api/accounts/" + accountId)
        .header("Authorization", authHeader())
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.accountType").isEqualTo("CHECKING");
  }

  @Test
  @Order(6)
  void testDeleteAccount() {
    String accountId = "1234567890abcdef12345678";

    webTestClient.delete()
        .uri("/api/accounts/" + accountId)
        .header("Authorization", authHeader())
        .exchange()
        .expectStatus().isOk(); // o isNoContent()
  }
}
