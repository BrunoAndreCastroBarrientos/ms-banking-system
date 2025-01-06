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

  public CreditCard requestToEntity(CreditCardRequest request) {
    CreditCard card = new CreditCard();
    card.setCustomerId(request.getCustomerId());
    card.setCardType(request.getCardType());
    card.setCreditLimit(request.getCreditLimit());
    card.setAvailableLimit(
        request.getAvailableLimit() != null ? request.getAvailableLimit() : request.getCreditLimit());
    card.setBalance(request.getBalance() != null ? request.getBalance() : BigDecimal.ZERO);
    card.setCutoffDate(request.getCutoffDate() != null ? request.getCutoffDate() : LocalDate.now().plusMonths(1));
    card.setStatus(RecordStatus.ACTIVE);
    card.setType("CREDIT"); // Discriminator
    return card;
  }

  public CreditCardResponse entityToResponse(CreditCard card) {
    CreditCardResponse resp = new CreditCardResponse();
    resp.setId(card.getId());
    resp.setCustomerId(card.getCustomerId());
    resp.setCardType(card.getCardType());
    resp.setCreditLimit(card.getCreditLimit());
    resp.setAvailableLimit(card.getAvailableLimit());
    resp.setBalance(card.getBalance());
    resp.setCutoffDate(card.getCutoffDate());
    resp.setStatus(card.getStatus().name());
    return resp;
  }
}