package com.nttdata.bootcamp.ms.banking.bootcoin.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
public class WalletRequest {

  @NotBlank(message = "Identification number is mandatory")
  @Pattern(regexp = "^[A-Za-z0-9]{6,20}$", message = "Identification number must be alphanumeric and between 6 to 20 characters")
  private String identificationNumber; // Número de identificación

  @NotBlank(message = "Phone number is mandatory")
  @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number must be between 7 to 15 digits and may start with +")
  private String phoneNumber; // Número de teléfono

  @NotBlank(message = "Email is mandatory")
  @Email(message = "Email should be valid")
  private String email; // Correo electrónico

  @NotNull(message = "Balance is mandatory")
  @DecimalMin(value = "0.0", inclusive = true, message = "Balance must be non-negative")
  private BigDecimal balance; // Saldo
}
