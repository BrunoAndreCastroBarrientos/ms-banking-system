package com.nttdata.bootcamp.ms.banking.model.request;

import com.nttdata.bootcamp.ms.banking.entity.Client;
import com.nttdata.bootcamp.ms.banking.model.ClientType;
import lombok.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Request DTO para crear o actualizar un cliente.
 * Contiene las validaciones necesarias para garantizar la integridad de los datos.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequest {

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres.")
    private String firstName; // Nombre del cliente.

    @NotBlank(message = "El apellido no puede estar vacío.")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres.")
    private String lastName; // Apellido del cliente.

    @NotNull(message = "El tipo de cliente no puede ser nulo.")
    private ClientType type; // Tipo de cliente (personal o empresarial).

    @NotBlank(message = "El correo electrónico no puede estar vacío.")
    @Email(message = "El correo electrónico debe ser válido.")
    private String email; // Correo electrónico del cliente.

    @NotBlank(message = "El número de teléfono no puede estar vacío.")
    @Size(min = 10, max = 15, message = "El número de teléfono debe tener entre 10 y 15 caracteres.")
    private String phone; // Número de teléfono del cliente.

    @NotNull(message = "La fecha de creación no puede ser nula.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt; // Fecha y hora en que se creó el cliente.

    @NotNull(message = "La fecha de actualización no puede ser nula.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt; // Fecha y hora en que se actualizó por última vez el cliente.

    /**
     * Convierte este DTO a una entidad Client.
     *
     * @return Una nueva instancia de Client, mapeada desde este DTO.
     */
    public Client toEntity() {
        return Client.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .type(this.type)
                .email(this.email)
                .phone(this.phone)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
