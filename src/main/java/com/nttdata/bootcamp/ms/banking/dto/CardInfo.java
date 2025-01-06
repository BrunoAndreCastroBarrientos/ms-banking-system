package com.nttdata.bootcamp.ms.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entidad crédito
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardInfo {
  private String cardId;
  private String cardType; // "CREDIT" o "DEBIT"
  private BigDecimal balanceOrConsumed;
  private BigDecimal availableLimit; // si es de crédito
}
