package com.nttdata.bootcamp.ms.banking.entity;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
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
  private String customerId;
  private String type;         // "CREDIT"
  private String cardType;     // "PERSONAL" o "ENTERPRISE" (según tu enum CardType)
  private BigDecimal creditLimit;
  private BigDecimal availableLimit;
  private BigDecimal balance;
  private LocalDate cutoffDate;
  private RecordStatus status;       // ACTIVE, BLOCKED, etc.
}