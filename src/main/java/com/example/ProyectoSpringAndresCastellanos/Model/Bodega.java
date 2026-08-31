package com.example.ProyectoSpringAndresCastellanos.Model;

import com.example.ProyectoSpringAndresCastellanos.Model.Listener.AuditoriaListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bodegas")
@EntityListeners(AuditoriaListener.class)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bodega implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String ubicacion;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(length = 100)
    private String encargado;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getAuditData() {
        return "nombre=" + nombre +
                ", ubicacion=" + ubicacion +
                ", capacidad=" + capacidad +
                ", encargado=" + encargado;
    }
}