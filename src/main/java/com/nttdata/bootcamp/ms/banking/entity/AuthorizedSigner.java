package com.nttdata.bootcamp.ms.banking.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entidad crédito
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "authorized_signers")
public class AuthorizedSigner {

  @Id
  private String id;              // Identificador único del firmante autorizado

    private String accountId;       // ID de la cuenta empresarial

    private String signerCustomerId; // ID del cliente (persona física) que funge como firmante

    private String role;            // Rol del firmante (ej. "titular", "autorizado", etc.)

    private boolean canSign;        // Indica si puede firmar transacciones en nombre de la cuenta
}
