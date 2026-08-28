package com.example.ProyectoSpringAndresCastellanos.Dto.Response;

import java.util.Date;

public record MovimientoResponse (
        Integer id,
        Date fecha,
        Integer usuario_id,
        Integer bodega_origen_id,
        Integer bodega_destino_id
){
}
