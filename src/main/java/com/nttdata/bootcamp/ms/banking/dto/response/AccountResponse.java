package com.nttdata.bootcamp.ms.banking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO para la cuenta bancaria.
 * Este DTO se utiliza para enviar los datos de la cuenta bancaria como respuesta.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
  private String id;                // ID autogenerado de la cuenta
  private String customerId;        // ID del cliente dueño
  private String accountType;       // "SAVINGS", "CHECKING", "TIME_DEPOSIT"
  private BigDecimal balance;       // Saldo actual
  private String currency;          // Moneda (USD, EUR, etc.)
  private LocalDateTime openDate;   // Fecha de apertura
  private String status;            // "ACTIVE", "CLOSED", etc.
  private Integer transactionsAllowed;
  private BigDecimal maintenanceFee;
  private LocalDateTime cutoffDate; // Para cuentas de plazo fijo u otras configuraciones

}

