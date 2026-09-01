package com.example.ProyectoSpringAndresCastellanos.Controller;

import com.example.ProyectoSpringAndresCastellanos.Dto.Request.ProductoRequest;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.ProductoResponse;
import com.example.ProyectoSpringAndresCastellanos.Service.ProductoService;
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
@RequestMapping("/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestión de productos")
public class ProductoController {

    private final ProductoService productoService;

    @Operation(
            summary = "Crear producto",
            description = "Requiere rol ADMIN"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Producto creado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Datos no válidos / Body mal estructurado"),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponse> crear(
            @Valid @RequestBody ProductoRequest request) {

        return ResponseEntity.ok(
                productoService.crear(request)
        );
    }

    @Operation(
            summary = "Listar productos",
            description = "Devuelve todos los productos registrados"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Productos listados exitosamente"),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping
    public ResponseEntity<List<ProductoResponse>> obtenerTodos() {

        return ResponseEntity.ok(
                productoService.obtenerTodos()
        );
    }

    @Operation(
            summary = "Listar productos con stock bajo",
            description = "Devuelve los productos con stock menor al umbral indicado"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Productos con stock bajo listados exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Umbral no válido"),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<ProductoResponse>> stockBajo(
            @RequestParam(defaultValue = "10") Integer umbral) {

        return ResponseEntity.ok(
                productoService.obtenerConStockBajo(umbral)
        );
    }

    @Operation(
            summary = "Obtener producto por ID",
            description = "No requiere rol ADMIN"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productoService.obtenerPorId(id)
        );
    }

    @Operation(
            summary = "Actualizar producto",
            description = "Requiere rol ADMIN"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Datos no válidos / Body mal estructurado"),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request) {

        return ResponseEntity.ok(
                productoService.actualizar(id, request)
        );
    }

    @Operation(
            summary = "Eliminar producto",
            description = "Requiere rol ADMIN"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        productoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}