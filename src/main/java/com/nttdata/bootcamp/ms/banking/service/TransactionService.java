package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.dto.request.TransactionRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.TransactionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {
  Mono<TransactionResponse> processTransaction(TransactionRequest request);

  Mono<TransactionResponse> getById(String id);

  Flux<TransactionResponse> getAll();

  /**
   * Métodos adicionales para buscar transacciones
   * p.ej. por tipo, por cuenta, por fecha, etc.
   */
  Flux<TransactionResponse> getByAccountId(String accountId);

  Flux<TransactionResponse> getByCreditId(String creditId);

  Flux<TransactionResponse> getByCreditCardId(String creditCardId);

  Flux<TransactionResponse> getByDebitCardId(String debitCardId);

}

