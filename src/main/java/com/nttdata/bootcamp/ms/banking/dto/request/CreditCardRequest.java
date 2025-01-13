package com.nttdata.bootcamp.ms.banking.dto.request;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.CardType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase Request para crear o actualizar un crédito.
 * Este DTO se utiliza para enviar los datos de la tarjeta de crédito, con los detalles
 * correspondientes del cliente, tipo de tarjeta, límites de crédito y balance.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.1
 */
@Data
@NoArgsConstructor
public class CreditCardRequest {

  @NotBlank(message = "El ID del cliente no puede estar vacío.") // ID del cliente dueño de la tarjeta
  @Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}",
      message = "El ID del cliente debe ser un UUID válido.")
  private String customerId;

  @NotBlank(message = "El tipo de tarjeta no puede estar vacío.") // Tipo de tarjeta: PERSONAL o ENTERPRISE
  @Pattern(regexp = "PERSONAL|ENTERPRISE",
      message = "El tipo de tarjeta debe ser PERSONAL o ENTERPRISE.")
  private CardType cardType;

  @NotNull(message = "El límite de crédito no puede ser nulo.") // Límite total de crédito de la tarjeta
  @DecimalMin(value = "0.01", inclusive = true,
      message = "El límite de crédito debe ser mayor a 0.")
  private BigDecimal creditLimit;

  @DecimalMin(value = "0.0", inclusive = true,
      message = "El límite disponible debe ser mayor o igual a 0.") // Límite disponible, opcional
  private BigDecimal availableLimit;

  @DecimalMin(value = "0.0", inclusive = true,
      message = "El balance inicial debe ser mayor o igual a 0.") // Balance inicial de la tarjeta
  @Digits(integer = 18, fraction = 2,
      message = "El balance debe tener como máximo 18 dígitos enteros y 2 decimales.")
  private BigDecimal balance;

  @FutureOrPresent(message = "La fecha de corte debe ser una fecha futura o actual.") // Fecha de corte
  private LocalDate cutoffDate;
}
