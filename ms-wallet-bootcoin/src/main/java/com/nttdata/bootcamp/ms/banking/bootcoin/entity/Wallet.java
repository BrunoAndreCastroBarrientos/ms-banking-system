package com.nttdata.bootcamp.ms.banking.bootcoin.entity;

import com.nttdata.bootcamp.ms.banking.bootcoin.dto.enumeration.RecordStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;


/**
 * Entidad cuenta bancaria
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "wallets")
public class Wallet {
  @Id
  private Long id;
  private String identificationNumber;
  private String phoneNumber;
  private String email;
  private BigDecimal balance;
  private RecordStatus status;
}