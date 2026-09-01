package com.example.ProyectoSpringAndresCastellanos.Service;

import com.example.ProyectoSpringAndresCastellanos.Dto.Response.AuditoriaResponse;
import com.example.ProyectoSpringAndresCastellanos.Exception.BusinessRuleException;
import com.example.ProyectoSpringAndresCastellanos.Mapper.AuditoriaMapper;
import com.example.ProyectoSpringAndresCastellanos.Model.Auditable;
import com.example.ProyectoSpringAndresCastellanos.Model.Auditoria;
import com.example.ProyectoSpringAndresCastellanos.Model.TipoOperacion;
import com.example.ProyectoSpringAndresCastellanos.Model.Usuario;
import com.example.ProyectoSpringAndresCastellanos.Repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final AuditoriaMapper auditoriaMapper;

    /**
     * Registra una operación en la tabla de auditoría.
     */
    public void registrar(
            Auditable entidad,
            TipoOperacion tipoOperacion,
            String valorAnterior,
            String valorNuevo) {

        Auditoria auditoria = new Auditoria();

        auditoria.setTipoOperacion(tipoOperacion);
        auditoria.setFechaHora(LocalDateTime.now());

        auditoria.setUsuario(obtenerUsuarioActual());

        auditoria.setEntidadAfectada(
                entidad.getClass().getSimpleName()
        );

        auditoria.setEntidadId(entidad.getId());

        auditoria.setValoresAnteriores(valorAnterior);
        auditoria.setValoresNuevos(valorNuevo);

        auditoriaRepository.save(auditoria);
    }

    /**
     * Obtiene el usuario autenticado mediante el JWT.
     */
    private Usuario obtenerUsuarioActual() {

        var authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof Usuario usuario) {
            return usuario;
        }

        return null;
    }

    /**
     * Consulta todas las auditorías.
     */
    public List<AuditoriaResponse> obtenerTodas() {

        return auditoriaRepository.findAll()
                .stream()
                .map(auditoriaMapper::toResponse)
                .toList();
    }

    /**
     * Consulta auditorías realizadas por un usuario.
     */
    public List<AuditoriaResponse> obtenerPorUsuario(Long usuarioId) {

        return auditoriaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(auditoriaMapper::toResponse)
                .toList();
    }

    /**
     * Consulta auditorías por tipo de operación.
     */
    public List<AuditoriaResponse> obtenerPorTipo(
            TipoOperacion tipoOperacion) {

        return auditoriaRepository
                .findByTipoOperacion(tipoOperacion)
                .stream()
                .map(auditoriaMapper::toResponse)
                .toList();
    }

    /**
     * Consulta una auditoría por ID.
     */
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
