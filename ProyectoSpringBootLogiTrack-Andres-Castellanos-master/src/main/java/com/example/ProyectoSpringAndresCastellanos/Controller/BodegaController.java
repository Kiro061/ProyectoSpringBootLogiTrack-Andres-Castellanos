package com.example.ProyectoSpringAndresCastellanos.Controller;


import com.example.ProyectoSpringAndresCastellanos.Dto.Request.BodegaRequest;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.BodegaResponse;
import com.example.ProyectoSpringAndresCastellanos.Mapper.BodegaMapper;
import com.example.ProyectoSpringAndresCastellanos.Model.Bodega;
import com.example.ProyectoSpringAndresCastellanos.Repository.BodegaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bodegas")
@RequiredArgsConstructor
public class BodegaController {
    private final BodegaRepository bodegaRepository;
    private final BodegaMapper bodegaMapper;

    // Crear una bodega
    @PostMapping
    public ResponseEntity<BodegaResponse> crear(
            @Valid@RequestBody BodegaRequest request){
        Bodega bodega = bodegaMapper.toEntity(request);
        Bodega guardar = bodegaRepository.save(bodega);
        return ResponseEntity.ok(bodegaMapper.toResponse(guardar));
    }

    // Listar todas las bodegas
    @GetMapping
    public ResponseEntity<List<BodegaResponse>> listar() {
        List<BodegaResponse> bodegas = bodegaRepository.findAll()
                .stream()
                .map(bodegaMapper::toResponse)
                .toList();
        return ResponseEntity.ok(bodegas);
    }

    // Buscar una bodega por ID
    @GetMapping("/{id}")
    public ResponseEntity<BodegaResponse> buscarPorId(
            @PathVariable Long id){
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Bodega no encontrada."));
        return ResponseEntity.ok(bodegaMapper.toResponse(bodega));
    }

    // Actualizar una bodega
    @PutMapping("/{id}")
    public ResponseEntity<BodegaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody BodegaRequest request){
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Bodega no encontrada."));
        bodega.setNombre(request.nombre());
        bodega.setUbicacion(request.ubicacion());
        bodega.setCapacidad(request.capacidad());
        bodega.setEncargado(request.encargado());
        Bodega actualizada = bodegaRepository.save(bodega);
        return ResponseEntity.ok(bodegaMapper.toResponse(actualizada));

    }

    // Eliminar una bodega
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bodega no encontrada"));
        bodegaRepository.delete(bodega);
        return ResponseEntity.noContent().build();
    }
}

