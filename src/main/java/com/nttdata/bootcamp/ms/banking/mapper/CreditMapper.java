package com.nttdata.bootcamp.ms.banking.mapper;

import com.nttdata.bootcamp.ms.banking.entity.Credit;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.CreditRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditResponse;
import org.springframework.stereotype.Component;

@Component
public class CreditMapper {

  public Credit toEntity(CreditRequest request) {
    Credit credit = new Credit();
    credit.setCustomerId(request.getCustomerId());
    credit.setCreditType(request.getCreditType());
    credit.setAmount(request.getAmount());
    //credit.setDebt(request.getDebt());
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
}