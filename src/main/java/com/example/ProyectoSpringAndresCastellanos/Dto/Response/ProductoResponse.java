package com.example.ProyectoSpringAndresCastellanos.Dto.Response;

import java.math.BigDecimal;

public record ProductoResponse (
        Integer id,
        String nombre,
        String categoria,
        Integer stock,
        BigDecimal precio
){
}
