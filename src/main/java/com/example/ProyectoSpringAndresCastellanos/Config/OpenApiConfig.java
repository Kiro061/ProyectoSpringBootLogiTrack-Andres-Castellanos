package com.example.ProyectoSpringAndresCastellanos.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI y Swagger para la documentación de la API.
 *
 * Permite definir el nombre, descripción y versión de la API,
 * además de configurar la autenticación mediante tokens JWT.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Nombre utilizado para identificar el esquema de autenticación JWT.
     */
    private static final String ESQUEMA_JWT = "bearerAuth";

    /**
     * Configura la información general de la API y el sistema
     * de autenticación utilizado por Swagger.
     *
     * @return configuración de OpenAPI para la aplicación
     */
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

                // Aplica la autenticación JWT a los endpoints de Swagger.
                .addSecurityItem(new SecurityRequirement().addList(ESQUEMA_JWT))

                // Define el esquema de autenticación mediante Bearer Token.
                .components(new Components()
                        .addSecuritySchemes(ESQUEMA_JWT, new SecurityScheme()
                                .name(ESQUEMA_JWT)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
