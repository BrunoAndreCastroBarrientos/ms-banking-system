package com.nttdata.bootcamp.ms.banking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
public class DebitCardResponse {
  private String id;
  private String customerId;
  private String cardType;
  private List<String> associatedAccounts;
  private String primaryAccount;
  private String status; // ACTIVE, BLOCKED, etc.

}

