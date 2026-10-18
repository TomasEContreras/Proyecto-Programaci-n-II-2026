package com.LOTUS.jteducation.exception;

// Se lanza cuando una operación viola una regla (DNI repetido, borrar algo que tiene datos asociados, etc.)
public class ReglaDeNegocioException extends RuntimeException {

    public ReglaDeNegocioException(String mensaje) {
        super(mensaje);
    }
}
