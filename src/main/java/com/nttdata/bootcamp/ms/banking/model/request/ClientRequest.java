package com.nttdata.bootcamp.ms.banking.model.request;

import com.nttdata.bootcamp.ms.banking.entity.Client;
import com.nttdata.bootcamp.ms.banking.model.ClientType;
import lombok.*;
import jakarta.validation.constraints.*;

/**
 * Request object for creating or updating a Client.
 * Contains validation rules and a method to map to the Client entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequest {

    @NotBlank(message = "First name is required.")
    @Size(max = 50, message = "First name must not exceed 50 characters.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(max = 50, message = "Last name must not exceed 50 characters.")
    private String lastName;

    @NotNull(message = "Client type is required.")
    private ClientType type;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be a valid format.")
    private String email;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number must be valid (7-15 digits, optional '+').")
    private String phone;

    /**
     * Converts this request object to a Client entity.
     *
     * @return a Client entity with the data from this request.
     */
    public Client toEntity() {
        return Client.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .type(this.type)
                .email(this.email)
                .phone(this.phone)
                .build();
    }
}
