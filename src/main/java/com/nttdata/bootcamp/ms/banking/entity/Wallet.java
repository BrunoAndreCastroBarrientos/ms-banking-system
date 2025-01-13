package com.nttdata.bootcamp.ms.banking.entity;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.AccountType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * Entidad cuenta bancaria
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "wallets")
public class Wallet {
  @Id
  private Long id;
  private String documentId;
  private String phoneNumber;
  private String imei;
  private String email;
  private Double balance;
}