package com.example.ProyectoSpringAndresCastellanos.Repository;

import com.example.ProyectoSpringAndresCastellanos.Model.Bodega;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BodegaRepository extends JpaRepository<Bodega, Long> {
}