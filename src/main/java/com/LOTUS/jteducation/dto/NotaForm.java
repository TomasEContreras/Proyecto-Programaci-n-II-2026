package com.LOTUS.jteducation.dto;

import com.LOTUS.jteducation.entity.AlumnoMateria;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Las tres notas son opcionales: un campo vacío llega como null y significa "todavía no hay nota".
// Las anotaciones de Bean Validation ignoran los null, así que solo validan las notas que se escribieron.
// @Digits: como máximo 2 enteros y 2 decimales, igual que la columna DECIMAL(4,2).
@Getter
@Setter
@NoArgsConstructor
public class NotaForm {

    private static final String MSJ_RANGO = "La nota debe estar entre 1 y 10";
    private static final String MSJ_DECIMALES = "La nota puede tener como máximo 2 decimales";

    @DecimalMin(value = "1", message = MSJ_RANGO)
    @DecimalMax(value = "10", message = MSJ_RANGO)
    @Digits(integer = 2, fraction = 2, message = MSJ_DECIMALES)
    private BigDecimal primerNota;

    @DecimalMin(value = "1", message = MSJ_RANGO)
    @DecimalMax(value = "10", message = MSJ_RANGO)
    @Digits(integer = 2, fraction = 2, message = MSJ_DECIMALES)
    private BigDecimal segundaNota;

    @DecimalMin(value = "1", message = MSJ_RANGO)
    @DecimalMax(value = "10", message = MSJ_RANGO)
    @Digits(integer = 2, fraction = 2, message = MSJ_DECIMALES)
    private BigDecimal notaFinal;

    // Carga el formulario con las notas que ya existen (o lo deja vacío si todavía no hay ninguna)
    public static NotaForm desde(AlumnoMateria notas) {
        NotaForm form = new NotaForm();
        if (notas != null) {
            form.setPrimerNota(notas.getPrimerNota());
            form.setSegundaNota(notas.getSegundaNota());
            form.setNotaFinal(notas.getNotaFinal());
        }
        return form;
    }

    public boolean estaVacio() {
        return primerNota == null && segundaNota == null && notaFinal == null;
    }
}
