package com.nttdata.bootcamp.ms.banking.product.dto.request;

import com.nttdata.bootcamp.ms.banking.product.dto.enumeration.CardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Clase Request para crear o actualizar una tarjeta de débito.
 * Incluye validaciones para asegurar la integridad de los datos.
 *
 * @author Bruno Andre
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class DebitCardRequest {

  @NotBlank(message = "Customer ID is mandatory")
  @Pattern(regexp = "^[A-Za-z0-9]{24}$", message = "Customer ID must be a 24-character alphanumeric string")
  private String customerId; // ID del cliente

  @NotBlank(message = "Type is mandatory")
  @Pattern(regexp = "^DEBIT$", message = "Type must be 'DEBIT'")
  private String type; // "DEBIT"

  private CardType cardType; // "PERSONAL" o "ENTERPRISE" (opcional si aplica)

  @NotNull(message = "Associated accounts are mandatory")
  @Size(min = 1, message = "At least one associated account is required")
  private List<@Pattern(regexp = "^[A-Za-z0-9]{24}$", message = "Each account ID must be a 24-character alphanumeric string") String> associatedAccounts; // IDs de cuentas bancarias del cliente

  //@NotNull(message = "Status is mandatory")
  //private RecordStatus status; // ACTIVE, BLOCKED, etc.

}
