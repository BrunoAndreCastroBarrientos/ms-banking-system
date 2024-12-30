package com.nttdata.bootcamp.ms.banking.model.response;

import com.nttdata.bootcamp.ms.banking.entity.Client;
import com.nttdata.bootcamp.ms.banking.model.ClientType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO para el cliente.
 * Este DTO se utiliza para enviar los datos del cliente como respuesta.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {

    private String id; // Identificador único del cliente.
    private String firstName; // Nombre del cliente.
    private String lastName; // Apellido del cliente.
    private ClientType type; // Tipo de cliente (personal o empresarial).
    private String email; // Correo electrónico del cliente.
    private String phone; // Número de teléfono del cliente.
    private LocalDateTime createdAt; // Fecha y hora en que se creó el cliente.
    private LocalDateTime updatedAt; // Fecha y hora en que se actualizó por última vez el cliente.

    /**
     * Convierte una entidad Client a un DTO ClientResponse.
     *
     * @param client La entidad Client.
     * @return Un nuevo objeto ClientResponse con los datos de la entidad Client.
     */
    public static ClientResponse fromEntity(Client client) {
        return ClientResponse.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .type(client.getType())
                .email(client.getEmail())
                .phone(client.getPhone())
                .createdAt(client.getCreatedAt())
                .updatedAt(client.getUpdatedAt())
                .build();
    }
}
