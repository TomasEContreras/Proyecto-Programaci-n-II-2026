package com.LOTUS.jteducation.controller;

import com.LOTUS.jteducation.entity.Rol;
import java.security.Principal;
import java.time.Year;
import org.springframework.security.core.Authentication;
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

    // El header muestra los links de gestión solo al administrador
    @ModelAttribute("esAdmin")
    public boolean esAdmin(Principal principal) {
        return tieneRol(principal, Rol.ADMIN);
    }

    // Con Spring Security, el Principal que recibe un controlador es la Authentication del usuario logueado,
    // que trae sus permisos ("ROLE_ADMIN" o "ROLE_ALUMNO"). Es static para reutilizarlo en GlobalExceptionHandler.
    public static boolean tieneRol(Principal principal, Rol rol) {
        return principal instanceof Authentication autenticacion
                && autenticacion.getAuthorities().stream()
                        .anyMatch(permiso -> permiso.getAuthority().equals("ROLE_" + rol.name()));
    }
}
