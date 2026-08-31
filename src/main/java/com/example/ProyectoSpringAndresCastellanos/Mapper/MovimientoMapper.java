package com.example.ProyectoSpringAndresCastellanos.Mapper;

import com.example.ProyectoSpringAndresCastellanos.Dto.Response.MovimientoDetalleResponse;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.MovimientoResponse;
import com.example.ProyectoSpringAndresCastellanos.Model.Movimiento;
import com.example.ProyectoSpringAndresCastellanos.Model.MovimientoDetalle;
import org.springframework.stereotype.Component;

@Component
public class MovimientoMapper {

    public MovimientoResponse toResponse(Movimiento movimiento){
        return new MovimientoResponse(
                movimiento.getId(),
                movimiento.getFecha(),
                movimiento.getTipoMovimiento().name(),
                movimiento.getUsuario().getUsername(),
                movimiento.getBodegaOrigen() !=null ?movimiento.getBodegaOrigen().getId() : null,
                movimiento.getBodegaDestino() !=null ?movimiento.getBodegaDestino().getId() : null,
                movimiento.getDetalles().stream()
                        .map(this::toDetalleResponse)
                        .toList()

        );
    }

    private MovimientoDetalleResponse toDetalleResponse(MovimientoDetalle detalle){
        return new MovimientoDetalleResponse(
          detalle.getProducto().getId(),
          detalle.getProducto().getNombre(),
          detalle.getCantidad()
        );
    }
}
