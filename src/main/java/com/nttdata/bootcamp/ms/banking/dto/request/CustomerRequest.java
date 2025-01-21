package com.nttdata.bootcamp.ms.banking.dto.request;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.CustomerProfile;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.CustomerType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.IdentificationType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

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

  @NotNull(message = "Customer type is mandatory")
  private CustomerType customerType; // Tipo de cliente (PERSONAL/ENTERPRISE)

  @NotNull(message = "SubType is mandatory")
  private CustomerProfile subType; // Subtipo de cliente (STANDARD, VIP, PYME)

  // Solo aplica a Personal
  @Pattern(regexp = "^[A-Za-z]{1,50}$", message = "First name must contain only letters and up to 50 characters")
  private String firstName; // Nombre del cliente (solo aplica a Personal)

  @Pattern(regexp = "^[A-Za-z]{1,50}$", message = "Last name must contain only letters and up to 50 characters")
  private String lastName; // Apellidos del cliente (solo aplica a Personal)

  // Solo aplica a Empresarial
  @Pattern(regexp = "^[A-Za-z0-9 ]{1,100}$", message = "Business name must be alphanumeric and up to 100 characters")
  private String businessName; // Razón social (solo aplica a Empresarial)

  @NotBlank(message = "Identification number is mandatory")
  @Pattern(regexp = "^[A-Za-z0-9]{6,20}$", message = "Identification number must be alphanumeric and between 6 to 20 characters")
  private String identificationNumber; // Numero de identificación

  @NotNull(message = "Identification type is mandatory")
  private IdentificationType identificationType; // DNI, CEX, PASSPORT

  @NotBlank(message = "Email is mandatory")
  @Email(message = "Email should be valid")
  private String email; // Información de contacto (email)

  @NotBlank(message = "Phone number is mandatory")
  @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number must be between 7 to 15 digits and may start with +")
  private String phoneNumber; // Información de contacto (teléfono)

  @NotNull(message = "Creation date is mandatory")
  @FutureOrPresent(message = "Date must be in the future or present")
  private LocalDateTime creationDate; // Fecha de alta del cliente

  //@NotNull(message = "Status is mandatory")
  //private RecordStatus status; // Estado del cliente (ACTIVE, INACTIVE)
}

