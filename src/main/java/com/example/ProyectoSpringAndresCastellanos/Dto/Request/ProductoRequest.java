package com.example.ProyectoSpringAndresCastellanos.Dto.Request;

import jakarta.persistence.NamedAttributeNode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductoRequest (
        @NotBlank(message = "No se permite el nombre vacio.")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres." )
        String nombre,
        @NotBlank(message = "No se permite el nombre vacio.")
        @Size(min = 2, max = 50, message = "La categoria debe tener enntre 2 y 150 caracteres.")
        String categoria,
        @NotNull(message = "El stock no puede estar nulo")
        @Min(value =0,message = "El stock no puede ser negativo")
        Integer stock,
        @NotBlank(message = "No se permite el precio vacio.")
        @Min(value = 1, message = "El precio debe ser mayor a 0.")
        BigDecimal precio

){
}
