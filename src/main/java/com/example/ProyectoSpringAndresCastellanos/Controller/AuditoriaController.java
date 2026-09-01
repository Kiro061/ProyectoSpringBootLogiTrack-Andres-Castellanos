package com.example.ProyectoSpringAndresCastellanos.Controller;

import com.example.ProyectoSpringAndresCastellanos.Dto.Response.AuditoriaResponse;
import com.example.ProyectoSpringAndresCastellanos.Model.TipoOperacion;
import com.example.ProyectoSpringAndresCastellanos.Service.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auditorias")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Auditoría", description = "Consulta del historial de operaciones")
@ApiResponses(
        value = {
                @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
        }
)
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @Operation(
            summary = "Listar toda la auditoría",
            description = "Devuelve todos los registros de auditoría"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Auditoría listada exitosamente"),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping
    public ResponseEntity<List<AuditoriaResponse>> obtenerTodas() {

        return ResponseEntity.ok(
                auditoriaService.obtenerTodas()
        );
    }

    @Operation(
            summary = "Obtener auditoría por ID",
            description = "Devuelve un registro de auditoría"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Registro encontrado exitosamente"),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
                    @ApiResponse(responseCode = "404", description = "Registro de auditoría no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AuditoriaResponse> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                auditoriaService.obtenerPorId(id)
        );
    }

    @Operation(
            summary = "Listar auditoría por usuario",
            description = "Filtra los registros de auditoría por usuario"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Auditoría listada exitosamente"),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AuditoriaResponse>> obtenerPorUsuario(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                auditoriaService.obtenerPorUsuario(usuarioId)
        );
    }

    @Operation(
            summary = "Listar auditoría por tipo de operación",
            description = "Filtra los registros por tipo de operación"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Auditoría listada exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Tipo de operación no válido"),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/tipo/{tipoOperacion}")
    public ResponseEntity<List<AuditoriaResponse>> obtenerPorTipo(
            @PathVariable TipoOperacion tipoOperacion) {

        return ResponseEntity.ok(
                auditoriaService.obtenerPorTipo(tipoOperacion)
        );
    }
}