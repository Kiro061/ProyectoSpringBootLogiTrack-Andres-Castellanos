package com.example.ProyectoSpringAndresCastellanos.Service;

import com.example.ProyectoSpringAndresCastellanos.Dto.Request.BodegaRequest;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.BodegaResponse;
import com.example.ProyectoSpringAndresCastellanos.Exception.BusinessRuleException;
import com.example.ProyectoSpringAndresCastellanos.Mapper.BodegaMapper;
import com.example.ProyectoSpringAndresCastellanos.Model.Bodega;
import com.example.ProyectoSpringAndresCastellanos.Model.TipoOperacion;
import com.example.ProyectoSpringAndresCastellanos.Repository.BodegaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BodegaService {

    private final BodegaRepository bodegaRepository;
    private final BodegaMapper bodegaMapper;
    private final AuditoriaService auditoriaService;

    // Crear una bodega
    public BodegaResponse crear(BodegaRequest request) {

        Bodega bodega = bodegaMapper.toEntity(request);

        Bodega guardada = bodegaRepository.save(bodega);

        auditoriaService.registrar(
                guardada,
                TipoOperacion.INSERT,
                null,
                guardada.getAuditData()
        );

        return bodegaMapper.toResponse(guardada);
    }

    // Obtener todas las bodegas
    public List<BodegaResponse> obtenerTodas() {

        return bodegaRepository.findAll()
                .stream()
                .map(bodegaMapper::toResponse)
                .toList();
    }

    // Obtener una bodega por ID
    public BodegaResponse obtenerPorId(Long id) {

        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessRuleException(
                                "Bodega no encontrada con id: " + id
                        )
                );

        return bodegaMapper.toResponse(bodega);
    }

    // Actualizar una bodega
    public BodegaResponse actualizar(Long id, BodegaRequest request) {

        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessRuleException(
                                "Bodega no encontrada con id: " + id
                        )
                );
        String valorAnterior = bodega.getAuditData();

        bodega.setNombre(request.nombre());
        bodega.setUbicacion(request.ubicacion());
        bodega.setCapacidad(request.capacidad());
        bodega.setEncargado(request.encargado());

        Bodega actualizada = bodegaRepository.save(bodega);

        String valorNuevo = actualizada.getAuditData();
        auditoriaService.registrar(
                actualizada,
                TipoOperacion.UPDATE,
                valorAnterior,
                valorNuevo
        );

        return bodegaMapper.toResponse(actualizada);
    }

    // Eliminar una bodega
    public void eliminar(Long id) {

        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessRuleException(
                                "Bodega no encontrada con id: " + id
                        )
                );
        String valorAnterior = bodega.getAuditData();

        bodegaRepository.delete(bodega);

        auditoriaService.registrar(
                bodega,
                TipoOperacion.DELETE,
                valorAnterior,
                null
        );
    }
}