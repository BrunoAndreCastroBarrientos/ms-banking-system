package com.nttdata.bootcamp.ms.banking.transaction.mapper;

import com.nttdata.bootcamp.ms.banking.transaction.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.transaction.dto.request.CreditRequest;
import com.nttdata.bootcamp.ms.banking.transaction.dto.response.CreditResponse;
import com.nttdata.bootcamp.ms.banking.transaction.entity.Credit;
import org.springframework.stereotype.Component;

@Component
public class CreditMapper {

  public Credit toEntity(CreditRequest request) {
    Credit credit = new Credit();
    credit.setCustomerId(request.getCustomerId());
    credit.setCreditType(request.getCreditType());
    credit.setAmount(request.getAmount());
    credit.setInterestRate(request.getInterestRate());
    credit.setDueDate(request.getDueDate());
    credit.setStatus(RecordStatus.valueOf("ACTIVE"));
    return credit;
  }

  public CreditResponse toResponse(Credit credit) {
    CreditResponse response = new CreditResponse();
    response.setId(credit.getId());
    response.setCustomerId(credit.getCustomerId());
    response.setCreditType(credit.getCreditType());
    response.setAmount(credit.getAmount());
    response.setDebt(credit.getDebt());
    response.setInterestRate(credit.getInterestRate());
    response.setDueDate(credit.getDueDate());
    response.setStatus(credit.getStatus());
    return response;
  }

  public Credit responseToEntity(CreditResponse response) {
    Credit credit = new Credit();
    credit.setId(response.getId());
    credit.setCustomerId(response.getCustomerId());
    credit.setCreditType(response.getCreditType());
    credit.setAmount(response.getAmount());
    credit.setDebt(response.getDebt());
    credit.setInterestRate(response.getInterestRate());
    credit.setDueDate(response.getDueDate());
    credit.setStatus(response.getStatus());
    return credit;
  }

  public CreditRequest entityToRequest(Credit credit) {
    CreditRequest request = new CreditRequest();
    request.setCustomerId(credit.getCustomerId());
    request.setCreditType(credit.getCreditType());
    request.setAmount(credit.getAmount());
    request.setInterestRate(credit.getInterestRate());
    request.setDueDate(credit.getDueDate());
    return request;
  }
}