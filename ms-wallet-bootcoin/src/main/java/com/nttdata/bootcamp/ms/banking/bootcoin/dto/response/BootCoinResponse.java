package com.nttdata.bootcamp.ms.banking.bootcoin.dto.response;

import com.nttdata.bootcamp.ms.banking.bootcoin.dto.enumeration.TransactionBootCoinType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class BootCoinResponse implements Serializable {
  private String phoneNumber;

  private BigDecimal amount;

  private TransactionBootCoinType transactionBootCoinType; // "BUY" or "SELL"

  private LocalDateTime transactionDate;
}
