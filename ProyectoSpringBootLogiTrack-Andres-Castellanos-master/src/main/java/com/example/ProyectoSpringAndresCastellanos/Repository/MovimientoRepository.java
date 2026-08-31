package com.example.ProyectoSpringAndresCastellanos.Repository;

import com.example.ProyectoSpringAndresCastellanos.Model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoRepository extends JpaRepository<Movimiento,Long> {
}
