package com.example.ProyectoSpringAndresCastellanos.Controller;

import com.example.ProyectoSpringAndresCastellanos.Dto.Response.AuditoriaResponse;
import com.example.ProyectoSpringAndresCastellanos.Model.TipoOperacion;
import com.example.ProyectoSpringAndresCastellanos.Service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auditorias")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    public ResponseEntity<List<AuditoriaResponse>> obtenerTodas() {

        return ResponseEntity.ok(
                auditoriaService.obtenerTodas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditoriaResponse> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                auditoriaService.obtenerPorId(id)
        );
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AuditoriaResponse>> obtenerPorUsuario(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                auditoriaService.obtenerPorUsuario(usuarioId)
        );
    }

    @GetMapping("/tipo/{tipoOperacion}")
    public ResponseEntity<List<AuditoriaResponse>> obtenerPorTipo(
            @PathVariable TipoOperacion tipoOperacion) {

        return ResponseEntity.ok(
                auditoriaService.obtenerPorTipo(tipoOperacion)
        );
    }
}