package com.nttdata.bootcamp.ms.banking.product.service;

import com.nttdata.bootcamp.ms.banking.product.dto.request.CreditRequest;
import com.nttdata.bootcamp.ms.banking.product.dto.response.CreditResponse;
import reactor.core.publisher.Mono;

public interface CreditService {
  Mono<CreditResponse> createCredit(CreditRequest request);
  Mono<CreditResponse> getCreditById(String id);
  Mono<CreditResponse> updateCredit(String id, CreditRequest request);
  Mono<Void> deleteCredit(String id);
}

