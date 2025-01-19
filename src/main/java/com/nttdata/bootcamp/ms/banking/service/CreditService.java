package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.dto.request.CreditRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditResponse;
import com.nttdata.bootcamp.ms.banking.entity.Credit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface CreditService {
  Mono<CreditResponse> createCredit(CreditRequest request);
  Mono<CreditResponse> getCreditById(String id);
  Mono<CreditResponse> updateCredit(String id, CreditRequest request);
  Mono<Void> deleteCredit(String id);
}

