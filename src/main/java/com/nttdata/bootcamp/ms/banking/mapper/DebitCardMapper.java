package com.nttdata.bootcamp.ms.banking.mapper;

import com.nttdata.bootcamp.ms.banking.entity.DebitCard;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.DebitCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.DebitCardResponse;
import org.springframework.stereotype.Component;

@Component
public class DebitCardMapper {

  public static DebitCard toEntity(DebitCardRequest request) {
    DebitCard card = new DebitCard();
    card.setCustomerId(request.getCustomerId());
    card.setCardType(request.getCardType());
    card.setAssociatedAccounts(request.getAssociatedAccounts());
    card.setPrimaryAccount(request.getPrimaryAccount());
    card.setStatus(RecordStatus.ACTIVE);
    card.setType("DEBIT"); // Discriminator
    return card;
  }

  public DebitCardResponse toResponse(DebitCard card) {
    DebitCardResponse resp = new DebitCardResponse();
    resp.setId(card.getId());
    resp.setCustomerId(card.getCustomerId());
    resp.setCardType(card.getCardType());
    resp.setAssociatedAccounts(card.getAssociatedAccounts());
    resp.setPrimaryAccount(card.getPrimaryAccount());
    resp.setStatus(card.getStatus().name());
    return resp;
  }
}