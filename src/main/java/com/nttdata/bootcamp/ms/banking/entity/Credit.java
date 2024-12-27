package com.nttdata.bootcamp.ms.banking.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection = "credits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credit {
    @Id
    private String id;
    private String clientId; // Linked to Client entity
    private double amount;
    private double interestRate;
    private String status; // e.g., "Approved", "Pending", "Closed"
    private String creditType; // Personal or Business
    private double remainingBalance;
}

