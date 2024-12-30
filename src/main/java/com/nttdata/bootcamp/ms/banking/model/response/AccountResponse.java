package com.nttdata.bootcamp.ms.banking.model.response;

import com.nttdata.bootcamp.ms.banking.entity.Account;
import com.nttdata.bootcamp.ms.banking.model.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class AccountResponse {

    private String id; // Identificador único de la cuenta bancaria.
    private String accountNumber; // Número de cuenta bancaria.
    private AccountType type; // Tipo de cuenta (Ahorro, Corriente, Plazo Fijo).
    private String clientId; // Identificador del cliente propietario de la cuenta.
    private BigDecimal balance; // Saldo actual de la cuenta bancaria.
    private BigDecimal maintenanceFee; // Comisión mensual por mantenimiento de la cuenta (si aplica).
    private Integer movementLimit; // Límite de movimientos mensuales (si aplica).
    private LocalDateTime createdAt; // Fecha y hora en que se creó la cuenta bancaria.
    private LocalDateTime updatedAt; // Fecha y hora en que se actualizó por última vez la cuenta bancaria.

    /**
     * Convierte una entidad Account a un DTO AccountResponse.
     *
     * @param account La entidad Account.
     * @return Un nuevo objeto AccountResponse con los datos de la entidad Account.
     */
    public static AccountResponse fromEntity(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .type(account.getType())
                .clientId(account.getClientId())
                .balance(account.getBalance())
                .maintenanceFee(account.getMaintenanceFee())
                .movementLimit(account.getMovementLimit())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}

