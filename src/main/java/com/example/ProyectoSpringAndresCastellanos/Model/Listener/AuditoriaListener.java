package com.example.ProyectoSpringAndresCastellanos.Model.Listener;

import com.example.ProyectoSpringAndresCastellanos.Model.Auditable;
import com.example.ProyectoSpringAndresCastellanos.Model.Auditoria;
import com.example.ProyectoSpringAndresCastellanos.Model.TipoOperacion;
import com.example.ProyectoSpringAndresCastellanos.Model.Usuario;
import com.example.ProyectoSpringAndresCastellanos.Repository.AuditoriaRepository;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuditoriaListener {

    private static AuditoriaRepository auditoriaRepository;

    public AuditoriaListener(AuditoriaRepository auditoriaRepository) {
        AuditoriaListener.auditoriaRepository = auditoriaRepository;
    }

    @PostPersist
    public void registrarInsert(Object entidad) {

        if (!(entidad instanceof Auditable auditable)) {
            return;
        }

        registrarAuditoria(
                auditable,
                TipoOperacion.INSERT,
                null,
                auditable.getAuditData()
        );
    }

    @PostUpdate
    public void registrarUpdate(Object entidad) {

        if (!(entidad instanceof Auditable auditable)) {
            return;
        }

        registrarAuditoria(
                auditable,
                TipoOperacion.UPDATE,
                null,
                auditable.getAuditData()
        );
    }

    @PostRemove
    public void registrarDelete(Object entidad) {

        if (!(entidad instanceof Auditable auditable)) {
            return;
        }

        registrarAuditoria(
                auditable,
                TipoOperacion.DELETE,
                auditable.getAuditData(),
                null
        );
    }

    private void registrarAuditoria(
            Auditable entidad,
            TipoOperacion tipoOperacion,
            String valoresAnteriores,
            String valoresNuevos) {

        Auditoria auditoria = new Auditoria();

        auditoria.setTipoOperacion(tipoOperacion);
        auditoria.setFechaHora(LocalDateTime.now());

        auditoria.setUsuario(obtenerUsuarioActual());

        auditoria.setEntidadAfectada(
                entidad.getClass().getSimpleName()
        );

        auditoria.setEntidadId(entidad.getId());

        auditoria.setValoresAnteriores(valoresAnteriores);

        auditoria.setValoresNuevos(valoresNuevos);

        auditoriaRepository.save(auditoria);
    }

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
}