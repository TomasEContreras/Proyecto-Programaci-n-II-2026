package com.LOTUS.jteducation.exception;

// Se lanza cuando se busca algo por id y no existe.
// GlobalExceptionHandler la convierte en una página 404 con el diseño del sitio.
// Hereda de RuntimeException (unchecked) a propósito: no obliga a poner throws en cada método que busca
// por id, y puede subir sola hasta GlobalExceptionHandler.
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
