package com.example.ProyectoSpringAndresCastellanos.Dto.Request;

import com.example.ProyectoSpringAndresCastellanos.Model.TipoMovimiento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MovimientoRequest (
        @NotNull(message = "El tipo de movimiento es obligatorio.")
        TipoMovimiento tipoMovimiento,
        Long bodegaOrigenId,
        Long bodegaDestinoId,
        @NotEmpty(message = "Debe incluir al menos un producto en el movimiento.")
        @Valid
        List<MovimientoDetalleRequest> detalles
){
}
