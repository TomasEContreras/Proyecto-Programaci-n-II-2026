package com.LOTUS.jteducation.exception;

// Se lanza cuando se busca algo por id y no existe.
// GlobalExceptionHandler la convierte en una página 404 con el diseño del sitio.
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
