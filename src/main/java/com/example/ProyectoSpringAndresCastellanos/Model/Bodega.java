package com.example.ProyectoSpringAndresCastellanos.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bodegas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bodega implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String ubicacion;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(nullable = false)
    private String encargado;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getAuditData() {
        return "id=" + id +
                ", nombre=" + nombre +
                ", ubicacion=" + ubicacion +
                ", capacidad=" + capacidad +
                ", encargado=" + encargado;
    }
}
