package com.example.ProyectoSpringAndresCastellanos.Exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String mensaje,
        String errorCode
) {
}
