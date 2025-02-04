package com.nttdata.bootcamp.ms.banking.account.config;

import org.apache.http.HttpHeaders;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Bean
  @LoadBalanced
  public WebClient customerWebClient(WebClient.Builder builder) {
    return builder.baseUrl("http://localhost:8090/api/customers") // URL base del microservicio de clientes
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }

  @Bean
  @LoadBalanced
  public WebClient creditWebClient(WebClient.Builder builder) {
    return builder.baseUrl("http://localhost:8090/api/credits") // URL base del microservicio de créditos
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }

  @Bean
  @LoadBalanced
  public WebClient creditCardWebClient(WebClient.Builder builder) {
    return builder.baseUrl("http://localhost:8090/api/cards/credit") // URL base del microservicio de tarjetas de crédito
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }
}

