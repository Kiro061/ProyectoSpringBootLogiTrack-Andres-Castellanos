package com.example.ProyectoSpringAndresCastellanos.Repository;

import com.example.ProyectoSpringAndresCastellanos.Model.MovimientoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovimientoDetalleRepository extends JpaRepository<MovimientoDetalle,Long> {

    // JOIN FETCH: trae movimiento, producto y las bodegas del movimiento en la misma consulta
    // (si no, cada d.getMovimiento().getBodegaOrigen() dispara una consulta extra por fila -> N+1).
    @Query("SELECT d FROM MovimientoDetalle d " +
            "JOIN FETCH d.movimiento m " +
            "JOIN FETCH d.producto p " +
            "LEFT JOIN FETCH m.bodegaOrigen " +
            "LEFT JOIN FETCH m.bodegaDestino")
    List<MovimientoDetalle> findAllConDetalleCompleto();
}