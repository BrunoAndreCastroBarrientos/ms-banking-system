package com.nttdata.bootcamp.ms.banking.dto.request;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * Clase Request para crear o actualizar una transacción.
 * Contiene validaciones para garantizar la consistencia de los datos.
 *
 * @author Bruno Andre
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class BootCoinRequest {

  @NotBlank(message = "Phone number is mandatory")
  @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number must be between 7 to 15 digits and may start with +")
  private String phoneNumber; // Información de contacto (teléfono)

  @NotNull(message = "Amount is mandatory")
  @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
  private BigDecimal amount; // Monto principal del crédito
}
