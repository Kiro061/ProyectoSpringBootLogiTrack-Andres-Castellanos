package com.example.ProyectoSpringAndresCastellanos.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private BigDecimal precio;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getAuditData() {
        return "id=" + id +
                ", nombre=" + nombre +
                ", categoria=" + categoria +
                ", stock=" + stock +
                ", precio=" + precio;
    }
}
