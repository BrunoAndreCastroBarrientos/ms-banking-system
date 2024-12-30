package com.nttdata.bootcamp.ms.banking.model.request;

import com.nttdata.bootcamp.ms.banking.entity.Account;
import com.nttdata.bootcamp.ms.banking.model.AccountType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Request DTO para crear o actualizar una cuenta bancaria.
 * Contiene las validaciones necesarias para garantizar la integridad de los datos.
 *
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    @NotBlank(message = "El número de cuenta no puede estar vacío.")
    @Size(min = 10, max = 20, message = "El número de cuenta debe tener entre 10 y 20 caracteres.")
    private String accountNumber; // Número de cuenta bancaria.

    @NotNull(message = "El tipo de cuenta no puede ser nulo.")
    private AccountType type; // Tipo de cuenta (Ahorro, Corriente, Plazo Fijo).

    @NotBlank(message = "El ID del cliente no puede estar vacío.")
    private String clientId; // Identificador del cliente propietario de la cuenta.

    @NotNull(message = "El saldo inicial no puede ser nulo.")
    @Positive(message = "El saldo debe ser mayor a cero.")
    private BigDecimal balance; // Saldo inicial de la cuenta bancaria.

    @Positive(message = "La comisión de mantenimiento debe ser mayor a cero.")
    private BigDecimal maintenanceFee; // Comisión mensual por mantenimiento de la cuenta (si aplica).

    @Positive(message = "El límite de movimientos debe ser mayor a cero.")
    private Integer movementLimit; // Límite de movimientos mensuales (si aplica).

    @NotNull(message = "La fecha de creación no puede ser nula.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt; // Fecha y hora en que se creó la cuenta bancaria.

    @NotNull(message = "La fecha de actualización no puede ser nula.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt; // Fecha y hora en que se actualizó por última vez la cuenta bancaria.

    /**
     * Convierte este DTO a una entidad Account.
     *
     * @return Una nueva instancia de Account, mapeada desde este DTO.
     */
    public Account toEntity() {
        return Account.builder()
                .accountNumber(this.accountNumber)
                .type(this.type)
                .clientId(this.clientId)
                .balance(this.balance)
                .maintenanceFee(this.maintenanceFee)
                .movementLimit(this.movementLimit)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
