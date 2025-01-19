package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.dto.request.CreditCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditCardResponse;
import com.nttdata.bootcamp.ms.banking.entity.CreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditCardService {
  Mono<CreditCardResponse> createCreditCard(CreditCardRequest request);
  Mono<CreditCardResponse> getCreditCardById(String id);
  Mono<CreditCardResponse> updateCreditCard(String id, CreditCardRequest request);
  Mono<Void> deleteCreditCard(String id);
}

