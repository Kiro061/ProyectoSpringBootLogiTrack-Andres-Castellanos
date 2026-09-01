package com.example.ProyectoSpringAndresCastellanos.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String ESQUEMA_JWT = "bearerAuth";

    @Bean
    public OpenAPI logiTrackOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LogiTrack S.A. - API de Gestión de Bodegas")
                        .description("Sistema backend para el control de bodegas, inventario, " +
                                "movimientos y auditoría de LogiTrack S.A. " +
                                "Para usar los endpoints protegidos: haz login en /auth/login, " +
                                "copia el token y pégalo en el botón Authorize (sin escribir 'Bearer').")
                        .version("1.0"))
                // Aplica el candado a TODOS los endpoints por defecto en la UI
                .addSecurityItem(new SecurityRequirement().addList(ESQUEMA_JWT))
                .components(new Components()
                        .addSecuritySchemes(ESQUEMA_JWT, new SecurityScheme()
                                .name(ESQUEMA_JWT)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}