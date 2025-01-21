package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.AccountType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.AccountRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.AccountResponse;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.service.AccountService;
import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing account-related operations.
 *
 * <p>This controller provides endpoints for creating,
 * updating, retrieving, and closing accounts.</p>
 *
 * @version 1.1
 * @author Bruno Andre Castro Barrientos
 */

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @PostMapping
  public Mono<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request) {
    return accountService.createAccount(request);
  }

  @GetMapping
  public Flux<AccountResponse> getAllAccounts() {
    return accountService.getAllAccounts();
  }

  @GetMapping("/{id}")
  public Mono<AccountResponse> getAccountById(@PathVariable String id) {
    return accountService.getAccountById(id);
  }

  @GetMapping("/customer/{customerId}")
  public Flux<AccountResponse> getAccountsByCustomerId(@PathVariable String customerId) {
    return accountService.getAccountsByCustomerId(customerId);
  }

  @PutMapping("/{id}")
  public Mono<AccountResponse> updateAccount(@PathVariable String id, @Valid @RequestBody AccountRequest request) {
    return accountService.updateAccount(id, request);
  }

  @DeleteMapping("/{id}")
  public Mono<Void> deleteAccount(@PathVariable String id) {
    return accountService.deleteAccount(id);
  }
}