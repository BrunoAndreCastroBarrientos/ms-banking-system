package com.nttdata.bootcamp.ms.banking.dto.request;

import lombok.*;

/**
 * Request DTO para crear o actualizar un cliente.
 * Contiene las validaciones necesarias para garantizar la integridad de los datos.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {
  private String customerType;  // Ej: "PERSONAL" o "ENTERPRISE"
  private String subType;       // Ej: "STANDARD", "VIP", "PYME"
  private String firstName;
  private String lastName;
  private String businessName;
  private String ruc;
  private String contactInfo;
}
