package com.nttdata.bootcamp.ms.banking.transaction.service;

import com.nttdata.bootcamp.ms.banking.transaction.dto.request.TransactionRequest;
import com.nttdata.bootcamp.ms.banking.transaction.dto.response.TransactionResponse;
import reactor.core.publisher.Mono;

public interface TransactionService {
  Mono<TransactionResponse> processTransaction(TransactionRequest request);
}

