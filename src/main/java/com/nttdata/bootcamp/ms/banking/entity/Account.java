package com.nttdata.bootcamp.ms.banking.entity;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.AccountType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


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

  private Integer transactionsAllowed;    // Número máximo de transacciones sin comisión

  private BigDecimal maintenanceFee;      // Comisión de mantenimiento (puede ser 0)

  private LocalDateTime cutoffDate; // Para cuentas de plazo fijo u otras configuraciones

}