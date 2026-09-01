package com.example.ProyectoSpringAndresCastellanos.Service;

import com.example.ProyectoSpringAndresCastellanos.Dto.Request.MovimientoRequest;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.MovimientoResponse;
import com.example.ProyectoSpringAndresCastellanos.Exception.BusinessRuleException;
import com.example.ProyectoSpringAndresCastellanos.Mapper.MovimientoMapper;
import com.example.ProyectoSpringAndresCastellanos.Model.*;
import com.example.ProyectoSpringAndresCastellanos.Repository.BodegaRepository;
import com.example.ProyectoSpringAndresCastellanos.Repository.MovimientoRepository;
import com.example.ProyectoSpringAndresCastellanos.Repository.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.ProyectoSpringAndresCastellanos.Dto.Request.MovimientoDetalleRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final BodegaRepository bodegaRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoMapper movimientoMapper;
    private final AuditoriaService auditoriaService;


    @Transactional
    public MovimientoResponse registrar(MovimientoRequest request) {

        // 1) Usuario responsable: viene del token, nunca del request
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        // 2) Validar bodegas según el tipo de movimiento
        Bodega bodegaOrigen = null;
        Bodega bodegaDestino = null;

        switch (request.tipoMovimiento()) {
            case ENTRADA -> {
                if (request.bodegaDestinoId() == null) {
                    throw new BusinessRuleException("Una ENTRADA requiere bodega destino");
                }
                bodegaDestino = buscarBodega(request.bodegaDestinoId());
            }
            case SALIDA -> {
                if (request.bodegaOrigenId() == null) {
                    throw new BusinessRuleException("Una SALIDA requiere bodega origen");
                }
                bodegaOrigen = buscarBodega(request.bodegaOrigenId());
            }
            case TRANSFERENCIA -> {
                if (request.bodegaOrigenId() == null || request.bodegaDestinoId() == null) {
                    throw new BusinessRuleException("Una TRANSFERENCIA requiere bodega origen y destino");
                }
                if (request.bodegaOrigenId().equals(request.bodegaDestinoId())) {
                    throw new BusinessRuleException("La bodega origen y destino no pueden ser la misma");
                }
                bodegaOrigen = buscarBodega(request.bodegaOrigenId());
                bodegaDestino = buscarBodega(request.bodegaDestinoId());
            }
        }

        // 3) Crear la cabecera del movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipoMovimiento(request.tipoMovimiento());
        movimiento.setUsuario(usuario);
        movimiento.setBodegaOrigen(bodegaOrigen);
        movimiento.setBodegaDestino(bodegaDestino);

        // 4) Crear los detalles y actualizar stock por cada producto
        List<MovimientoDetalle> detalles = new ArrayList<>();

        for (MovimientoDetalleRequest detalleReq : request.detalles()) {
            Producto producto = productoRepository.findById(detalleReq.productoId())
                    .orElseThrow(() -> new BusinessRuleException(
                            "Producto no encontrado con id: " + detalleReq.productoId()));

            actualizarStock(producto, request.tipoMovimiento(), detalleReq.cantidad());

            MovimientoDetalle detalle = new MovimientoDetalle();
            detalle.setMovimiento(movimiento);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleReq.cantidad());
            detalles.add(detalle);
        }

        movimiento.setDetalles(detalles);

        // 5) Guardar (cascade guarda también los detalles)
        Movimiento guardado = movimientoRepository.save(movimiento);

        auditoriaService.registrar(
                guardado,
                TipoOperacion.INSERT,
                null,
                guardado.getAuditData()
        );

        return movimientoMapper.toResponse(guardado);
    }

    public List<MovimientoResponse> obtenerTodos() {
        return movimientoRepository.findAll()
                .stream()
                .map(movimientoMapper::toResponse)
                .toList();
    }

    public MovimientoResponse obtenerPorId(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Movimiento no encontrado con id: " + id));
        return movimientoMapper.toResponse(movimiento);
    }

    // ---------------------------------------------------
    // Actualiza el stock del producto según el tipo de movimiento
    // ---------------------------------------------------
    private void actualizarStock(Producto producto, TipoMovimiento tipo, Integer cantidad) {
        switch (tipo) {
            case ENTRADA -> producto.setStock(producto.getStock() + cantidad);
            case SALIDA -> {
                if (producto.getStock() < cantidad) {
                    throw new BusinessRuleException(
                            "Stock insuficiente para el producto: " + producto.getNombre());
                }
                producto.setStock(producto.getStock() - cantidad);
            }
            case TRANSFERENCIA -> {
                // El stock global del producto no cambia:
                // se mantiene el mismo total, solo cambia de bodega.
                // (En este diseño el stock es global, no por bodega)
            }
        }
        productoRepository.save(producto);
    }

    private Bodega buscarBodega(Long id) {
        return bodegaRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Bodega no encontrada con id: " + id));
    }
}