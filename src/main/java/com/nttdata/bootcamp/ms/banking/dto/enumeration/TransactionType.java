package com.nttdata.bootcamp.ms.banking.dto.enumeration;

/**
 * Tipos de créditos disponibles
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
public enum TransactionType {
  DEPOSIT,            // Depósito
  WITHDRAWAL,         // Retiro
  TRANSFER,           // Transferencia
  CREDIT_PAYMENT,     // Pago de crédito
  CREDIT_CARD_PAYMENT // Pago de tarjeta de crédito
}