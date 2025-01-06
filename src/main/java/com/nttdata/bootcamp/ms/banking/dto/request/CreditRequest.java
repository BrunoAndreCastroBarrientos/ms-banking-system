package com.nttdata.bootcamp.ms.banking.dto.request;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.CreditType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase Request para crear o actualizar un crédito.
 * Este DTO se utiliza para enviar los datos relacionados con un crédito, incluyendo el tipo de crédito,
 * el monto principal, la tasa de interés, las cuotas mensuales, y la fecha de vencimiento.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.1
 */
@Data
@NoArgsConstructor
public class CreditRequest {

  @NotBlank(message = "El ID del cliente no puede estar vacío.") // ID del cliente
  @Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}",
      message = "El ID del cliente debe ser un UUID válido.")
  private String customerId;

  @NotBlank(message = "El tipo de crédito no puede estar vacío.") // Tipo de crédito: PERSONAL o ENTERPRISE
  @Pattern(regexp = "PERSONAL|ENTERPRISE",
      message = "El tipo de crédito debe ser PERSONAL o ENTERPRISE.")
  private CreditType creditType;

  @NotNull(message = "El monto principal no puede ser nulo.") // Monto principal del crédito
  @DecimalMin(value = "0.01", inclusive = true,
      message = "El monto principal debe ser mayor a 0.")
  private BigDecimal principalAmount;

  @NotNull(message = "La tasa de interés no puede ser nula.") // Tasa de interés del crédito
  @DecimalMin(value = "0.01", inclusive = true,
      message = "La tasa de interés debe ser mayor a 0.")
  @Digits(integer = 3, fraction = 2,
      message = "La tasa de interés debe ser un valor válido con hasta 3 enteros y 2 decimales.")
  private BigDecimal interestRate;

  @DecimalMin(value = "0.0", inclusive = true,
      message = "La cuota mensual debe ser mayor o igual a 0.") // Cuota mensual (opcional)
  @Digits(integer = 18, fraction = 2,
      message = "La cuota mensual debe tener como máximo 18 dígitos enteros y 2 decimales.")
  private BigDecimal monthlyPayment;

  @Future(message = "La fecha de vencimiento debe ser una fecha futura.") // Fecha de vencimiento del crédito
  private LocalDate dueDate;
}
