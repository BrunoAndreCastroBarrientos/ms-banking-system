package com.nttdata.bootcamp.ms.banking.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstantUtil {
  public static final String OK_CODE = "200";
  public static final String OK_MESSAGE = "Operación realizada con éxito.";
  public static final String ERROR_CODE = "400";
  public static final String ERROR_MESSAGE = "Ocurrió un error al procesar la operación.";
  public static final String NOT_FOUND_CODE = "404";
  public static final String NOT_FOUND_MESSAGE = "Not Found.";
  public static final String CREATED_CODE = "201";
  public static final String ACCEPTED_CODE = "202";
  public static final String DELETED_CODE = "204";
}
