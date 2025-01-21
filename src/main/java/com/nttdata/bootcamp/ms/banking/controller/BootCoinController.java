package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.dto.request.BootCoinRequest;
import com.nttdata.bootcamp.ms.banking.dto.request.WalletRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.BootCoinResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.WalletResponse;
import com.nttdata.bootcamp.ms.banking.service.BootcoinService;
import com.nttdata.bootcamp.ms.banking.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Controller for managing transaction-related operations.
 * Provides endpoints for creating, retrieving, and managing transactions.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@RestController
@RequestMapping("/api/bootcoin")
@RequiredArgsConstructor
public class BootCoinController {

  private final BootcoinService bootcoinService;

  @PostMapping("/buy")
  public Mono<BootCoinResponse> buyBootCoin(
      @RequestBody BootCoinRequest request,
      @RequestParam BigDecimal amount) {
    return bootcoinService.buyBootCoin(request, amount);
  }

  @PostMapping("/sell")
  public Mono<BootCoinResponse> sellBootCoin(
      @RequestBody BootCoinRequest request,
      @RequestParam BigDecimal amount) {
    return bootcoinService.sellBootCoin(request, amount);
  }
}
