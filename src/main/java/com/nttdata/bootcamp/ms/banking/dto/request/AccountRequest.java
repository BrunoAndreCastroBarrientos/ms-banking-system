package com.nttdata.bootcamp.ms.banking.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.AccountType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

/**
 * Request DTO para crear o actualizar una cuenta bancaria.
 * Contiene las validaciones necesarias para garantizar la integridad de los datos.
 *
 * @author Bruno Andre
 * @version 1.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

  @NotBlank(message = "Customer ID is mandatory")
  @Pattern(regexp = "^[A-Za-z0-9]{24}$", message = "Customer ID must be a 24-character alphanumeric string")
  private String customerId; // ID del cliente propietario de la cuenta

  @NotNull(message = "Account type is mandatory")
  private AccountType accountType; // Tipo de cuenta (SAVINGS, CHECKING, TIME_DEPOSIT)

  @NotNull(message = "Balance is mandatory")
  @DecimalMin(value = "0.0", inclusive = true, message = "Balance must be non-negative")
  private BigDecimal balance; // Saldo actual de la cuenta

  @NotBlank(message = "Currency is mandatory")
  @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter uppercase code (e.g., USD, EUR)")
  private String currency; // Moneda de la cuenta (ej. USD, EUR, etc.)

  @NotNull(message = "Open date is mandatory")
  private LocalDateTime openDate; // Fecha de apertura de la cuenta

  @NotNull(message = "Status is mandatory")
  private RecordStatus status; // Estado de la cuenta (ACTIVE, CLOSED, etc.)
}
