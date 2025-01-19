package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.dto.request.DebitCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.DebitCardResponse;
import com.nttdata.bootcamp.ms.banking.entity.DebitCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DebitCardService {
  Mono<DebitCardResponse> createDebitCard(DebitCardRequest request);
  Mono<DebitCardResponse> getDebitCardById(String id);
  Mono<DebitCardResponse> updateDebitCard(String id, DebitCardRequest request);
  Mono<Void> deleteDebitCard(String id);
}

