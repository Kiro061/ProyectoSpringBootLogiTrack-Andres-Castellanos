package com.example.ProyectoSpringAndresCastellanos.Dto.Request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record BodegaRequest (
        @NotBlank(message = "No se permite el nombre vacio.")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.")
        String nombre,
        @NotBlank(message = "No se permite la ubicacion vacia.")
        @Size(min = 2, max = 150, message = "La ubicacion debe tener entre 2 y 150 caracteres.")
        String ubicacion,
        @Min(value = 1, message = "La capacidad debe ser mayor a 0.")
        Integer capacidad,
        @Size(max = 100, message = "El nombre del encargado no puede superar 100 caracteres.")
        String encargado
) {
}
