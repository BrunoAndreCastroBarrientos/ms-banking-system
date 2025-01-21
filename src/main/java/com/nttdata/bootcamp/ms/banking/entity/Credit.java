package com.nttdata.bootcamp.ms.banking.entity;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.CreditType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

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
@Document(collection = "credits")
public class Credit {
  @Id
  private String id;                      // Identificador único del crédito
  private String customerId;              // ID del cliente titular del crédito
  private CreditType creditType;          // Tipo de crédito (PERSONAL, ENTERPRISE)
  private BigDecimal amount;              // Monto principal del crédito
  private BigDecimal debt;                // Deuda pendiente (este es el saldo actual después de pagos)
  private BigDecimal interestRate;        // Tasa de interés aplicada
  private LocalDate dueDate;              // Fecha de vencimiento
  private RecordStatus status;            // Estado del crédito (ACTIVE, CANCELLED, etc.)
}