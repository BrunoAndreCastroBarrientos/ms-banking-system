package com.nttdata.bootcamp.ms.banking.product.mapper;

import com.nttdata.bootcamp.ms.banking.product.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.product.dto.request.DebitCardRequest;
import com.nttdata.bootcamp.ms.banking.product.dto.response.DebitCardResponse;
import com.nttdata.bootcamp.ms.banking.product.entity.DebitCard;
import org.springframework.stereotype.Component;

@Component
public class DebitCardMapper {

  public DebitCard toEntity(DebitCardRequest request) {
    DebitCard debitCard = new DebitCard();
    debitCard.setCustomerId(request.getCustomerId());
    debitCard.setCustomerId(request.getCustomerId());
    debitCard.setType(request.getType());
    debitCard.setCardType(request.getCardType());
    debitCard.setAssociatedAccounts(request.getAssociatedAccounts());
    debitCard.setStatus(RecordStatus.valueOf("ACTIVE"));
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