package com.nttdata.bootcamp.ms.banking.entity;

import com.nttdata.bootcamp.ms.banking.model.ClientType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private ClientType type;
    private String email;
    private String phone;
}



