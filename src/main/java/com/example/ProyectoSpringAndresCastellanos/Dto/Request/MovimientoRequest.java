package com.example.ProyectoSpringAndresCastellanos.Dto.Request;

import java.util.Date;

public record MovimientoRequest (
        Date fecha,
        Enum tipo_mocimiento,
        Integer usuario_id,
        Integer bodega_origen_id,
        Integer bodega_destino_id
){

}
