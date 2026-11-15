package com.LOTUS.jteducation.exception;

// Se lanza cuando una operación viola una regla (DNI repetido, borrar algo que tiene datos asociados, etc.)
// Hereda de RuntimeException a propósito: @Transactional solo deshace los cambios (rollback) ante excepciones
// unchecked, así que si se lanza a mitad de una operación no queda nada guardado a medias.
public class ReglaDeNegocioException extends RuntimeException {

    public ReglaDeNegocioException(String mensaje) {
        super(mensaje);
    }
}
