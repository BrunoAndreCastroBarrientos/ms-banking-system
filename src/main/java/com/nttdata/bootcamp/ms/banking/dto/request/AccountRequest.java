package com.nttdata.bootcamp.ms.banking.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

  @NotBlank(message = "El ID del cliente no puede estar vacío.") //ID del cliente a vinculado
  @Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}",
      message = "El ID del cliente debe ser un UUID válido.")
  private String customerId;

  @NotBlank(message = "El tipo de cuenta no puede estar vacío.") //(SAVINGS, CHECKING, TIME_DEPOSIT)
  @Pattern(regexp = "SAVINGS|CHECKING|TIME_DEPOSIT",
      message = "El tipo de cuenta debe ser SAVINGS, CHECKING o TIME_DEPOSIT.")
  private String accountType;

  @NotBlank(message = "La moneda no puede estar vacía.") //(ISO 4217: USD, EUR, PEN)
  @Pattern(regexp = "USD|EUR|PEN",
      message = "La moneda debe ser USD, EUR o PEN.")
  private String currency;

  @NotNull(message = "El número de transacciones permitidas no puede ser nulo.") // Máximo de transacciones sin comisión
  @Min(value = 0, message = "El número de transacciones permitidas debe ser mayor o igual a 0.")
  private Integer transactionsAllowed;

  @NotNull(message = "La comisión de mantenimiento no puede ser nula.") // Comisión de mantenimiento (>= 0.0)
  @DecimalMin(value = "0.0", inclusive = true,
      message = "La comisión de mantenimiento debe ser mayor o igual a 0.")
  private BigDecimal maintenanceFee;

  @FutureOrPresent(message = "La fecha de corte debe ser futura o presente.") // Fecha de corte de la cuenta
  private LocalDateTime cutoffDate;
}
