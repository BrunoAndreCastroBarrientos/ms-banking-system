package com.nttdata.bootcamp.ms.banking.mapper;

import com.nttdata.bootcamp.ms.banking.entity.CreditCard;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.CreditCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditCardResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class CreditCardMapper {

  public CreditCard toEntity(CreditCardRequest request) {
    CreditCard creditCard = new CreditCard();
    creditCard.setCardNumber(request.getCardNumber());
    creditCard.setCustomerId(request.getCustomerId());
    creditCard.setType(request.getType());
    creditCard.setCardType(request.getCardType());
    creditCard.setCreditLimit(request.getCreditLimit());
    creditCard.setBalance(request.getBalance());
    creditCard.setCutoffDate(request.getCutoffDate());
    creditCard.setStatus(request.getStatus());
    return creditCard;
  }

  public CreditCardResponse toResponse(CreditCard creditCard) {
    CreditCardResponse response = new CreditCardResponse();
    response.setId(creditCard.getId());
    response.setCardNumber(creditCard.getCardNumber());
    response.setCustomerId(creditCard.getCustomerId());
    response.setType(creditCard.getType());
    response.setCardType(creditCard.getCardType());
    response.setCreditLimit(creditCard.getCreditLimit());
    response.setBalance(creditCard.getBalance());
    response.setCutoffDate(creditCard.getCutoffDate());
    response.setStatus(creditCard.getStatus());
    return response;
  }
}