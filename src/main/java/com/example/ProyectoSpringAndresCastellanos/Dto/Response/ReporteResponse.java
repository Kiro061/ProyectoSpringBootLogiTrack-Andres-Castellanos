package com.example.ProyectoSpringAndresCastellanos.Dto.Response;

import java.util.List;

public record ReporteResponse(
        List<StockPorBodegaResponse> stockPorBodega,
        List<ProductoMasMovidoResponse> productosMasMovidos,
        List<CantidadMovRegResponse> cantidadMovReg
) {
}