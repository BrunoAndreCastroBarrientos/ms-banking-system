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

  public Wallet toEntity(WalletRequest request) {
    Wallet wallet = new Wallet();
    wallet.setIdentificationNumber(request.getIdentificationNumber());
    wallet.setPhoneNumber(request.getPhoneNumber());
    wallet.setEmail(request.getEmail());
    wallet.setBalance(request.getBalance());
    return wallet;
  }

  public WalletResponse toResponse(Wallet wallet) {
    WalletResponse response = new WalletResponse();
    response.setId(wallet.getId());
    response.setIdentificationNumber(wallet.getIdentificationNumber());
    response.setPhoneNumber(wallet.getPhoneNumber());
    response.setEmail(wallet.getEmail());
    response.setBalance(wallet.getBalance());
    return response;
  }
}