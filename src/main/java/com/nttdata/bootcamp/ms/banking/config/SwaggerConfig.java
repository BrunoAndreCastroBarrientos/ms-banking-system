package com.nttdata.bootcamp.ms.banking.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("my-api") // Nombre de tu grupo
                .pathsToMatch("/**") // Define las rutas a documentar
                .build();
    }
}





