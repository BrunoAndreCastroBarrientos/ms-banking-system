package com.nttdata.bootcamp.ms.banking.product.dto.request;

import com.nttdata.bootcamp.ms.banking.product.dto.enumeration.CardType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Clase Request para crear o actualizar un crédito.
 * Este DTO se utiliza para enviar los datos de la tarjeta de crédito, con los detalles
 * correspondientes del cliente, tipo de tarjeta, límites de crédito y balance.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.1
 */
@Data
@NoArgsConstructor
public class CreditCardRequest {

  @NotBlank(message = "Card number is mandatory")
  @Pattern(regexp = "^[0-9]{16}$", message = "Card number must be a 16-digit number")
  private String cardNumber; // Número de la tarjeta

  @NotBlank(message = "Customer ID is mandatory")
  @Pattern(regexp = "^[A-Za-z0-9]{24}$", message = "Customer ID must be a 24-character alphanumeric string")
  private String customerId; // ID del cliente

  @NotBlank(message = "Type is mandatory")
  @Pattern(regexp = "^CREDIT$", message = "Type must be 'CREDIT'")
  private String type; // "CREDIT"

  @NotNull(message = "Card type is mandatory")
  private CardType cardType; // "PERSONAL" o "ENTERPRISE"

  @NotNull(message = "Credit limit is mandatory")
  @DecimalMin(value = "0.0", inclusive = false, message = "Credit limit must be greater than zero")
  private BigDecimal creditLimit; // Límite de crédito

  @NotNull(message = "Balance is mandatory")
  @DecimalMin(value = "0.0", inclusive = true, message = "Balance must be non-negative")
  private BigDecimal balance; // Saldo

  @NotNull(message = "Cutoff date is mandatory")
  private LocalDate cutoffDate; // Fecha de corte

  //@NotNull(message = "Status is mandatory")
  //private RecordStatus status; // Estado (ACTIVE, BLOCKED, etc.)
}

