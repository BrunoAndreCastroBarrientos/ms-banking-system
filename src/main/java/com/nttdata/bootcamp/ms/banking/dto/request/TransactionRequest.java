package com.nttdata.bootcamp.ms.banking.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Clase Request para crear o actualizar una transacción.
 * Contiene validaciones para garantizar la consistencia de los datos.
 *
 * @author Bruno Andre
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class TransactionRequest {

  @NotBlank(message = "El tipo de transacción no puede estar vacío.") // Tipo de transacción
  @Pattern(regexp = "DEPOSIT|WITHDRAWAL|TRANSFER|CREDIT_PAYMENT|CREDIT_CARD_PAYMENT",
      message = "El tipo de transacción debe ser uno de los siguientes: DEPOSIT, WITHDRAWAL, TRANSFER, CREDIT_PAYMENT, CREDIT_CARD_PAYMENT.")
  private String transactionType;

  @Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}",
      message = "El ID de la cuenta de origen debe ser un UUID válido.") // ID de la cuenta de origen
  private String originAccountId;

  @Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}",
      message = "El ID de la cuenta de destino debe ser un UUID válido.") // ID de la cuenta de destino
  private String destinationAccountId;

  @Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}",
      message = "El ID del crédito debe ser un UUID válido.") // ID del crédito (si aplica)
  private String creditId;

  @Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}",
      message = "El ID de la tarjeta de crédito debe ser un UUID válido.") // ID de la tarjeta de crédito (si aplica)
  private String creditCardId;

  @Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}",
      message = "El ID de la tarjeta de débito debe ser un UUID válido.") // ID de la tarjeta de débito (si aplica)
  private String debitCardId;

  @NotNull(message = "El monto de la transacción no puede ser nulo.") // Monto de la transacción
  @DecimalMin(value = "0.01", inclusive = true,
      message = "El monto de la transacción debe ser mayor a 0.")
  @Digits(integer = 18, fraction = 2,
      message = "El monto de la transacción debe tener como máximo 18 dígitos enteros y 2 decimales.")
  private BigDecimal amount;
}
