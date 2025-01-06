package com.nttdata.bootcamp.ms.banking.entity;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad crédito
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "transactions")
public class Transaction {

  @Id
  private String id;                          // Identificador único de la transacción

  private TransactionType transactionType;     // Tipo de transacción (DEPOSIT, WITHDRAWAL, etc.)

  private String originAccountId;             // Cuenta de origen (puede ser null)

  private String destinationAccountId;        // Cuenta de destino (puede ser null)

  private String creditId;                    // Crédito al que se aplica el pago (si corresponde)

  private String creditCardId;                // Tarjeta de crédito a la que se aplica el pago (si corresponde)
  private String debitCardId;                 // Opcional, si deseas guardar la tarjeta de débito

  private BigDecimal amount;                  // Monto de la transacción

  private LocalDateTime transactionDate;      // Fecha y hora de la transacción

  private BigDecimal commission;              // Comisión cobrada (si aplica)
}