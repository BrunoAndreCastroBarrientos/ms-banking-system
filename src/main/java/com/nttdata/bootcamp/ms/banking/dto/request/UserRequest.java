package com.nttdata.bootcamp.ms.banking.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class UserRequest {
  @Pattern(regexp = "^[a-zA-Z0-9._-]{3,20}$",
      message = "El nombre de usuario debe tener entre 3 y 20 caracteres y solo puede contener letras, números, puntos, guiones bajos o guiones.")
  private String username;

  @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,20}$", message = "La contraseña debe tener entre 8 y 20 caracteres, incluir al menos una letra mayúscula, una minúscula, un número y un carácter especial (@#$%^&+=).")
  private String password;
}
