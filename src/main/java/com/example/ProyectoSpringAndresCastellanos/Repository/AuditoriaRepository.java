package com.example.ProyectoSpringAndresCastellanos.Repository;

import com.example.ProyectoSpringAndresCastellanos.Model.Auditoria;
import com.example.ProyectoSpringAndresCastellanos.Model.TipoOperacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

    List<Auditoria> findByUsuarioId(Long usuarioId);

    List<Auditoria> findByTipoOperacion(TipoOperacion tipoOperacion);
}