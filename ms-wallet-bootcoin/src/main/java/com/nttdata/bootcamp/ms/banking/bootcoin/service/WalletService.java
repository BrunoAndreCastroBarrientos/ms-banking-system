package com.nttdata.bootcamp.ms.banking.bootcoin.service;

import com.nttdata.bootcamp.ms.banking.bootcoin.dto.request.WalletRequest;
import com.nttdata.bootcamp.ms.banking.bootcoin.dto.response.WalletResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface WalletService {
  Mono<WalletResponse> sendPayment(WalletRequest request, BigDecimal amount);
  Mono<WalletResponse> receivePayment(WalletRequest request, BigDecimal amount);
  Mono<WalletResponse> getWalletDetails(String phoneNumber);
}




