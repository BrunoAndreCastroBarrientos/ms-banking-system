package com.nttdata.bootcamp.ms.banking.model.response;

import com.nttdata.bootcamp.ms.banking.entity.Client;
import com.nttdata.bootcamp.ms.banking.model.AccountType;
import com.nttdata.bootcamp.ms.banking.model.ClientType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientResponse {
    private String id;
    private String firstName;
    private String lastName;
    private ClientType type;
    private String email;
    private String phone;

    public static ClientResponse fromEntity(Client client) {
        return ClientResponse.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .type(client.getType())
                .email(client.getEmail())
                .phone(client.getPhone())
                .build();
    }
}
