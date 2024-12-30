package com.nttdata.bootcamp.ms.banking.model;

/**
 * Tipos de créditos disponibles
 */
public enum CreditType {
    PERSONAL,    // Crédito personal (límite: uno por cliente)
    BUSINESS     // Crédito empresarial (múltiples permitidos)
}