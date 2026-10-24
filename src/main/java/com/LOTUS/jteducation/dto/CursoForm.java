package com.LOTUS.jteducation.dto;

import com.LOTUS.jteducation.entity.Curso;
import com.LOTUS.jteducation.entity.Division;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Se usan Integer (y no int) para que un campo vacío llegue como null y lo atrape @NotNull,
// en lugar de fallar al convertir el texto a número.
// Los límites salen de las constantes de Curso: así la regla está escrita en un solo lugar.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CursoForm {

    @NotNull(message = "El año es obligatorio")
    @Min(value = Curso.ANIO_MIN, message = "El año debe estar entre " + Curso.ANIO_MIN + " y " + Curso.ANIO_MAX)
    @Max(value = Curso.ANIO_MAX, message = "El año debe estar entre " + Curso.ANIO_MIN + " y " + Curso.ANIO_MAX)
    private Integer anio;

    @NotNull(message = "La división es obligatoria")
    private Division division;

    @NotNull(message = "El ciclo lectivo es obligatorio")
    @Min(value = Curso.CICLO_MIN, message = "El ciclo lectivo ingresado no es correcto")
    private Integer cicloLectivo;

}
