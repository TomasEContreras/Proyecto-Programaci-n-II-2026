package com.LOTUS.jteducation.controller;

import java.security.Principal;
import java.time.Year;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

// Desafío del apunte (footer dinámico): @ControllerAdvice aplica a TODOS los controladores.
// Todo @ModelAttribute declarado acá queda disponible en todas las vistas, sin repetirlo en cada método.
@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("anioActual")
    public int anioActual() {
        return Year.now().getValue();
    }

    // Email del usuario logueado (null si no hay sesión): el header lo usa para mostrar el menú
    @ModelAttribute("emailUsuario")
    public String emailUsuario(Principal principal) {
        return principal == null ? null : principal.getName();
    }
}
