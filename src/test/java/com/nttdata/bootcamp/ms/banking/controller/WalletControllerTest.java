package com.nttdata.bootcamp.ms.banking.controller;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

public class WalletControllerTest extends BaseIntegrationTest {

  @Test
  @Order(1)
  void testSendPayment() {
    var request = new Object() {
      public String identificationNumber = "ABC12345";
      public String phoneNumber = "+51987654321";
      public String email = "wallet@example.com";
      public BigDecimal balance = BigDecimal.valueOf(0);
    };

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder
            .path("/api/wallets/send-payment")
            .queryParam("amount", 50)
            .build())
        .header("Authorization", authHeader())
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.phoneNumber").isEqualTo("+51987654321");
  }

  @Test
  @Order(2)
  void testReceivePayment() {
    var request = new Object() {
      public String identificationNumber = "ABC12345";
      public String phoneNumber = "+51987654321";
      public String email = "wallet@example.com";
      public BigDecimal balance = BigDecimal.valueOf(0);
    };

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder
            .path("/api/wallets/receive-payment")
            .queryParam("amount", 25)
            .build())
        .header("Authorization", authHeader())
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.phoneNumber").isEqualTo("+51987654321");
  }

  @Test
  @Order(3)
  void testGetWalletDetails() {
    String phoneNumber = "+51987654321";

    webTestClient.get()
        .uri("/api/wallets/" + phoneNumber)
        .header("Authorization", authHeader())
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.phoneNumber").isEqualTo(phoneNumber);
  }
}
