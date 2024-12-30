package com.nttdata.bootcamp.ms.banking.model;

/**
 * Tipos de créditos disponibles
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
public enum CreditType {
    PERSONAL,    // Crédito personal, limitado a un solo crédito por cliente.
    BUSINESS     // Crédito empresarial, permite múltiples créditos por empresa.
}