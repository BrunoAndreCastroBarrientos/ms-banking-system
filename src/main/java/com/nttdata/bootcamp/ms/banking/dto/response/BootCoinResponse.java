package com.nttdata.bootcamp.ms.banking.dto.response;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.CardType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.TransactionBootCoinType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Clase Response que representa la respuesta de un crédito.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BootCoinResponse {
  private String phoneNumber;

  private BigDecimal amount;

  private TransactionBootCoinType transactionBootCoinType; // "BUY" or "SELL"

  private LocalDateTime transactionDate;
}
