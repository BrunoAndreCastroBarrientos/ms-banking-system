package com.nttdata.bootcamp.ms.banking.mapper;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.TransactionBootCoinType;
import com.nttdata.bootcamp.ms.banking.dto.request.BootCoinRequest;
import com.nttdata.bootcamp.ms.banking.dto.request.WalletRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.BootCoinResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.WalletResponse;
import com.nttdata.bootcamp.ms.banking.entity.BootCoinTransaction;
import com.nttdata.bootcamp.ms.banking.entity.Wallet;
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