package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.entity.Client;
import com.nttdata.bootcamp.ms.banking.entity.Credit;
import com.nttdata.bootcamp.ms.banking.model.request.ClientRequest;
import com.nttdata.bootcamp.ms.banking.model.request.CreditRequest;
import com.nttdata.bootcamp.ms.banking.model.response.CreditResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {
    Mono<CreditResponse> createCredit(CreditRequest creditRequest);
    Mono<CreditResponse> updateCredit(String creditId, CreditRequest creditRequest);
    Mono<Void> deleteCredit(String creditId);
    Mono<CreditResponse> getCreditById(String creditId);
    Flux<CreditResponse> getCreditsByClientId(String clientId);
}

