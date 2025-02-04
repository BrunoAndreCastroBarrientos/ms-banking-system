package com.nttdata.bootcamp.ms.banking.transaction.dto.request;

import com.nttdata.bootcamp.ms.banking.transaction.dto.enumeration.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
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
public class TransactionRequest {

  @NotNull(message = "Transaction type is mandatory")
  private TransactionType transactionType; // Tipo de transacción (DEPOSIT, WITHDRAWAL, TRANSFER, CREDIT_PAYMENT,CREDIT_CARD_PAYMENT.)

  @Pattern(regexp = "^[A-Za-z0-9]{24}$", message = "Origin Account ID must be a 24-character alphanumeric string")
  private String originAccountId; // Cuenta de origen (puede ser null)

  @Pattern(regexp = "^[A-Za-z0-9]{24}$", message = "Destination Account ID must be a 24-character alphanumeric string")
  private String destinationAccountId; // Cuenta de destino (puede ser null)

  @Pattern(regexp = "^[A-Za-z0-9]{24}$", message = "Credit ID must be a 24-character alphanumeric string")
  private String creditId; // Crédito al que se aplica el pago (si corresponde)

  @Pattern(regexp = "^[A-Za-z0-9]{24}$", message = "Credit Card ID must be a 24-character alphanumeric string")
  private String creditCardId; // Tarjeta de crédito a la que se aplica el pago (si corresponde)

  @Pattern(regexp = "^[A-Za-z0-9]{24}$", message = "Debit Card ID must be a 24-character alphanumeric string")
  private String debitCardId; // Opcional, si deseas guardar la tarjeta de débito

  @NotNull(message = "Amount is mandatory")
  @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
  private BigDecimal amount; // Monto de la transacción

  @NotNull(message = "Transaction date is mandatory")
  @FutureOrPresent(message = "Transaction date must be in the future or present")
  private LocalDateTime transactionDate; // Fecha y hora de la transacción
}
