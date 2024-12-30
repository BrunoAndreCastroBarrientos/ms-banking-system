package com.nttdata.bootcamp.ms.banking.entity;

import com.nttdata.bootcamp.ms.banking.model.AccountType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad cuenta bancaria
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "accounts")
public class Account {

    @Id
    private String id; // Identificador único de la cuenta bancaria.

    @Field("account_number")
    private String accountNumber; // Número de cuenta bancaria.

    private AccountType type; // Tipo de cuenta (Ahorro, Corriente, Plazo Fijo).

    @Field("client_id")
    private String clientId; // Identificador del cliente propietario de la cuenta.

    private BigDecimal balance; // Saldo actual de la cuenta bancaria.
    private Integer transactionCount; // Cantidad de Transacciones

    @Field("maintenance_fee")
    private BigDecimal maintenanceFee; // Comisión mensual por mantenimiento de la cuenta (si aplica).

    @Field("movement_limit")
    private Integer movementLimit; // Límite de movimientos mensuales (si aplica, como en las cuentas de ahorro).

    @Field("created_at")
    private LocalDateTime createdAt; // Fecha y hora en que se creó la cuenta bancaria.

    @Field("updated_at")
    private LocalDateTime updatedAt; // Fecha y hora en que se actualizó por última vez la cuenta bancaria.
}