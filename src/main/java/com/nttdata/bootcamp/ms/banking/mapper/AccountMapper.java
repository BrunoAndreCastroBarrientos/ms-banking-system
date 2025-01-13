package com.nttdata.bootcamp.ms.banking.mapper;

import com.nttdata.bootcamp.ms.banking.entity.Account;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.AccountType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.AccountRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.AccountResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class AccountMapper {

  public static Account toEntity(AccountRequest request) {
    Account account = new Account();
    account.setCustomerId(request.getCustomerId());
    account.setAccountType(AccountType.valueOf(request.getAccountType()));
    account.setCurrency(request.getCurrency());
    account.setBalance(BigDecimal.ZERO); // Al crear una cuenta, saldo inicial 0
    account.setOpenDate(LocalDateTime.now());
    account.setStatus(RecordStatus.ACTIVE);
    account.setTransactionsAllowed(request.getTransactionsAllowed());
    account.setMaintenanceFee(request.getMaintenanceFee());
    // cutoffDate podría usarse para TimeDeposit, Checking VIP, etc.
    // OJO: Validar que solo aplique si es TIME_DEPOSIT
    account.setCutoffDate(request.getCutoffDate());
    return account;
  }

  public AccountResponse toResponse(Account account) {
    AccountResponse response = new AccountResponse();
    response.setId(account.getId());
    response.setCustomerId(account.getCustomerId());
    response.setAccountType(account.getAccountType().name());
    response.setBalance(account.getBalance());
    response.setCurrency(account.getCurrency());
    response.setOpenDate(account.getOpenDate());
    response.setStatus(account.getStatus().name());
    response.setTransactionsAllowed(account.getTransactionsAllowed());
    response.setMaintenanceFee(account.getMaintenanceFee());
    response.setCutoffDate(account.getCutoffDate());
    return response;
  }
}