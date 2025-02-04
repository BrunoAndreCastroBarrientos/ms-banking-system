package com.nttdata.bootcamp.ms.banking.transaction.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger para generar documentación de la API.
 * Esta clase configura la integración de Swagger en una aplicación Spring Boot,
 * creando una instancia de {@link GroupedOpenApi} para generar documentación
 * de todas las rutas de la aplicación.
 */
@Configuration
public class SwaggerConfig {

  /**
   * Define la configuración de OpenAPI para un grupo específico.
   * Este bean crea una configuración personalizada de Swagger para un grupo
   * denominado "my-api", y establece que todas las rutas (/**) deben ser documentadas.
   *
   * @return una instancia de {@link GroupedOpenApi} con la configuración definida
   */
  @Bean
  public GroupedOpenApi api() {
    return GroupedOpenApi.builder()
        .group("my-api") // Nombre del grupo de la API para organizar la documentación
        .pathsToMatch("/**") // Define que todas las rutas serán documentadas
        .build();
  }
}





