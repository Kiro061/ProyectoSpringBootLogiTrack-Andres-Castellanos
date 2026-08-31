package com.example.ProyectoSpringAndresCastellanos.Model;

import com.example.ProyectoSpringAndresCastellanos.Model.Listener.AuditoriaListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "productos")
@EntityListeners(AuditoriaListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mantén aquí tus demás campos exactamente como los tienes.

    // Ejemplo:
    private String nombre;

    private String categoria;

    private Integer stock;

    private Double precio;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getAuditData() {
        return "nombre=" + nombre +
                ", categoria=" + categoria +
                ", stock=" + stock +
                ", precio=" + precio;
    }
}