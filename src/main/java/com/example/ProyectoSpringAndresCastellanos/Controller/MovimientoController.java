package com.example.ProyectoSpringAndresCastellanos.Controller;

import com.example.ProyectoSpringAndresCastellanos.Dto.Request.MovimientoRequest;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.MovimientoResponse;
import com.example.ProyectoSpringAndresCastellanos.Service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @PostMapping
    public ResponseEntity<MovimientoResponse> registrar(@Valid@RequestBody MovimientoRequest request){
        return ResponseEntity.ok(movimientoService.registrar(request));
    }

    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> obtenerTodos(){
        return ResponseEntity.ok(movimientoService.obtenerTodos());
    }

    // Requisito 6: movimientos por rango de fechas.
    // Ej: GET /movimientos/rango-fechas?desde=2026-01-01T00:00:00&hasta=2026-12-31T23:59:59
    @GetMapping("/rango-fechas")
    public ResponseEntity<List<MovimientoResponse>> porRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta){
        return ResponseEntity.ok(movimientoService.obtenerPorRangoFechas(desde, hasta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponse> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok(movimientoService.obtenerPorId(id));
    }
}