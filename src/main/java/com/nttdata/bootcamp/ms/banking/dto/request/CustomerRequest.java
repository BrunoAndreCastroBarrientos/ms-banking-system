package com.nttdata.bootcamp.ms.banking.dto.request;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.CustomerProfile;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.CustomerType;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Request DTO para crear o actualizar un cliente.
 * Contiene las validaciones necesarias para garantizar la integridad de los datos.
 *
 * @author Bruno Andre
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {

  @NotNull(message = "El tipo de cliente no puede ser nulo.") // Ejemplo: PERSONAL o ENTERPRISE
  private CustomerType customerType;

  @NotNull(message = "El sub-tipo de cliente no puede ser nulo.") // Ejemplo: STANDARD, VIP, PYME
  private CustomerProfile subType;

  @Size(max = 100, message = "El nombre no debe superar los 100 caracteres.") // Nombre del cliente (para PERSONAL)
  @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$",
      message = "El nombre solo puede contener letras y espacios.")
  private String firstName;

  @Size(max = 100, message = "El apellido no debe superar los 100 caracteres.") // Apellido del cliente (para PERSONAL)
  @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$",
      message = "El apellido solo puede contener letras y espacios.")
  private String lastName;

  @Size(max = 150, message = "El nombre comercial no debe superar los 150 caracteres.") // Nombre comercial (para ENTERPRISE)
  private String businessName;

  @Pattern(regexp = "\\d{11}",
      message = "El RUC debe contener exactamente 11 dígitos.") // Registro Único de Contribuyente (solo para ENTERPRISE)
  private String ruc;

  @Size(max = 250, message = "La información de contacto no debe superar los 250 caracteres.") // Información de contacto
  @Email(message = "El formato del correo electrónico es inválido.") // Ejemplo: contacto@empresa.com
  private String contactInfo;
}

