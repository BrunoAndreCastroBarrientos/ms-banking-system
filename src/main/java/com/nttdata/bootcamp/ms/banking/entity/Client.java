package com.nttdata.bootcamp.ms.banking.entity;

import lombok.*;
import com.nttdata.bootcamp.ms.banking.model.ClientType;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;

/**
 * Entidad cliente
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "clients")
public class Client {

    @Id
    private String id; // Identificador único del cliente.

    private String firstName; // Nombre del cliente.

    private String lastName; // Apellido del cliente.

    private ClientType type; // Tipo de cliente (personal o empresarial).

    private String email; // Correo electrónico del cliente.

    private String phone; // Número de teléfono del cliente.

    @Field("created_at")
    private LocalDateTime createdAt; // Fecha y hora en que se creó el cliente.

    @Field("updated_at")
    private LocalDateTime updatedAt; // Fecha y hora en que se actualizó por última vez el cliente.
}