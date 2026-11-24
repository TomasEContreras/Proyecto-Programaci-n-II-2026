package com.LOTUS.jteducation.dto;

import com.LOTUS.jteducation.entity.AlumnoMateria;
import com.LOTUS.jteducation.entity.Materia;
import java.math.BigDecimal;
import java.util.function.Function;

/**
 * Una fila de la ficha del alumno: una materia del plan de su curso y sus notas.
 * "notas" es null cuando el alumno todavía no tiene notas cargadas en esa materia.
 * Es un record: una clase inmutable que solo transporta datos hacia la vista.
 */
public record MateriaConNotas(Materia materia, AlumnoMateria notas) {

    // Notas listas para mostrar: "8" en vez de "8.00", "7,5" en vez de "7.50", y "-" si no hay nota
    public String primerNota() {
        return mostrar(AlumnoMateria::getPrimerNota);
    }

    public String segundaNota() {
        return mostrar(AlumnoMateria::getSegundaNota);
    }

    public String notaFinal() {
        return mostrar(AlumnoMateria::getNotaFinal);
    }

    private String mostrar(Function<AlumnoMateria, BigDecimal> nota) {
        BigDecimal valor = (notas == null) ? null : nota.apply(notas);
        // stripTrailingZeros saca los ceros sobrantes; toPlainString evita la notación "1E+1" para el 10
        return (valor == null) ? "-" : valor.stripTrailingZeros().toPlainString().replace('.', ',');
    }
}
