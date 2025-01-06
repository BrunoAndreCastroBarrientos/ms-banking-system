package com.nttdata.bootcamp.ms.banking.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Clase Response que representa la respuesta de un crédito.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class CreditCardResponse {
  private String id;
  private String customerId;
  private String cardType;
  private BigDecimal creditLimit;
  private BigDecimal availableLimit;
  private BigDecimal balance;
  private LocalDate cutoffDate;
  private String status; // ACTIVE, BLOCKED, etc.
}
