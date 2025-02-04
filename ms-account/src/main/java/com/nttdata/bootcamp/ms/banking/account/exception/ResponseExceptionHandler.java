package com.nttdata.bootcamp.ms.banking.account.exception;

import com.nttdata.bootcamp.ms.banking.account.dto.response.ApiExceptionResponse;
import com.nttdata.bootcamp.ms.banking.account.utility.ConstantUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

/**
 * Manejador global de excepciones
 * Procesa las excepciones de forma reactiva
 * Mapear errores a nivel de usuario y logs para developers
 */
@RestControllerAdvice
public class ResponseExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(ResponseExceptionHandler.class);

  /**
   * Maneja errores de validación de datos
   */
  @ExceptionHandler(WebExchangeBindException.class)
  public Mono<ResponseEntity<ApiExceptionResponse>> manejarErrorValidacion(
      WebExchangeBindException ex) {
    return Mono.just(ResponseEntity.ok()
        .body(ApiExceptionResponse.builder()
            .code(ConstantUtil.ERROR_CODE)
            .message(ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse("Error de validación"))
            .build()));
  }

  /**
   * Elimina o comenta el manejador para ApiValidateException
   */
    @ExceptionHandler(ApiValidateException.class)
    public Mono<ResponseEntity<ApiExceptionResponse>> manejarExcepcionValidacion(
            ApiValidateException ex) {
        return construirRespuestaError(ex.getLocalizedMessage());
    }

  /**
   * Maneja errores específicos de la API
   */
  @ExceptionHandler(ApiErrorException.class)
  public Mono<ResponseEntity<ApiExceptionResponse>> manejarErrorApi(
      ApiErrorException ex) {
    log.error("Error en la API", ex);
    return construirRespuestaError(ConstantUtil.ERROR_MESSAGE);
  }

  /**
   * Maneja cualquier otra excepción no controlada
   */
  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<ApiExceptionResponse>> manejarExcepcion(Exception ex) {
    log.error("Error inesperado", ex);
    return construirRespuestaError(ConstantUtil.ERROR_MESSAGE);
  }

  /**
   * Método auxiliar para construir la respuesta de error
   */
  private Mono<ResponseEntity<ApiExceptionResponse>> construirRespuestaError(String mensaje) {
    return Mono.just(ResponseEntity.ok()
        .body(ApiExceptionResponse.builder()
            .code(ConstantUtil.ERROR_CODE)
            .message(mensaje)
            .build()));
  }
}
