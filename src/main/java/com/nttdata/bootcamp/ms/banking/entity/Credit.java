package com.nttdata.bootcamp.ms.banking.entity;

import com.nttdata.bootcamp.ms.banking.model.CreditStatus;
import com.nttdata.bootcamp.ms.banking.model.CreditType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad crédito
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "credits")
public class Credit {

    @Id
    private String id; // Identificador único del crédito.

    private String clientId; // Identificador del cliente al que pertenece el crédito.

    private BigDecimal amount; // Monto total del crédito otorgado.

    private BigDecimal interestRate; // Tasa de interés aplicada al crédito.

    private CreditStatus status; // Estado actual del crédito (aprobado, pendiente, cerrado).

    private CreditType creditType; // Tipo de crédito (personal o empresarial).

    private BigDecimal remainingBalance; // Saldo restante del crédito por pagar.

    @Field("created_at")
    private LocalDateTime createdAt; // Fecha y hora en que se creó el crédito.

    @Field("updated_at")
    private LocalDateTime updatedAt; // Fecha y hora en que se actualizó por última vez el crédito.
}