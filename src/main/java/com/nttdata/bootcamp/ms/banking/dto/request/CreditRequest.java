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
public class CreditRequest {
  private String customerId;           // ID del cliente
  private String creditType;           // "PERSONAL" o "ENTERPRISE"
  private BigDecimal principalAmount;
  private BigDecimal interestRate;     // Tasa de interés
  private BigDecimal monthlyPayment;   // Cuota mensual (si aplica)
  private LocalDate dueDate;
}
