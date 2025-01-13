package com.nttdata.bootcamp.ms.banking.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request DTO para crear o actualizar una cuenta bancaria.
 * Contiene las validaciones necesarias para garantizar la integridad de los datos.
 *
 * @author Bruno Andre
 * @version 1.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletRequest {
  private String documentId;
  private String phoneNumber;
  private String imei;
  private String email;
}
