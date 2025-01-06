package com.nttdata.bootcamp.ms.banking.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Clase Request para crear o actualizar un crédito.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class DebitCardRequest {
  private String customerId;
  private String cardType;              // PERSONAL o ENTERPRISE (si aplica)
  private List<String> associatedAccounts;
  private String primaryAccount;           // ID de la cuenta principal
}
