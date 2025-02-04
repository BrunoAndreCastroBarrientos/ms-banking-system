package com.nttdata.bootcamp.ms.banking.bootcoin.service;

import com.nttdata.bootcamp.ms.banking.bootcoin.dto.request.BootCoinRequest;
import com.nttdata.bootcamp.ms.banking.bootcoin.dto.response.BootCoinResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface BootcoinService {
  Mono<BootCoinResponse> buyBootCoin(BootCoinRequest request, BigDecimal amount);
  Mono<BootCoinResponse> sellBootCoin(BootCoinRequest request, BigDecimal amount);
}




