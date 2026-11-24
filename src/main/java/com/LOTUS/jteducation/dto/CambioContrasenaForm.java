package com.LOTUS.jteducation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Formulario "Cambiar contraseña". Las reglas entre campos (la actual es correcta, las dos nuevas
// coinciden y son distintas de la actual) las controla CuentaService.
@Getter
@Setter
@NoArgsConstructor
public class CambioContrasenaForm {

    @NotBlank(message = "Ingresá tu contraseña actual")
    private String actual;

    @NotBlank(message = "Ingresá la nueva contraseña")
    @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres")
    private String nueva;

    @NotBlank(message = "Repetí la nueva contraseña")
    private String confirmacion;
}
