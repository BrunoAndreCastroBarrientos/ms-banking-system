package com.nttdata.bootcamp.ms.banking.product.mapper;

import com.nttdata.bootcamp.ms.banking.product.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.product.dto.request.CreditCardRequest;
import com.nttdata.bootcamp.ms.banking.product.dto.response.CreditCardResponse;
import com.nttdata.bootcamp.ms.banking.product.entity.CreditCard;
import org.springframework.stereotype.Component;

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
    creditCard.setStatus(RecordStatus.valueOf("ACTIVE"));
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

  public CreditCard responseToEntity(CreditCardResponse response) {
    CreditCard creditCard = new CreditCard();
    creditCard.setId(response.getId());
    creditCard.setCardNumber(response.getCardNumber());
    creditCard.setCustomerId(response.getCustomerId());
    creditCard.setType(response.getType());
    creditCard.setCardType(response.getCardType());
    creditCard.setCreditLimit(response.getCreditLimit());
    creditCard.setBalance(response.getBalance());
    creditCard.setCutoffDate(response.getCutoffDate());
    creditCard.setStatus(response.getStatus());
    return creditCard;
  }
}