package com.nttdata.bootcamp.ms.banking.controller;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

public class CustomerControllerTest extends BaseIntegrationTest {

  @Test
  @Order(1)
  void testCreateCustomer() {
    var request = new Object() {
      public String customerType = "PERSONAL";  // or ENTERPRISE
      public String subType = "STANDARD";       // or VIP, PYME
      public String firstName = "Juan";
      public String lastName = "Pérez";
      public String businessName = null; // no aplica a PERSONAL
      public String identificationNumber = "ABC12345";
      public String identificationType = "DNI"; // DNI, CEX, PASSPORT
      public String email = "juan.perez@example.com";
      public String phoneNumber = "+34987654321";
      public LocalDateTime creationDate = LocalDateTime.now();
    };

    webTestClient.post()
        .uri("/api/customers")
        .header("Authorization", authHeader())
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk() // o isCreated()
        .expectBody()
        .jsonPath("$.id").exists();
  }

  @Test
  @Order(2)
  void testGetAllCustomers() {
    webTestClient.get()
        .uri("/api/customers")
        .header("Authorization", authHeader())
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$").isArray();
  }

  @Test
  @Order(3)
  void testGetCustomerById() {
    String customerId = "1234567890abcdef12345678";

    webTestClient.get()
        .uri("/api/customers/" + customerId)
        .header("Authorization", authHeader())
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.id").isEqualTo(customerId);
  }

  @Test
  @Order(4)
  void testUpdateCustomer() {
    String customerId = "1234567890abcdef12345678";
    var request = new Object() {
      public String customerType = "ENTERPRISE";
      public String subType = "PYME";
      public String firstName = null;  // no aplica a ENTERPRISE
      public String lastName = null;
      public String businessName = "MiEmpresaSRL";
      public String identificationNumber = "EMP00123";
      public String identificationType = "CEX";
      public String email = "info@miempresa.com";
      public String phoneNumber = "+51999999999";
      public LocalDateTime creationDate = LocalDateTime.now().plusDays(2);
    };

    webTestClient.put()
        .uri("/api/customers/" + customerId)
        .header("Authorization", authHeader())
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.businessName").isEqualTo("MiEmpresaSRL");
  }

  @Test
  @Order(5)
  void testDeleteCustomer() {
    String customerId = "1234567890abcdef12345678";

    webTestClient.delete()
        .uri("/api/customers/" + customerId)
        .header("Authorization", authHeader())
        .exchange()
        .expectStatus().isOk(); // o isNoContent()
  }
}
