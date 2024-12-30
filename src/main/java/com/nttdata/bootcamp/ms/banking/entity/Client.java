package com.nttdata.bootcamp.ms.banking.entity;

import lombok.*;
import com.nttdata.bootcamp.ms.banking.model.ClientType;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * Entidad cliente
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "clients")
public class Client {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private ClientType type;
    private String email;
    private String phone;
    @Field("created_at")
    private LocalDateTime createdAt;
    @Field("updated_at")
    private LocalDateTime updatedAt;
}
