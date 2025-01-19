package com.nttdata.bootcamp.ms.banking.dto.response;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.CreditType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class CardTransactionResponse {
  private String cardId;
  private int transactionCount;
}
