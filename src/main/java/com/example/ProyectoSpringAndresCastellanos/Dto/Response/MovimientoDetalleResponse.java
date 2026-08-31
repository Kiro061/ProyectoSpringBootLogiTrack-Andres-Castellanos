package com.example.ProyectoSpringAndresCastellanos.Dto.Response;

public record MovimientoDetalleResponse (
        Long productoId,
        String productoNombre,
        Integer cantidad
){
}
