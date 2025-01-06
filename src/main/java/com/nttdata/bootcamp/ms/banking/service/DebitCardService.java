package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.dto.request.DebitCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.DebitCardResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DebitCardService {
  Mono<DebitCardResponse> createDebitCard(DebitCardRequest request);

  Mono<DebitCardResponse> getById(String cardId);

  Flux<DebitCardResponse> getByCustomerId(String customerId);

  /**
   * Realizar un consumo/retiro:
   * - Se descuenta del saldo de la cuenta principal,
   * - Si no hay suficiente, se intenta con la siguiente en la lista.
   */
  Mono<Void> withdraw(String cardId, double amount);

  /**
   * Bloquear la tarjeta (si el cliente está bloqueado, etc.).
   */
  Mono<DebitCardResponse> blockCard(String cardId);

  /**
   * Consulta de últimos 10 movimientos,
   * ya sea implementado en este microservicio o llamando a otro (Transacciones).
   */
  Flux<String> getLastMovements(String cardId, int limit);

}

