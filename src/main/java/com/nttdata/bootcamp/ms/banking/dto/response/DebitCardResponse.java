package com.nttdata.bootcamp.ms.banking.dto.response;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.CardType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
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
  private String id; // Identificador único de la tarjeta de débito
  private String customerId; // ID del cliente
  private String type; // "DEBIT"
  private CardType cardType; // "PERSONAL" o "ENTERPRISE" (opcional si aplica)
  private List<String> associatedAccounts; // IDs de cuentas bancarias del cliente
  private RecordStatus status; // ACTIVE, BLOCKED, etc.
}

