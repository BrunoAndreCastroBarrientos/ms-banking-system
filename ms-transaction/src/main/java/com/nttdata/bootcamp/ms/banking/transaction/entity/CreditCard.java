package com.nttdata.bootcamp.ms.banking.transaction.entity;

import com.nttdata.bootcamp.ms.banking.transaction.dto.enumeration.CardType;
import com.nttdata.bootcamp.ms.banking.transaction.dto.enumeration.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "cards")
public class CreditCard {
  @Id
  private String id;
  private String cardNumber;
  private String customerId;
  private String type;         // "CREDIT"
  private CardType cardType;     // "PERSONAL" o "ENTERPRISE"
  private BigDecimal creditLimit;
  private BigDecimal balance;
  private LocalDate cutoffDate;
  private RecordStatus status;       // ACTIVE, BLOCKED, etc.
}