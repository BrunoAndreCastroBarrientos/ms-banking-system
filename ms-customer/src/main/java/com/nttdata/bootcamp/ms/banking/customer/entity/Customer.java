package com.nttdata.bootcamp.ms.banking.customer.entity;

import com.nttdata.bootcamp.ms.banking.customer.dto.enumeration.CustomerProfile;
import com.nttdata.bootcamp.ms.banking.customer.dto.enumeration.CustomerType;
import com.nttdata.bootcamp.ms.banking.customer.dto.enumeration.IdentificationType;
import com.nttdata.bootcamp.ms.banking.customer.dto.enumeration.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

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
public class Customer{
  private String id;                  // Identificador único del cliente
  private CustomerType customerType;  // Tipo de cliente (PERSONAL/ENTERPRISE)
  private CustomerProfile subType;    // Subtipo de cliente (STANDARD, VIP, PYME)
  private String firstName;           // Nombre del cliente (solo aplica a Personal)
  private String lastName;            // Apellidos del cliente (solo aplica a Personal)
  private String businessName;        // Razón social (solo aplica a Empresarial)
  private String identificationNumber; // Numero de identificación
  private IdentificationType identificationType; // DNI, CEX, PASSPORT
  private String email;         // Información de contacto (teléfono, email, etc.)
  private String phoneNumber;         // Información de contacto (teléfono, email, etc.)
  private LocalDateTime creationDate; // Fecha de alta del cliente
  private RecordStatus status;        // Estado del cliente (ACTIVE, INACTIVE)
}