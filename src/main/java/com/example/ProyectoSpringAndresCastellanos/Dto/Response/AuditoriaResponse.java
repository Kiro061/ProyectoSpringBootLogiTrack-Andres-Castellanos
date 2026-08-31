package com.example.ProyectoSpringAndresCastellanos.Dto.Response;

import java.time.LocalDateTime;

public record AuditoriaResponse(
        Long id,
        String tipoOperacion,
        LocalDateTime fechaHora,
        Long usuarioId,
        String usuario,
        String entidadAfectada,
        Long entidadId,
        String valoresAnteriores,
        String valoresNuevos
) {
}