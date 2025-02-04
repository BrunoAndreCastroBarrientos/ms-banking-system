package com.nttdata.bootcamp.ms.banking.transaction.mapper;

import com.nttdata.bootcamp.ms.banking.transaction.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.transaction.dto.request.AccountRequest;
import com.nttdata.bootcamp.ms.banking.transaction.dto.response.AccountResponse;
import com.nttdata.bootcamp.ms.banking.transaction.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

  public Account toEntity(AccountRequest request) {
    Account account = new Account();
    account.setCustomerId(request.getCustomerId());
    account.setAccountType(request.getAccountType());
    account.setBalance(request.getBalance());
    account.setCurrency(request.getCurrency());
    account.setOpenDate(request.getOpenDate());
    account.setStatus(RecordStatus.valueOf("ACTIVE"));
    return account;
  }

  public AccountResponse toResponse(Account account) {
    AccountResponse response = new AccountResponse();
    response.setId(account.getId());
    response.setCustomerId(account.getCustomerId());
    response.setAccountType(account.getAccountType());
    response.setBalance(account.getBalance());
    response.setCurrency(account.getCurrency());
    response.setOpenDate(account.getOpenDate());
    response.setStatus(account.getStatus());
    return response;
  }

  public static AccountResponse toResponseStatic(Account account) {
    AccountResponse response = new AccountResponse();
    response.setId(account.getId());
    response.setCustomerId(account.getCustomerId());
    response.setAccountType(account.getAccountType());
    response.setBalance(account.getBalance());
    response.setCurrency(account.getCurrency());
    response.setOpenDate(account.getOpenDate());
    response.setStatus(account.getStatus());
    return response;
  }

  public Account responseToEntity(AccountResponse response) {
    Account account = new Account();
    account.setId(response.getId());
    account.setCustomerId(response.getCustomerId());
    account.setAccountType(response.getAccountType());
    account.setBalance(response.getBalance());
    account.setCurrency(response.getCurrency());
    account.setOpenDate(response.getOpenDate());
    account.setStatus(response.getStatus());
    return account;
  }

  public AccountRequest entityToRequest(Account account) {
    AccountRequest request = new AccountRequest();
    request.setCustomerId(account.getCustomerId());
    request.setAccountType(account.getAccountType());
    request.setBalance(account.getBalance());
    request.setCurrency(account.getCurrency());
    request.setOpenDate(account.getOpenDate());
    return request;
  }
}