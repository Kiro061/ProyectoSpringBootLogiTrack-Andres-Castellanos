package com.example.ProyectoSpringAndresCastellanos.Controller;

import com.example.ProyectoSpringAndresCastellanos.Dto.Response.ReporteResponse;
import com.example.ProyectoSpringAndresCastellanos.Service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/resumen")
    public ResponseEntity<ReporteResponse> resumen() {
        return ResponseEntity.ok(reporteService.generarResumen());
    }
}