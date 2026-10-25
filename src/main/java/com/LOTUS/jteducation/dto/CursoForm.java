package com.LOTUS.jteducation.dto;

import com.LOTUS.jteducation.entity.Curso;
import com.LOTUS.jteducation.entity.Division;
import com.LOTUS.jteducation.entity.Materia;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Se usan Integer (y no int) para que un campo vacío llegue como null y lo atrape @NotNull,
// en lugar de fallar al convertir el texto a número.
// Los límites salen de las constantes de Curso: así la regla está escrita en un solo lugar.
@Getter
@Setter
@NoArgsConstructor
public class CursoForm {

    @NotNull(message = "El año es obligatorio")
    @Min(value = Curso.ANIO_MIN, message = "El año debe estar entre " + Curso.ANIO_MIN + " y " + Curso.ANIO_MAX)
    @Max(value = Curso.ANIO_MAX, message = "El año debe estar entre " + Curso.ANIO_MIN + " y " + Curso.ANIO_MAX)
    private Integer anio;

    @NotNull(message = "La división es obligatoria")
    private Division division;

    // El ciclo lectivo NO está acá a propósito: lo asigna el sistema (año actual) al crear el curso.

    // El formulario solo envía los ids de los checkboxes marcados; el servicio los convierte en materias
    @NotEmpty(message = "Elegí al menos una materia para el curso")
    private Set<Long> idsMaterias = new HashSet<>();

    // Carga el formulario de edición con los datos actuales del curso
    public static CursoForm desde(Curso curso) {
        CursoForm form = new CursoForm();
        form.setAnio(curso.getAnio());
        form.setDivision(curso.getDivision());
        form.setIdsMaterias(curso.getMaterias().stream()
                .map(Materia::getIdMateria)
                .collect(Collectors.toSet()));
        return form;
    }
}
