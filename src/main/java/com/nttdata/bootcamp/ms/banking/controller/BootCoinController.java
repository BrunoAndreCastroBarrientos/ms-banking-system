package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.dto.request.BootCoinRequest;
import com.nttdata.bootcamp.ms.banking.dto.request.WalletRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.BootCoinResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.WalletResponse;
import com.nttdata.bootcamp.ms.banking.service.BootcoinService;
import com.nttdata.bootcamp.ms.banking.service.WalletService;
import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "BootCoin Controller", description = "Endpoints for managing BootCoin transactions")
public class BootCoinController {

  private final BootcoinService bootcoinService;

  /**
   * Endpoint to buy BootCoin.
   *
   * @param request the BootCoin request details
   * @param amount  the amount of BootCoin to buy
   * @return the response containing transaction details
   */
  @Operation(summary = "Buy BootCoin", description = "Allows users to buy BootCoin by specifying the amount.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = ConstantUtil.ERROR_MESSAGE)
  @PostMapping("/buy")
  public Mono<BootCoinResponse> buyBootCoin(
      @RequestBody BootCoinRequest request,
      @RequestParam BigDecimal amount) {
    return bootcoinService.buyBootCoin(request, amount);
  }

  /**
   * Endpoint to sell BootCoin.
   *
   * @param request the BootCoin request details
   * @param amount  the amount of BootCoin to sell
   * @return the response containing transaction details
   */
  @Operation(summary = "Sell BootCoin", description = "Allows users to sell BootCoin by specifying the amount.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = ConstantUtil.ERROR_MESSAGE)
  @PostMapping("/sell")
  public Mono<BootCoinResponse> sellBootCoin(
      @RequestBody BootCoinRequest request,
      @RequestParam BigDecimal amount) {
    return bootcoinService.sellBootCoin(request, amount);
  }
}

