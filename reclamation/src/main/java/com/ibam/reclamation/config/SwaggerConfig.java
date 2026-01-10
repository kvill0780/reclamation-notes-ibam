package com.ibam.reclamation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI reclamationAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Réclamations IBAM")
                        .description("Gestion des demandes de réclamation de notes")
                        .version("1.0"));
    }
}