package com.nttdata.bootcamp.ms.banking.entity;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.TransactionBootCoinType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad cliente
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "bootcoin_transactions")
public class BootCoinTransaction {
  @Id
  private String id;
  private String phoneNumber;
  private BigDecimal amount;
  private TransactionBootCoinType transactionBootCoinType; // "BUY" or "SELL"
  private LocalDateTime transactionDate;
}