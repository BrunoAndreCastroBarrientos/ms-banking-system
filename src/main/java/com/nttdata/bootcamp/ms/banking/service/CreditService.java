package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.dto.request.CreditRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {
  Mono<CreditResponse> createCredit(CreditRequest request);

  Mono<CreditResponse> getCreditById(String creditId);

  Flux<CreditResponse> getCreditsByCustomerId(String customerId);

  Mono<Void> cancelCredit(String creditId);

  /**
   * Endpoint para que otros microservicios consulten si existe deuda vencida.
   */
  Mono<Boolean> hasOverdueDebt(String customerId);

  /**
   * Método para actualizar saldo pendiente cuando se recibe un pago desde Transacciones
   * (u otro mecanismo).
   */
  Mono<CreditResponse> applyPayment(String creditId, Double paymentAmount);

}

