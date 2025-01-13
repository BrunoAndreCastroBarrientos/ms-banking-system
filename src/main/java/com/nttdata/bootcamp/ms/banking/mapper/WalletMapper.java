package com.nttdata.bootcamp.ms.banking.mapper;

import com.nttdata.bootcamp.ms.banking.dto.request.TransactionRequest;
import com.nttdata.bootcamp.ms.banking.dto.request.WalletRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.TransactionResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.WalletResponse;
import com.nttdata.bootcamp.ms.banking.entity.Transaction;
import com.nttdata.bootcamp.ms.banking.entity.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class WalletMapper {

  public static Wallet toEntity(WalletRequest request) {
    Wallet wallet = new Wallet();
    wallet.setDocumentId(request.getDocumentId());
    wallet.setPhoneNumber(request.getPhoneNumber());
    wallet.setImei(request.getImei());
    wallet.setEmail(request.getEmail());
    wallet.setBalance(0.0);
    return wallet;
  }

  public static WalletResponse toResponse(Wallet wallet) {
    WalletResponse response = new WalletResponse();
    response.setPhoneNumber(wallet.getPhoneNumber());
    response.setBalance(wallet.getBalance());
    return response;
  }
}