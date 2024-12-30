package com.nttdata.bootcamp.ms.banking.model.request;

import com.nttdata.bootcamp.ms.banking.entity.Credit;
import com.nttdata.bootcamp.ms.banking.model.CreditStatus;
import com.nttdata.bootcamp.ms.banking.model.CreditType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Clase Request para crear o actualizar un crédito.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class CreditRequest {

    @NotNull(message = "El identificador del cliente no puede ser nulo.")
    private String clientId;

    @NotNull(message = "El monto del crédito no puede ser nulo.")
    @PositiveOrZero(message = "El monto del crédito debe ser mayor o igual a cero.")
    private BigDecimal amount;

    @NotNull(message = "La tasa de interés no puede ser nula.")
    @PositiveOrZero(message = "La tasa de interés debe ser mayor o igual a cero.")
    private BigDecimal interestRate;

    @NotNull(message = "El estado del crédito no puede ser nulo.")
    private CreditStatus status;

    @NotNull(message = "El tipo de crédito no puede ser nulo.")
    private CreditType creditType;

    @NotNull(message = "El saldo restante no puede ser nulo.")
    @PositiveOrZero(message = "El saldo restante debe ser mayor o igual a cero.")
    private BigDecimal remainingBalance;

    @NotNull(message = "La fecha de creación no puede ser nula.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt; // Fecha y hora en que se creó.

    @NotNull(message = "La fecha de actualización no puede ser nula.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt; // Fecha y hora en que se actualizó por última vez.

    /**
     * Convierte el CreditRequest a una entidad Credit.
     *
     * @return La entidad Credit.
     */
    public Credit toEntity() {
        return Credit.builder()
                .clientId(this.clientId)
                .amount(this.amount)
                .interestRate(this.interestRate)
                .status(this.status)
                .creditType(this.creditType)
                .remainingBalance(this.remainingBalance)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
