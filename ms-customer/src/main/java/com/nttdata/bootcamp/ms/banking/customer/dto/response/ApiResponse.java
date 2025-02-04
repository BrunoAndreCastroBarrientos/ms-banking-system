package com.nttdata.bootcamp.ms.banking.customer.dto.response;

import lombok.*;

/**
 * Representa una respuesta estandarizada de una API.
 *
 * @param <T> Tipo de datos que se incluirán en el campo {@code data}.
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
  private String code;//Código que describe el estado o resultado de la respuesta.
  private String message;// Mensaje que proporciona información adicional sobre el resultado de la respuesta.
  private T data;// Datos adicionales devueltos por la API, cuyo tipo es genérico.
}
