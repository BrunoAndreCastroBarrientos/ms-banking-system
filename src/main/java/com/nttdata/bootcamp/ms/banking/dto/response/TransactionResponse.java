package com.nttdata.bootcamp.ms.banking.dto.response;

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
public class TransactionResponse {
  private String id;
  private String transactionType;
  private String originAccountId;
  private String destinationAccountId;
  private String creditId;
  private String creditCardId;
  private String debitCardId;
  private BigDecimal amount;
  private LocalDateTime transactionDate;
  private BigDecimal commission;
}
