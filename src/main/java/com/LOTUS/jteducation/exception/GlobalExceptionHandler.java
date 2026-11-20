package com.LOTUS.jteducation.exception;

import java.security.Principal;
import java.time.Year;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

// Punto 4 del parcial: manejo centralizado de excepciones.
// Si un controlador deja "escapar" una de estas excepciones, termina acá y el usuario ve una página
// con el diseño del sitio (templates/error.html) en vez de un error técnico de Spring.
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ModelAndView manejarNoEncontrado(RecursoNoEncontradoException e, Principal principal) {
        return crearVista(principal, HttpStatus.NOT_FOUND, "Recurso no encontrado", e.getMessage());
    }

    // Red de seguridad: los controladores ya capturan esta excepción cuando pueden mostrarla en el formulario
    @ExceptionHandler(ReglaDeNegocioException.class)
    public ModelAndView manejarReglaDeNegocio(ReglaDeNegocioException e, Principal principal) {
        return crearVista(principal, HttpStatus.CONFLICT, "No se pudo completar la operación", e.getMessage());
    }

    // Red de seguridad: un dato inválido que rechazó una entidad y ningún controlador capturó
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView manejarDatoInvalido(IllegalArgumentException e, Principal principal) {
        return crearVista(principal, HttpStatus.BAD_REQUEST, "Datos inválidos", e.getMessage());
    }

    // Red de seguridad: la base rechazó la operación (clave repetida, FK). No se muestra el mensaje
    // técnico de MySQL porque expone detalles internos (nombres de tablas y restricciones).
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView manejarIntegridad(DataIntegrityViolationException e, Principal principal) {
        return crearVista(principal, HttpStatus.CONFLICT, "No se pudo completar la operación",
                "Los datos entran en conflicto con información ya guardada.");
    }

    // No hay un @ExceptionHandler(Exception.class) a propósito: Spring Boot ya muestra templates/error.html
    // para cualquier otro error, y un "atrapa todo" convertiría los 404 y 400 en errores 500.

    private ModelAndView crearVista(Principal principal, HttpStatus status, String titulo, String mensaje) {
        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(status); // El navegador recibe el código HTTP correcto (404, 409...)
        mav.addObject("status", status.value());
        mav.addObject("titulo", titulo);
        mav.addObject("mensaje", mensaje);
        // Los @ModelAttribute de GlobalControllerAdvice no corren en los @ExceptionHandler: se agregan a mano
        mav.addObject("anioActual", Year.now().getValue());
        mav.addObject("emailUsuario", principal == null ? null : principal.getName()); // Para mostrar el menú
        return mav;
    }
}
