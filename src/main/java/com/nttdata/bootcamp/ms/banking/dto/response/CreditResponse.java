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
public class CreditResponse {
  private String id;                        // ID del crédito
  private String customerId;
  private String creditType;                // "PERSONAL", "ENTERPRISE"
  private BigDecimal principalAmount;
  private BigDecimal outstandingBalance;
  private BigDecimal interestRate;
  private BigDecimal monthlyPayment;
  private LocalDate dueDate;
  private String status;                    // "ACTIVE", "CANCELLED", etc.
  private boolean overdue;                  // Indica si está vencido/moroso (puede ser útil)
}
