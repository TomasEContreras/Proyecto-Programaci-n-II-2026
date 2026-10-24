package com.LOTUS.jteducation.exception;

import java.time.Year;
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
    public ModelAndView manejarNoEncontrado(RecursoNoEncontradoException e) {
        return crearVista(HttpStatus.NOT_FOUND, "Recurso no encontrado", e.getMessage());
    }

    // Red de seguridad: los controladores ya capturan esta excepción cuando pueden mostrarla en el formulario
    @ExceptionHandler(ReglaDeNegocioException.class)
    public ModelAndView manejarReglaDeNegocio(ReglaDeNegocioException e) {
        return crearVista(HttpStatus.CONFLICT, "No se pudo completar la operación", e.getMessage());
    }

    private ModelAndView crearVista(HttpStatus status, String titulo, String mensaje) {
        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(status); // El navegador recibe el código HTTP correcto (404, 409...)
        mav.addObject("status", status.value());
        mav.addObject("titulo", titulo);
        mav.addObject("mensaje", mensaje);
        // El @ModelAttribute de GlobalControllerAdvice no corre en los @ExceptionHandler: se agrega a mano
        mav.addObject("anioActual", Year.now().getValue());
        return mav;
    }
}
