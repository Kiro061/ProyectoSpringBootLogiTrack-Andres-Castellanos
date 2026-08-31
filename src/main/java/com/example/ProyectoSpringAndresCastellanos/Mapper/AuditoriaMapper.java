package com.example.ProyectoSpringAndresCastellanos.Mapper;

import com.example.ProyectoSpringAndresCastellanos.Dto.Response.AuditoriaResponse;
import com.example.ProyectoSpringAndresCastellanos.Model.Auditoria;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaMapper {

    public AuditoriaResponse toResponse(Auditoria auditoria) {

        return new AuditoriaResponse(
                auditoria.getId(),
                auditoria.getTipoOperacion().name(),
                auditoria.getFechaHora(),
                auditoria.getUsuario() != null
                        ? auditoria.getUsuario().getId()
                        : null,
                auditoria.getUsuario() != null
                        ? auditoria.getUsuario().getUsername()
                        : null,
                auditoria.getEntidadAfectada(),
                auditoria.getEntidadId(),
                auditoria.getValoresAnteriores(),
                auditoria.getValoresNuevos()
        );
    }
}