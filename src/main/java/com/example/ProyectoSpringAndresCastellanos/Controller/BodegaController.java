package com.example.ProyectoSpringAndresCastellanos.Controller;

import com.example.ProyectoSpringAndresCastellanos.Dto.Request.BodegaRequest;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.BodegaResponse;
import com.example.ProyectoSpringAndresCastellanos.Service.BodegaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bodegas")
@RequiredArgsConstructor
public class BodegaController {

    private final BodegaService bodegaService;

    // Crear una bodega
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BodegaResponse> crear(
            @Valid @RequestBody BodegaRequest request) {

        return ResponseEntity.ok(
                bodegaService.crear(request)
        );
    }

    // Obtener todas las bodegas
    @GetMapping
    public ResponseEntity<List<BodegaResponse>> obtenerTodas() {

        return ResponseEntity.ok(
                bodegaService.obtenerTodas()
        );
    }

    // Obtener una bodega por ID
    @GetMapping("/{id}")
    public ResponseEntity<BodegaResponse> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bodegaService.obtenerPorId(id)
        );
    }

    // Actualizar una bodega
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BodegaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody BodegaRequest request) {

        return ResponseEntity.ok(
                bodegaService.actualizar(id, request)
        );
    }

    // Eliminar una bodega
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        bodegaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}