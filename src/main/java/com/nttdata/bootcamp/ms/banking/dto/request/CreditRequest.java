package com.nttdata.bootcamp.ms.banking.dto.request;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.CreditType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
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

  @NotBlank(message = "Customer ID is mandatory")
  @Pattern(regexp = "^[A-Za-z0-9]{24}$", message = "Customer ID must be a 24-character alphanumeric string")
  private String customerId; // ID del cliente titular del crédito

  @NotNull(message = "Credit type is mandatory")
  private CreditType creditType; // Tipo de crédito (PERSONAL, ENTERPRISE)

  @NotNull(message = "Amount is mandatory")
  @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
  private BigDecimal amount; // Monto principal del crédito

  @NotNull(message = "Interest rate is mandatory")
  @DecimalMin(value = "0.0", inclusive = false, message = "Interest rate must be greater than zero")
  private BigDecimal interestRate; // Tasa de interés aplicada

  @NotNull(message = "Due date is mandatory")
  private LocalDate dueDate; // Fecha de vencimiento

  @NotNull(message = "Status is mandatory")
  private RecordStatus status; // Estado del crédito (ACTIVE, CANCELLED, etc.)
}
