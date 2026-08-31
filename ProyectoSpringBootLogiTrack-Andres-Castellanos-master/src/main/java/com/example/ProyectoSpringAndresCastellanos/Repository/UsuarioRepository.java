package com.example.ProyectoSpringAndresCastellanos.Repository;

import com.example.ProyectoSpringAndresCastellanos.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
