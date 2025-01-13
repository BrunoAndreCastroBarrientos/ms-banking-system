package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.dto.request.TransactionRequest;
import com.nttdata.bootcamp.ms.banking.dto.request.WalletRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.TransactionResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.WalletResponse;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.service.TransactionService;
import com.nttdata.bootcamp.ms.banking.service.WalletService;
import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller for managing transaction-related operations.
 * Provides endpoints for creating, retrieving, and managing transactions.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

  private final WalletService walletService;

  @PostMapping
  public Mono<WalletResponse> createWallet(@RequestBody WalletRequest request) {
    return walletService.createWallet(request);
  }

  @GetMapping("/{phoneNumber}")
  public Mono<WalletResponse> getWallet(@PathVariable String phoneNumber) {
    return walletService.getWalletByPhoneNumber(phoneNumber);
  }

  @PostMapping("/associate")
  public Mono<WalletResponse> associateDebitCard(@RequestParam String phoneNumber, @RequestParam String debitCardNumber) {
    return walletService.associateDebitCard(phoneNumber, debitCardNumber);
  }

  @PostMapping("/send-payment")
  public Mono<WalletResponse> sendPayment(@RequestParam String fromPhoneNumber, @RequestParam String toPhoneNumber, @RequestParam Double amount) {
    return walletService.sendPayment(fromPhoneNumber, toPhoneNumber, amount);
  }
}
