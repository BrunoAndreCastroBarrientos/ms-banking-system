package com.nttdata.bootcamp.ms.banking.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Clase Request para crear o actualizar un crédito.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class CreditCardRequest {
  private String customerId;
  private String cardType;          // PERSONAL o ENTERPRISE
  private BigDecimal creditLimit;
  private BigDecimal availableLimit; // opcional, si no se pasa se iguala al creditLimit
  private BigDecimal balance;        // inicial (normalmente 0)
  private LocalDate cutoffDate;      // fecha de corte
}
