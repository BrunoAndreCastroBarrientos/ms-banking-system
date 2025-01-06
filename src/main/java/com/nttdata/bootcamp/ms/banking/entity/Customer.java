package com.nttdata.bootcamp.ms.banking.entity;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.CustomerProfile;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import lombok.*;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.CustomerType;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * Entidad cliente
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "customers")
public class Customer {

  @Id
  private String id;                  // Identificador único del cliente

  private CustomerType customerType;  // Tipo de cliente (PERSONAL/ENTERPRISE)

  private CustomerProfile subType;    // Subtipo de cliente (STANDARD, VIP, PYME)

  private String firstName;           // Nombre del cliente (solo aplica a Personal)
  private String lastName;            // Apellidos del cliente (solo aplica a Personal)

  private String businessName;        // Razón social (solo aplica a Empresarial)
  private String ruc;                // Número de registro de contribuyente (Empresarial)

  private String contactInfo;         // Información de contacto (teléfono, email, etc.)

  private LocalDateTime creationDate; // Fecha de alta del cliente

  private RecordStatus status;        // Estado del cliente (ACTIVE, INACTIVE, etc.)
}