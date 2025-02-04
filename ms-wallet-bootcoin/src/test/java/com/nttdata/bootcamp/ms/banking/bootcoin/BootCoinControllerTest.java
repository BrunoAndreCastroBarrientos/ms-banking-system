package com.nttdata.bootcamp.ms.banking.bootcoin;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

public class BootCoinControllerTest{

  @Autowired
  protected WebTestClient webTestClient;

  @Test
  @Order(1)
  void testBuyBootCoin() {
    var request = new Object() {
      public String phoneNumber = "+51987654321";
      public BigDecimal amount = BigDecimal.valueOf(100);
    };

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder
            .path("/api/bootcoin/buy")
            .queryParam("amount", 100)
            .build())
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.transactionId").exists();
  }

  @Test
  @Order(2)
  void testSellBootCoin() {
    var request = new Object() {
      public String phoneNumber = "+51987654321";
      public BigDecimal amount = BigDecimal.valueOf(50);
    };

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder
            .path("/api/bootcoin/sell")
            .queryParam("amount", 50)
            .build())
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.transactionId").exists();
  }
}

