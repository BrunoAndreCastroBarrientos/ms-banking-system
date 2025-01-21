package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIntegrationTest {

  @Autowired
  protected WebTestClient webTestClient;

  protected String token;

  @BeforeAll
  void authenticateAndGetToken() {
    // Ajustar credenciales al endpoint real
    var loginRequest = new Object() {
      public String username = "bcastrob";
      public String password = "Bcastrob123@$";
    };

    this.token = webTestClient.post()
        .uri("/api/auth/login")
        .bodyValue(loginRequest)
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .returnResult()
        .getResponseBody();

    if (token == null || token.isBlank()) {
      throw new ApiValidateException("No se pudo obtener el token de autenticación.");
    }

  }

  protected String authHeader() {
    return "Bearer " + token;
  }
}
