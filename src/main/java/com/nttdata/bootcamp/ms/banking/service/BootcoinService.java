package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.dto.request.BootCoinRequest;
import com.nttdata.bootcamp.ms.banking.dto.request.WalletRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.BootCoinResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.WalletResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface BootcoinService {
  Mono<BootCoinResponse> buyBootCoin(BootCoinRequest request, BigDecimal amount);
  Mono<BootCoinResponse> sellBootCoin(BootCoinRequest request, BigDecimal amount);
}




