package com.nttdata.bootcamp.ms.banking.mapper;

import com.nttdata.bootcamp.ms.banking.entity.DebitCard;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.DebitCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.DebitCardResponse;
import org.springframework.stereotype.Component;

@Component
public class DebitCardMapper {

  public DebitCard toEntity(DebitCardRequest request) {
    DebitCard debitCard = new DebitCard();
    debitCard.setCustomerId(request.getCustomerId());
    debitCard.setType(request.getType());
    debitCard.setCardType(request.getCardType());
    debitCard.setAssociatedAccounts(request.getAssociatedAccounts());
    debitCard.setStatus(request.getStatus());
    return debitCard;
  }

  public DebitCardResponse toResponse(DebitCard debitCard) {
    DebitCardResponse response = new DebitCardResponse();
    response.setId(debitCard.getId());
    response.setCustomerId(debitCard.getCustomerId());
    response.setType(debitCard.getType());
    response.setCardType(debitCard.getCardType());
    response.setAssociatedAccounts(debitCard.getAssociatedAccounts());
    response.setStatus(debitCard.getStatus());
    return response;
  }
}