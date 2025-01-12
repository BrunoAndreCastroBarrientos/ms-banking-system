package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.dto.request.AccountRequest;
import com.nttdata.bootcamp.ms.banking.dto.request.WalletRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.AccountResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.WalletResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {
  Mono<WalletResponse> createWallet(WalletRequest request);
  Mono<WalletResponse> getWalletByPhoneNumber(String phoneNumber);
  Mono<WalletResponse> associateDebitCard(String phoneNumber, String debitCardNumber);
  Mono<WalletResponse> sendPayment(String fromPhoneNumber, String toPhoneNumber, Double amount);
}




