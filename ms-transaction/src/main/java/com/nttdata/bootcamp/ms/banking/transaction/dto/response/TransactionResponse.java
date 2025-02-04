package com.nttdata.bootcamp.ms.banking.transaction.dto.response;

import com.nttdata.bootcamp.ms.banking.transaction.dto.enumeration.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO para la cuenta bancaria.
 * Este DTO se utiliza para enviar los datos de la cuenta bancaria como respuesta.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse implements Serializable {
  private String id; // Identificador único de la transacción
  private TransactionType transactionType; // Tipo de transacción (DEPOSIT, WITHDRAWAL, etc.)
  private String originAccountId; // Cuenta de origen (puede ser null)
  private String destinationAccountId; // Cuenta de destino (puede ser null)
  private String creditId; // Crédito al que se aplica el pago (si corresponde)
  private String creditCardId; // Tarjeta de crédito a la que se aplica el pago (si corresponde)
  private String debitCardId; // Opcional, si deseas guardar la tarjeta de débito
  private BigDecimal amount; // Monto de la transacción
  private LocalDateTime transactionDate; // Fecha y hora de la transacción
}

