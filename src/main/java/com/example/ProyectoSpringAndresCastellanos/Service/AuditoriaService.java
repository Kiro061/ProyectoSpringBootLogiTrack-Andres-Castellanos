package com.example.ProyectoSpringAndresCastellanos.Service;

import com.example.ProyectoSpringAndresCastellanos.Dto.Response.AuditoriaResponse;
import com.example.ProyectoSpringAndresCastellanos.Exception.BusinessRuleException;
import com.example.ProyectoSpringAndresCastellanos.Mapper.AuditoriaMapper;
import com.example.ProyectoSpringAndresCastellanos.Model.TipoOperacion;
import com.example.ProyectoSpringAndresCastellanos.Repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final AuditoriaMapper auditoriaMapper;

    public List<AuditoriaResponse> obtenerTodas() {

        return auditoriaRepository.findAll()
                .stream()
                .map(auditoriaMapper::toResponse)
                .toList();
    }

    public List<AuditoriaResponse> obtenerPorUsuario(Long usuarioId) {

        return auditoriaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(auditoriaMapper::toResponse)
                .toList();
    }

    public List<AuditoriaResponse> obtenerPorTipo(
            TipoOperacion tipoOperacion) {

        return auditoriaRepository
                .findByTipoOperacion(tipoOperacion)
                .stream()
                .map(auditoriaMapper::toResponse)
                .toList();
    }

    public AuditoriaResponse obtenerPorId(Long id) {

        return auditoriaRepository.findById(id)
                .map(auditoriaMapper::toResponse)
                .orElseThrow(() ->
                        new BusinessRuleException(
                                "Auditoría no encontrada con id: " + id
                        )
                );
    }
}