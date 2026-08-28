package com.example.ProyectoSpringAndresCastellanos.Dto.Response;

public record BodegaResponse (
        Integer id,
        String nombre,
        String ubicacion,
        Integer capacidad,
        String encargado
) {
}
