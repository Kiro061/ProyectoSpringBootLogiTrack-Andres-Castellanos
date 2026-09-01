package com.example.ProyectoSpringAndresCastellanos.Dto.Response;

public record BodegaResponse (
        Long id,
        String nombre,
        String ubicacion,
        Integer capacidad,
        String encargado
) {
}
