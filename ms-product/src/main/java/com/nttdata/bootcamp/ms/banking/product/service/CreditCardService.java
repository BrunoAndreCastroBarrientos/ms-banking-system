package com.nttdata.bootcamp.ms.banking.product.service;

import com.nttdata.bootcamp.ms.banking.product.dto.request.CreditCardRequest;
import com.nttdata.bootcamp.ms.banking.product.dto.response.CreditCardResponse;
import reactor.core.publisher.Mono;

public interface CreditCardService {
  Mono<CreditCardResponse> createCreditCard(CreditCardRequest request);
  Mono<CreditCardResponse> getCreditCardById(String id);
  Mono<CreditCardResponse> updateCreditCard(String id, CreditCardRequest request);
  Mono<Void> deleteCreditCard(String id);
}

