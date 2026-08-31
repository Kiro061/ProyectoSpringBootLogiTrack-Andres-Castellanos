package com.example.ProyectoSpringAndresCastellanos.Dto.Response;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public record MovimientoResponse (
        Long id,
        LocalDateTime fecha,
        String usuarioResponsable,
        Long bodegaOrigenId,
        Long bodegaDestinoId,
        List<MovimientoDetalleResponse> detalles
){
}
