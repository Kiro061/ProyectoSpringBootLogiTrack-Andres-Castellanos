package com.example.ProyectoSpringAndresCastellanos.Dto.Response;

public record StockPorBodegaResponse(
        Long bodegaId,
        String nombreBodega,
        Integer stockTotal
) {
}