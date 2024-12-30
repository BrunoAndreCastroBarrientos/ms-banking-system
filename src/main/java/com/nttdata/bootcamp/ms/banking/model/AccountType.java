package com.nttdata.bootcamp.ms.banking.model;

/**
 * Tipos de cuentas bancarias disponibles en el sistema.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
public enum AccountType {
    AHORRO,      // Cuenta de ahorro con límite de movimientos mensuales y sin comisión de mantenimiento.
    CORRIENTE,   // Cuenta corriente sin límite de movimientos y con comisión de mantenimiento.
    PLAZO_FIJO   // Cuenta a plazo fijo con movimiento único permitido en un mes específico.
}






