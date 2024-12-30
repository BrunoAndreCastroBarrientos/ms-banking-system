package com.nttdata.bootcamp.ms.banking.model;

/**
 * Estados posibles para los créditos
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
public enum CreditStatus {
    APPROVED,    // Crédito aprobado y activo, se encuentra disponible para el cliente.
    PENDING,     // Crédito en proceso de evaluación, aún no ha sido aprobado ni denegado.
    CLOSED       // Crédito finalizado, cancelado o pagado por completo.
}







