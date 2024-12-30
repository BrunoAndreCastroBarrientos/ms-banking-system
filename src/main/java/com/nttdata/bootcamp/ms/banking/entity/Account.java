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
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "accounts")
public class Account {
    @Id
    private String id;
    @Field("account_number")
    private String accountNumber;
    private AccountType type;
    @Field("client_id")
    private String clientId;
    private BigDecimal balance;
    @Field("maintenance_fee")
    private BigDecimal maintenanceFee;
    @Field("movement_limit")
    private Integer movementLimit;
    @Field("created_at")
    private LocalDateTime createdAt;
    @Field("updated_at")
    private LocalDateTime updatedAt;
}



