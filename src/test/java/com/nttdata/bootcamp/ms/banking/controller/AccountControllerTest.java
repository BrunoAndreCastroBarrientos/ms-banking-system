package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.dto.request.AccountRequest;
import com.nttdata.bootcamp.ms.banking.dto.request.UserRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.AccountResponse;
import com.nttdata.bootcamp.ms.banking.service.AccountService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  private String token;

  @BeforeAll
  void doLoginAndGetToken() {
    // Aquí 'webTestClient' YA NO es null, porque el test es de instancia.
    UserRequest loginRequest = new UserRequest("bcastrob", "Bcastrob123@$");

    String responseToken = webTestClient.post()
        .uri("/api/auth/login")
        .bodyValue(loginRequest)
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .returnResult()
        .getResponseBody();

    this.token = responseToken;
    System.out.println("Token obtenido: " + token);
  }

  @Test
  void testGetAllAccounts() {
    webTestClient.get()
        .uri("/api/accounts")
        .header("Authorization", "Bearer " + token)
        .exchange()
        .expectStatus().isOk();
  }
}

