package com.example.ProyectoSpringAndresCastellanos.Controller;

import com.example.ProyectoSpringAndresCastellanos.Dto.Request.BodegaRequest;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.BodegaResponse;
import com.example.ProyectoSpringAndresCastellanos.Service.BodegaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bodegas")
@RequiredArgsConstructor
@Tag(name = "Bodegas", description = "Gestión de bodegas")
public class BodegaController {

    private final BodegaService bodegaService;

    @Operation(
            summary = "Crear una bodega",
            description = "Requiere rol ADMIN"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Bodega creada exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Datos no válidos / Body mal estructurado"),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BodegaResponse> crear(
            @Valid @RequestBody BodegaRequest request) {

        return ResponseEntity.ok(
                bodegaService.crear(request)
        );
    }

    @Operation(
            summary = "Listar bodegas",
            description = "Devuelve todas las bodegas registradas"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Bodegas listadas exitosamente"),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping
    public ResponseEntity<List<BodegaResponse>> obtenerTodas() {

        return ResponseEntity.ok(
                bodegaService.obtenerTodas()
        );
    }

    @Operation(
            summary = "Obtener bodega por ID",
            description = "No requiere rol ADMIN"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Bodega encontrada exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Bodega no encontrada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<BodegaResponse> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bodegaService.obtenerPorId(id)
        );
    }

    @Operation(
            summary = "Actualizar bodega",
            description = "Requiere rol ADMIN"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Bodega actualizada exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Datos no válidos / Body mal estructurado"),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
                    @ApiResponse(responseCode = "404", description = "Bodega no encontrada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BodegaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody BodegaRequest request) {

        return ResponseEntity.ok(
                bodegaService.actualizar(id, request)
        );
    }

    @Operation(
            summary = "Eliminar bodega",
            description = "Requiere rol ADMIN"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Bodega eliminada exitosamente"),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
                    @ApiResponse(responseCode = "404", description = "Bodega no encontrada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        bodegaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}