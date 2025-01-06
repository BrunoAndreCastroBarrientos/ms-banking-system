package com.nttdata.bootcamp.ms.banking.dto.response;

import com.nttdata.bootcamp.ms.banking.dto.AccountInfo;
import com.nttdata.bootcamp.ms.banking.dto.CardInfo;
import com.nttdata.bootcamp.ms.banking.dto.CreditInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

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
public class ConsolidatedSummaryResponse {
  private String customerId;
  private BigDecimal totalAccountsBalance; // Suma de saldos en cuentas
  private BigDecimal totalCreditDebt;      // Suma de saldos pendientes en créditos
  private BigDecimal totalCardBalance;     // Suma de saldos utilizados en tarjetas de crédito
  private boolean hasOverdueDebt;          // Indica si hay deuda vencida

  // Opcional: Detalle por cada cuenta, crédito, tarjeta
  private List<AccountInfo> accounts;
  private List<CreditInfo> credits;
  private List<CardInfo> cards;
}

