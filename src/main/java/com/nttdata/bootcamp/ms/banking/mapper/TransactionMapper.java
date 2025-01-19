package com.nttdata.bootcamp.ms.banking.mapper;

import com.nttdata.bootcamp.ms.banking.entity.Transaction;
import com.nttdata.bootcamp.ms.banking.dto.request.TransactionRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.TransactionResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class TransactionMapper {

  public Transaction toEntity(TransactionRequest request) {
    Transaction transaction = new Transaction();
    transaction.setTransactionType(request.getTransactionType());
    transaction.setOriginAccountId(request.getOriginAccountId());
    transaction.setDestinationAccountId(request.getDestinationAccountId());
    transaction.setCreditId(request.getCreditId());
    transaction.setCreditCardId(request.getCreditCardId());
    transaction.setDebitCardId(request.getDebitCardId());
    transaction.setAmount(request.getAmount());
    transaction.setTransactionDate(request.getTransactionDate());
    return transaction;
  }

  public TransactionResponse toResponse(Transaction transaction) {
    TransactionResponse response = new TransactionResponse();
    response.setId(transaction.getId());
    response.setTransactionType(transaction.getTransactionType());
    response.setOriginAccountId(transaction.getOriginAccountId());
    response.setDestinationAccountId(transaction.getDestinationAccountId());
    response.setCreditId(transaction.getCreditId());
    response.setCreditCardId(transaction.getCreditCardId());
    response.setDebitCardId(transaction.getDebitCardId());
    response.setAmount(transaction.getAmount());
    response.setTransactionDate(transaction.getTransactionDate());
    return response;
  }
}