package com.nttdata.bootcamp.ms.banking.account.entity;

import com.nttdata.bootcamp.ms.banking.account.dto.enumeration.AccountType;
import com.nttdata.bootcamp.ms.banking.account.dto.enumeration.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * Entidad cuenta bancaria
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "accounts")
public class Account {
  @Id
  private String id;                      // Identificador único de la cuenta
  private String customerId;              // ID del cliente propietario de la cuenta
  private AccountType accountType;        // Tipo de cuenta (SAVINGS, CHECKING, TIME_DEPOSIT)
  private BigDecimal balance;             // Saldo actual de la cuenta

  private String currency;                // Moneda de la cuenta (ej. USD, EUR, etc.)

  private LocalDateTime openDate;         // Fecha de apertura de la cuenta

  private RecordStatus status;            // Estado de la cuenta (ACTIVE, CLOSED, etc.)

}