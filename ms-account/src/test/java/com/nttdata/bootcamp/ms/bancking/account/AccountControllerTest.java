package com.nttdata.bootcamp.ms.bancking.account;

import com.nttdata.bootcamp.ms.banking.account.entity.Account;
import com.nttdata.bootcamp.ms.banking.account.service.AccountService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@WebFluxTest
public class AccountControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private AccountService accountService;

  @Test
  @Order(1)
  void testCreateAccount() {
    var request = new Object() {
      public String customerId = "678d0fe85042e5003358d58b";
      public String accountType = "SAVINGS"; // (SAVINGS, CHECKING, TIME_DEPOSIT)
      public BigDecimal balance = BigDecimal.valueOf(1000.00);
      public String currency = "USD";
      public LocalDateTime openDate = LocalDateTime.now(); // or future/present
    };

    //when(accountService.createAccount(any())).thenReturn(new Account("generatedAccountId", request.customerId, request.accountType, request.balance, request.currency, request.openDate));

    webTestClient.post()
        .uri("/api/accounts")
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk() // o isCreated(), según tu implementación
        .expectBody()
        .jsonPath("$.id").exists();

    verify(accountService).createAccount(any());
  }

  @Test
  @Order(2)
  void testGetAllAccounts() {
    webTestClient.get()
        .uri("http://localhost:8090/api/accounts")
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
        .uri("http://localhost:8090/api/accounts/" + accountId)
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
        .uri("http://localhost:8090/api/accounts/customer/" + customerId)
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
        .uri("http://localhost:8090/api/accounts/" + accountId)
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
        .uri("http://localhost:8090/api/accounts/" + accountId)
        .exchange()
        .expectStatus().isOk(); // o isNoContent()
  }
}
