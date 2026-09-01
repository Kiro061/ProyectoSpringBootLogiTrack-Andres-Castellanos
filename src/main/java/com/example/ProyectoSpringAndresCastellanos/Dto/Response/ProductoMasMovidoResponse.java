package com.example.ProyectoSpringAndresCastellanos.Dto.Response;

public record ProductoMasMovidoResponse(
        Long productoId,
        String nombreProducto,
        Integer cantidadMovida
) {
}