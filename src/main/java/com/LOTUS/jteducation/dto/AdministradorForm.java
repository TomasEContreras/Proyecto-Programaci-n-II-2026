package com.LOTUS.jteducation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Datos del formulario para crear un administrador.
// Que las dos contraseñas coincidan lo controla el servicio (es una regla entre dos campos).
@Getter
@Setter
@NoArgsConstructor
public class AdministradorForm {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Ingresá un email válido")
    @Size(max = 100, message = "El email no puede superar los 100 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres")
    private String password;

    @NotBlank(message = "Repetí la contraseña")
    private String confirmacion;
}
