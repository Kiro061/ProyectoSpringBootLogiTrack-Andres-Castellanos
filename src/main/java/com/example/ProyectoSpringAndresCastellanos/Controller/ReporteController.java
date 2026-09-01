package com.example.ProyectoSpringAndresCastellanos.Controller;

import com.example.ProyectoSpringAndresCastellanos.Dto.Response.ReporteResponse;
import com.example.ProyectoSpringAndresCastellanos.Service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "Consulta de reportes")
public class ReporteController {

    private final ReporteService reporteService;

    @Operation(
            summary = "Resumen general",
            description = "Genera un resumen general de los movimientos"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente"),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/resumen")
    public ResponseEntity<ReporteResponse> resumen() {

        return ResponseEntity.ok(
                reporteService.generarResumen()
        );
    }
}