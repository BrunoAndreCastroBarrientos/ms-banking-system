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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Controller for managing wallet-related operations.
 * Provides endpoints for sending and receiving payments, and retrieving wallet details.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
@Tag(name = "Wallet Controller", description = "Endpoints for managing wallets and performing wallet operations")
public class WalletController {

  private final WalletService walletService;

  /**
   * Endpoint to send a payment.
   *
   * @param request the wallet request details
   * @param amount  the amount to be sent
   * @return the response containing the transaction details
   */
  @Operation(summary = "Send Payment",
      description = "Sends a payment from one wallet to another.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = ConstantUtil.ERROR_MESSAGE)
  @PostMapping("/send-payment")
  public Mono<WalletResponse> sendPayment(@Valid @RequestBody WalletRequest request, @RequestParam BigDecimal amount) {
    return walletService.sendPayment(request, amount);
  }

  /**
   * Endpoint to receive a payment.
   *
   * @param request the wallet request details
   * @param amount  the amount to be received
   * @return the response containing the transaction details
   */
  @Operation(summary = "Receive Payment",
      description = "Receives a payment into a wallet.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = ConstantUtil.ERROR_MESSAGE)
  @PostMapping("/receive-payment")
  public Mono<WalletResponse> receivePayment(@Valid @RequestBody WalletRequest request, @RequestParam BigDecimal amount) {
    return walletService.receivePayment(request, amount);
  }

  /**
   * Endpoint to retrieve wallet details by phone number.
   *
   * @param phoneNumber the phone number associated with the wallet
   * @return the response containing the wallet details
   */
  @Operation(summary = "Get Wallet Details",
      description = "Retrieves wallet details based on the associated phone number.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = ConstantUtil.NOT_FOUND_MESSAGE)
  @GetMapping("/{phoneNumber}")
  public Mono<WalletResponse> getWalletDetails(@PathVariable String phoneNumber) {
    return walletService.getWalletDetails(phoneNumber);
  }
}

