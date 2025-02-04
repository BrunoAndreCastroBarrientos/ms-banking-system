package com.nttdata.bootcamp.ms.banking.account.dto.response;

import com.nttdata.bootcamp.ms.banking.account.dto.enumeration.CustomerProfile;
import com.nttdata.bootcamp.ms.banking.account.dto.enumeration.CustomerType;
import com.nttdata.bootcamp.ms.banking.account.dto.enumeration.IdentificationType;
import com.nttdata.bootcamp.ms.banking.account.dto.enumeration.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
public class CustomerResponse implements Serializable {
  private String id; // Identificador único del cliente
  private CustomerType customerType; // Tipo de cliente (PERSONAL/ENTERPRISE)
  private CustomerProfile subType; // Subtipo de cliente (STANDARD, VIP, PYME)
  private String firstName; // Nombre del cliente (solo aplica a Personal)
  private String lastName; // Apellidos del cliente (solo aplica a Personal)
  private String businessName; // Razón social (solo aplica a Empresarial)
  private String identificationNumber; // Numero de identificación
  private IdentificationType identificationType; // DNI, CEX, PASSPORT
  private String email; // Información de contacto (email)
  private String phoneNumber; // Información de contacto (teléfono)
  private LocalDateTime creationDate; // Fecha de alta del cliente
  private RecordStatus status; // Estado del cliente (ACTIVE, INACTIVE)
}
