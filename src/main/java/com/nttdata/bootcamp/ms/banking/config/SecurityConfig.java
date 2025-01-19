package com.nttdata.bootcamp.ms.banking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Value("${spring.secret.key}")
  private String secretKey;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        .csrf(csrf -> csrf.disable()) // Nueva forma de desactivar CSRF
        .authorizeExchange(exchange -> exchange
            .pathMatchers("/api/auth/*").permitAll()
            .anyExchange().authenticated()
        )
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwtConfigurer -> jwtConfigurer.jwtDecoder(reactiveJwtDecoder()))
        )
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public ReactiveJwtDecoder reactiveJwtDecoder() {
    String secretKeyString = secretKey;
    SecretKey secretKey = new SecretKeySpec(secretKeyString.getBytes(), "HmacSHA256");

    return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
  }
}


