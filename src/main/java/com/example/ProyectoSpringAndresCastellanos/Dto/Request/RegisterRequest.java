package com.example.ProyectoSpringAndresCastellanos.Dto.Request;

import com.example.ProyectoSpringAndresCastellanos.Model.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class RegisterRequest {

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 4,max = 50)
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener almenos 6 caracteres")
    private String password;

    private String nombreCompleto;

    @NotNull(message = "El rol es obligatorio")
    private Rol rol;

}
