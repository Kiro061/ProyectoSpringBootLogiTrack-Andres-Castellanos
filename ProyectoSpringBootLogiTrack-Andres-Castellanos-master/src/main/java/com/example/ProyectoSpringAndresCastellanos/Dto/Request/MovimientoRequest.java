package com.example.ProyectoSpringAndresCastellanos.Dto.Request;

import com.example.ProyectoSpringAndresCastellanos.Controller.MovimientoDetalleRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public record MovimientoRequest (
        @NotNull(message = "El tipo de movimiento es obligatorio.")
        Enum tipo_mocimiento,
        Long bodega_origen_id,
        Long bodega_destino_id,
        @NotEmpty(message = "Debe incluir al menos un producto en el movimiento.")
        @Valid
        List<MovimientoDetalleRequest> detalles

){

}
