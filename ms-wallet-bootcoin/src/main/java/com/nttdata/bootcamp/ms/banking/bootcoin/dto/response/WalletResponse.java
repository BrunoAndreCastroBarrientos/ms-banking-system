package com.nttdata.bootcamp.ms.banking.bootcoin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Response DTO para la cuenta bancaria.
 * Este DTO se utiliza para enviar los datos de la cuenta bancaria como respuesta.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponse implements Serializable {
  private Long id; // Identificador único de la billetera
  private String identificationNumber; // Número de identificación
  private String phoneNumber; // Número de teléfono
  private String email; // Correo electrónico
  private BigDecimal balance; // Saldo
}

