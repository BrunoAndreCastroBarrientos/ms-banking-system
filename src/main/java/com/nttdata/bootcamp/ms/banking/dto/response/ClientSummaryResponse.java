  package com.nttdata.bootcamp.ms.banking.dto.response;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

  /**
 * Response DTO para la cuenta bancaria.
 * Este DTO se utiliza para enviar los datos de la cuenta bancaria como respuesta.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientSummaryResponse {
    private String clientId;
    private int totalAccounts;
}

