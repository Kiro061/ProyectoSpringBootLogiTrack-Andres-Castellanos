package com.example.ProyectoSpringAndresCastellanos.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Getter@Setter
@AllArgsConstructor@NoArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String nombre;
    @Column(length = 50)
    private String categoria;
    @Column(nullable = false)
    private int stock;
    @Column(nullable = false)
    private BigDecimal precio;
}
