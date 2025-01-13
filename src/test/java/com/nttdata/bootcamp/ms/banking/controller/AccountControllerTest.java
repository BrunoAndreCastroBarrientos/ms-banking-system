package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.dto.request.AccountRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.AccountResponse;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountControllerTest {

  @InjectMocks
  private AccountController accountController;

  @Mock
  private AccountService accountService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createAccount_Success() {
    // Arrange
    AccountRequest request = AccountRequest.builder()
        .customerId("123e4567-e89b-12d3-a456-426614174000")
        .accountType("SAVINGS")
        .currency("USD")
        .transactionsAllowed(10)
        .maintenanceFee(BigDecimal.valueOf(5.0))
        .cutoffDate(LocalDateTime.now().plusDays(1))
        .build();

    AccountResponse response = AccountResponse.builder()
        .id("1")
        .customerId(request.getCustomerId())
        .accountType(request.getAccountType())
        .currency(request.getCurrency())
        .balance(BigDecimal.valueOf(100))
        .build();

    when(accountService.createAccount(request)).thenReturn(Mono.just(response));

    // Act
    Mono<AccountResponse> result = accountController.create(request);

    // Assert
    assertNotNull(result);
    AccountResponse actual = result.block();
    assertEquals("1", actual.getId());
    assertEquals("SAVINGS", actual.getAccountType());
    verify(accountService, times(1)).createAccount(request);
  }

  @Test
  void createAccount_InvalidRequest_ThrowsException() {
    // Arrange
    AccountRequest request = AccountRequest.builder().build();

    // Act & Assert
    assertThrows(ApiValidateException.class, () -> accountController.create(request));
  }

  @Test
  void getAccountById_Success() {
    // Arrange
    String accountId = "1";
    AccountResponse response = AccountResponse.builder()
        .id(accountId)
        .customerId("123e4567-e89b-12d3-a456-426614174000")
        .accountType("SAVINGS")
        .balance(BigDecimal.valueOf(100))
        .build();

    when(accountService.getById(accountId)).thenReturn(Mono.just(response));

    // Act
    Mono<AccountResponse> result = accountController.getById(accountId);

    // Assert
    assertNotNull(result);
    AccountResponse actual = result.block();
    assertEquals(accountId, actual.getId());
    assertEquals("SAVINGS", actual.getAccountType());
    verify(accountService, times(1)).getById(accountId);
  }

  @Test
  void getAccountById_NotFound() {
    // Arrange
    String accountId = "1";
    when(accountService.getById(accountId)).thenReturn(Mono.empty());

    // Act
    Mono<AccountResponse> result = accountController.getById(accountId);

    // Assert
    assertNull(result.block());
    verify(accountService, times(1)).getById(accountId);
  }

  @Test
  void debitAccount_Success() {
    // Arrange
    String accountId = "1";
    BigDecimal amount = BigDecimal.valueOf(50);
    BigDecimal newBalance = BigDecimal.valueOf(50);

    when(accountService.debit(accountId, amount)).thenReturn(Mono.just(newBalance));

    // Act
    Mono<BigDecimal> result = accountController.debit(accountId, amount);

    // Assert
    assertNotNull(result);
    assertEquals(newBalance, result.block());
    verify(accountService, times(1)).debit(accountId, amount);
  }

  @Test
  void debitAccount_InvalidAmount_ThrowsException() {
    // Arrange
    String accountId = "1";
    BigDecimal invalidAmount = BigDecimal.ZERO;

    // Act & Assert
    assertThrows(ApiValidateException.class, () -> accountController.debit(accountId, invalidAmount));
  }
}
