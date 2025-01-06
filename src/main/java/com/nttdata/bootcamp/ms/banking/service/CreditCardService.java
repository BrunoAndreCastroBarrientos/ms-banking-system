package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.dto.request.CreditCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditCardResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditCardService {
  Mono<CreditCardResponse> createCreditCard(CreditCardRequest request);

  Mono<CreditCardResponse> getById(String cardId);

  Flux<CreditCardResponse> getByCustomerId(String customerId);

  /**
   * Actualizar los límites al hacer un consumo o pago:
   * - Reducir availableLimit al consumir,
   * - Aumentar availableLimit y disminuir balance al pagar, etc.
   */
  Mono<CreditCardResponse> updateBalance(String cardId, double amount, boolean isPayment);

  /**
   * Bloquear la tarjeta si el cliente está inactivo o la tarjeta en mal uso.
   */
  Mono<CreditCardResponse> blockCard(String cardId);

}

