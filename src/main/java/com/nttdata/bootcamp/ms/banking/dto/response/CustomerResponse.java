package com.nttdata.bootcamp.ms.banking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO para el cliente.
 * Este DTO se utiliza para enviar los datos del cliente como respuesta.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
  private String id;            // ID autogenerado
  private String customerType;
  private String subType;
  private String firstName;
  private String lastName;
  private String businessName;
  private String ruc;
  private String contactInfo;
  private LocalDateTime creationDate;
  private String status;        // "ACTIVE", "INACTIVE", "BLOCKED", etc.
}
