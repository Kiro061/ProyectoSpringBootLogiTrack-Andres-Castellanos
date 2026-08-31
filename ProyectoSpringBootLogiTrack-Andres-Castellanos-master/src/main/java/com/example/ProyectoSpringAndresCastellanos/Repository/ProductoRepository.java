package com.example.ProyectoSpringAndresCastellanos.Repository;

import com.example.ProyectoSpringAndresCastellanos.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto,Long> {
}
