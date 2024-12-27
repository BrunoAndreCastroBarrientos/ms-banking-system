package com.nttdata.bootcamp.ms.banking.entity;

import com.nttdata.bootcamp.ms.banking.model.AccountType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import reactor.core.publisher.Flux;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "accounts")
public class Account {
    @Id
    private String id; // ID único de la cuenta
    private String accountNumber; // Número de la cuenta
    private AccountType type; // Tipo de cuenta (Ahorro, Corriente, Plazo Fijo)
    private String clientId; // ID del cliente al que pertenece la cuenta
    private Double balance; // Saldo de la cuenta
    private Boolean hasActiveTransactions; // Indicador de si la cuenta tiene transacciones activas
}

