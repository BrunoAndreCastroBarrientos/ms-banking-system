package com.nttdata.bootcamp.ms.banking.mapper;

import com.nttdata.bootcamp.ms.banking.entity.Transaction;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.TransactionType;
import com.nttdata.bootcamp.ms.banking.dto.request.TransactionRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.TransactionResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class TransactionMapper {

  public Transaction requestToEntity(TransactionRequest request) {
    Transaction tx = new Transaction();
    tx.setTransactionType(TransactionType.valueOf(request.getTransactionType()));
    tx.setOriginAccountId(request.getOriginAccountId());
    tx.setDestinationAccountId(request.getDestinationAccountId());
    tx.setCreditId(request.getCreditId());
    tx.setCreditCardId(request.getCreditCardId());
    tx.setDebitCardId(request.getDebitCardId());
    tx.setAmount(request.getAmount());
    tx.setTransactionDate(LocalDateTime.now());
    // Por defecto, sin comisión o se calculan en el service
    tx.setCommission(BigDecimal.ZERO);
    return tx;
  }

  public TransactionResponse entityToResponse(Transaction tx) {
    TransactionResponse resp = new TransactionResponse();
    resp.setId(tx.getId());
    resp.setTransactionType(tx.getTransactionType().name());
    resp.setOriginAccountId(tx.getOriginAccountId());
    resp.setDestinationAccountId(tx.getDestinationAccountId());
    resp.setCreditId(tx.getCreditId());
    resp.setCreditCardId(tx.getCreditCardId());
    resp.setDebitCardId(tx.getDebitCardId());
    resp.setAmount(tx.getAmount());
    resp.setTransactionDate(tx.getTransactionDate());
    resp.setCommission(tx.getCommission());
    return resp;
  }
}