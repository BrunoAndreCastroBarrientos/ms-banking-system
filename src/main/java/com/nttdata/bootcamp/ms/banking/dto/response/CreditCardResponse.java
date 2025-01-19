package com.nttdata.bootcamp.ms.banking.dto.response;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.CardType;
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
public class CreditCardResponse {
  private String id; // Identificador único de la tarjeta de crédito
  private String cardNumber; // Número de la tarjeta
  private String customerId; // ID del cliente
  private String type; // "CREDIT"
  private CardType cardType; // "PERSONAL" o "ENTERPRISE"
  private BigDecimal creditLimit; // Límite de crédito
  private BigDecimal balance; // Saldo
  private LocalDate cutoffDate; // Fecha de corte
  private RecordStatus status; // Estado (ACTIVE, BLOCKED, etc.)
}
