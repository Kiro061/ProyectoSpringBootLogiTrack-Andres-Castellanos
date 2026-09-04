package com.example.ProyectoSpringAndresCastellanos.Controller;

import com.example.ProyectoSpringAndresCastellanos.Dto.Request.MovimientoRequest;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.MovimientoResponse;
import com.example.ProyectoSpringAndresCastellanos.Service.MovimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Movimientos", description = "Gestión de movimientos de inventario")
public class MovimientoController {

    private final MovimientoService movimientoService;

    @Operation(
            summary = "Registrar un movimiento",
            description = "Registra un movimiento de inventario"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Movimiento registrado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Datos no válidos / Body mal estructurado"),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @PostMapping
    public ResponseEntity<MovimientoResponse> registrar(
            @Valid @RequestBody MovimientoRequest request) {

        return ResponseEntity.ok(
                movimientoService.registrar(request)
        );
    }

    @Operation(
            summary = "Listar movimientos",
            description = "Devuelve todos los movimientos registrados"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Movimientos listados exitosamente"),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> obtenerTodos() {

        return ResponseEntity.ok(
                movimientoService.obtenerTodos()
        );
    }

    @Operation(
            summary = "Listar movimientos por rango de fechas",
            description = "Devuelve los movimientos registrados entre dos fechas"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Movimientos listados exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Fechas no válidas"),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/rango-fechas")
    public ResponseEntity<List<MovimientoResponse>> porRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {

        return ResponseEntity.ok(
                movimientoService.obtenerPorRangoFechas(desde, hasta)
        );
    }

    @Operation(
            summary = "Obtener movimiento por ID",
            description = "No requiere rol ADMIN"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Movimiento encontrado exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Movimiento no encontrado"),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponse> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                movimientoService.obtenerPorId(id)
        );
    }

    @GetMapping("/movimientos/recientes")
    public ResponseEntity<List<MovimientoResponse>> listarRecientes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta){
     return ResponseEntity.ok(
       movimientoService.obtenerUltimos(desde,hasta)
     );
    }

}