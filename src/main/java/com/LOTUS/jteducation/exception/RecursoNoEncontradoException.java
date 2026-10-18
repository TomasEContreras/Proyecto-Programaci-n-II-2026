package com.LOTUS.jteducation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// @ResponseStatus: si esta excepción llega al navegador, responde 404 (no encontrado) en vez de un 500.
// Más adelante un @ControllerAdvice puede mostrar una página de error con el diseño del sitio.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
