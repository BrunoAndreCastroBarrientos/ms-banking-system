package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.dto.request.CreditCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.ConsolidatedSummaryResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditCardResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReportService {
  /**
   * Retorna un resumen consolidado de todos los productos de un cliente:
   * - Saldos en cuentas bancarias
   * - Créditos y deudas pendientes
   * - Tarjetas (crédito/débito)
   * - Indicador de deuda vencida, si aplica
   */
  Mono<ConsolidatedSummaryResponse> getConsolidatedSummary(String customerId);

  /**
   * Ejemplo: Cálculo de saldo promedio diario en cuentas para un rango de fechas.
   */
  Mono<Double> getAverageDailyBalance(String customerId, String accountId, String fromDate, String toDate);

  /**
   * Reporte de últimos 10 movimientos (puede ser de una cuenta o tarjeta).
   */
  Mono<String> getLastTenMovements(String customerId, String productId, String productType);
}

