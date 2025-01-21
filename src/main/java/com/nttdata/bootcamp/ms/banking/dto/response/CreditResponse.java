package com.nttdata.bootcamp.ms.banking.dto.response;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.CreditType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Clase Response que representa la respuesta de un crédito.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class CreditResponse {
  private String id; // Identificador único del crédito
  private String customerId; // ID del cliente titular del crédito
  private CreditType creditType; // Tipo de crédito (PERSONAL, ENTERPRISE)
  private BigDecimal amount; // Monto principal del crédito
  private BigDecimal debt; // Deuda pendiente (este es el saldo actual después de pagos)
  private BigDecimal interestRate; // Tasa de interés aplicada
  private LocalDate dueDate; // Fecha de vencimiento
  private RecordStatus status; // Estado del crédito (ACTIVE, CANCELLED, etc.)
}
