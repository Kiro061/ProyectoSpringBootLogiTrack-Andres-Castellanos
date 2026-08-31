package com.example.ProyectoSpringAndresCastellanos.Controller;

import com.example.ProyectoSpringAndresCastellanos.Dto.Request.MovimientoRequest;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.MovimientoResponse;
import com.example.ProyectoSpringAndresCastellanos.Service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @PostMapping
    public ResponseEntity<MovimientoResponse> registrar(@Valid@RequestBody MovimientoRequest request){
    return ResponseEntity.ok(movimientoService.registrar(request));
    }

    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> obtenerTodos(){
        return ResponseEntity.ok(movimientoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponse> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok(movimientoService.obtenerPorId(id));
    }
}
