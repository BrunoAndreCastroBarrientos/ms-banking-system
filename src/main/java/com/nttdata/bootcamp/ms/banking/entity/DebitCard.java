package com.nttdata.bootcamp.ms.banking.entity;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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
@Document(collection = "cards")
public class DebitCard {
  @Id
  private String id;
  private String customerId;
  private String type;                // "DEBIT"
  private String cardType;            // "PERSONAL" o "ENTERPRISE" (opcional si aplica)
  private List<String> associatedAccounts; // IDs de cuentas bancarias del cliente
  private String primaryAccount;           // ID de la cuenta principal
  private RecordStatus status;        // ACTIVE, BLOCKED, etc.
}