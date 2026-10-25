package com.LOTUS.jteducation.dto;

import com.LOTUS.jteducation.entity.AlumnoMateria;
import com.LOTUS.jteducation.entity.Materia;

/**
 * Una fila de la ficha del alumno: una materia del plan de su curso y sus notas.
 * "notas" es null cuando el alumno todavía no tiene notas cargadas en esa materia.
 * Es un record: una clase inmutable que solo transporta datos hacia la vista.
 */
public record MateriaConNotas(Materia materia, AlumnoMateria notas) {
}
