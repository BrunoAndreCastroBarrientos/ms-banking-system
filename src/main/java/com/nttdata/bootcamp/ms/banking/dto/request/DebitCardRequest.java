package com.nttdata.bootcamp.ms.banking.dto.request;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.CardType;
import jakarta.validation.constraints.*;
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

  @NotBlank(message = "El ID del cliente no puede estar vacío.") // ID del cliente al que pertenece la tarjeta
  @Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}",
      message = "El ID del cliente debe ser un UUID válido.")
  private String customerId;

  @NotNull(message = "El tipo de tarjeta no puede ser nulo.") // Tipo de tarjeta: PERSONAL o ENTERPRISE (si aplica)
  private CardType cardType;

  @NotNull(message = "La lista de cuentas asociadas no puede ser nula.") // Cuentas asociadas a la tarjeta
  @Size(min = 1, message = "Debe asociarse al menos una cuenta a la tarjeta de débito.")
  private List<
      @Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}",
          message = "El ID de la cuenta debe ser un UUID válido.")
          String> associatedAccounts;

  @NotBlank(message = "El ID de la cuenta principal no puede estar vacío.") // ID de la cuenta principal
  @Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}",
      message = "El ID de la cuenta principal debe ser un UUID válido.")
  private String primaryAccount;
}
