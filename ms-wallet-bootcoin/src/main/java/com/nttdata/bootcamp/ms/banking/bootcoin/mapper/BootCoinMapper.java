package com.nttdata.bootcamp.ms.banking.bootcoin.mapper;

import com.nttdata.bootcamp.ms.banking.bootcoin.dto.enumeration.TransactionBootCoinType;
import com.nttdata.bootcamp.ms.banking.bootcoin.dto.request.BootCoinRequest;
import com.nttdata.bootcamp.ms.banking.bootcoin.dto.response.BootCoinResponse;
import com.nttdata.bootcamp.ms.banking.bootcoin.entity.BootCoinTransaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BootCoinMapper {

  public static BootCoinResponse toResponse(BootCoinTransaction transaction) {
    return new BootCoinResponse(
        transaction.getPhoneNumber(),
        transaction.getAmount(),
        transaction.getTransactionBootCoinType(),
        transaction.getTransactionDate()
    );
  }

  public static BootCoinTransaction toEntity(BootCoinRequest request, TransactionBootCoinType transactionType) {
    return BootCoinTransaction.builder()
        .phoneNumber(request.getPhoneNumber())
        .amount(request.getAmount())
        .transactionBootCoinType(transactionType)
        .transactionDate(LocalDateTime.now())
        .build();
  }
}