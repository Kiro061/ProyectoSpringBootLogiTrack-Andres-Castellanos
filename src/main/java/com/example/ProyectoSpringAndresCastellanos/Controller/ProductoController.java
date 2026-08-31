package com.example.ProyectoSpringAndresCastellanos.Controller;

import com.example.ProyectoSpringAndresCastellanos.Dto.Request.ProductoRequest;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.ProductoResponse;
import com.example.ProyectoSpringAndresCastellanos.Service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid@RequestBody ProductoRequest request){
        return ResponseEntity.ok(productoService.crear(request));
    }

    @PostMapping
    public ResponseEntity<List<ProductoResponse>> obtenerTodos(){
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public  ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public  ResponseEntity<ProductoResponse> actualizar(@PathVariable Long id, @Valid@RequestBody ProductoRequest request){
        return ResponseEntity.ok(productoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
