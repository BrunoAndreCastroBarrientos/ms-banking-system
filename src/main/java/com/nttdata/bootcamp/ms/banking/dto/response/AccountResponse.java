package com.nttdata.bootcamp.ms.banking.dto.response;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.AccountType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class AccountResponse {
  private String id; // Identificador único de la cuenta
  private String customerId; // ID del cliente propietario de la cuenta
  private AccountType accountType; // Tipo de cuenta (SAVINGS, CHECKING, TIME_DEPOSIT)
  private BigDecimal balance; // Saldo actual de la cuenta
  private String currency; // Moneda de la cuenta (ej. USD, EUR, etc.)
  private LocalDateTime openDate; // Fecha de apertura de la cuenta
  private RecordStatus status; // Estado de la cuenta (ACTIVE, CLOSED, etc.)
}

