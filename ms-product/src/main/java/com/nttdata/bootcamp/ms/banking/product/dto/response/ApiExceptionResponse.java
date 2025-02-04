package com.nttdata.bootcamp.ms.banking.product.dto.response;

import lombok.*;

/**
 * Representa una respuesta de excepción de la API.
 * Esta clase se utiliza para manejar los errores que ocurren durante el procesamiento de una solicitud.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiExceptionResponse {
  private String code;//Código que describe el tipo de error o excepción.
  private String message;//Mensaje que describe el error o proporciona detalles adicionales sobre la excepción.
}