package com.nttdata.bootcamp.ms.banking.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO para crear o actualizar una cuenta bancaria.
 * Contiene las validaciones necesarias para garantizar la integridad de los datos.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {
  private String customerId;          // ID del cliente dueño de la cuenta
  private String accountType;         // "SAVINGS", "CHECKING", "TIME_DEPOSIT"
  private String currency;            // Moneda (USD, EUR, etc.)
  private Integer transactionsAllowed; // Número máximo de transacciones sin comisión
  private BigDecimal maintenanceFee;  // Comisión de mantenimiento (puede ser 0)
  private LocalDateTime cutoffDate;   // Fecha de corte (para cuentas a plazo fijo, etc.)
}
