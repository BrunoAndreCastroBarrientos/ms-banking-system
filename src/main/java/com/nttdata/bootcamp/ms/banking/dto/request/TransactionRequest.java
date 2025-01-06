package com.nttdata.bootcamp.ms.banking.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Clase Request para crear o actualizar un crédito.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class TransactionRequest {
  private String transactionType;   // "DEPOSIT", "WITHDRAWAL", "TRANSFER", "CREDIT_PAYMENT", "CREDIT_CARD_PAYMENT"
  private String originAccountId;
  private String destinationAccountId;
  private String creditId;
  private String creditCardId;
  private String debitCardId;       // si aplica
  private BigDecimal amount;
}
