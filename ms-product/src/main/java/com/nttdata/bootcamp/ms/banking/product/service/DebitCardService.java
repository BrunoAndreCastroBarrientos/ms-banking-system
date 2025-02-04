package com.nttdata.bootcamp.ms.banking.product.service;

import com.nttdata.bootcamp.ms.banking.product.dto.request.DebitCardRequest;
import com.nttdata.bootcamp.ms.banking.product.dto.response.DebitCardResponse;
import reactor.core.publisher.Mono;

public interface DebitCardService {
  Mono<DebitCardResponse> createDebitCard(DebitCardRequest request);
  Mono<DebitCardResponse> getDebitCardById(String id);
  Mono<DebitCardResponse> updateDebitCard(String id, DebitCardRequest request);
  Mono<Void> deleteDebitCard(String id);
}

