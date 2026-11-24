package com.LOTUS.jteducation.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.LOTUS.jteducation.entity.Alumno;
import com.LOTUS.jteducation.entity.AlumnoMateria;
import com.LOTUS.jteducation.entity.Curso;
import com.LOTUS.jteducation.entity.Division;
import com.LOTUS.jteducation.entity.Domicilio;
import com.LOTUS.jteducation.entity.Materia;
import com.LOTUS.jteducation.entity.Rol;
import com.LOTUS.jteducation.entity.Usuario;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class MateriaConNotasTest {

    private final Materia matematica = new Materia("Matemática");

    @Test
    void muestraLasNotasSinCerosDeMasYConComa() {
        Alumno alumno = new Alumno("40000000", "Lucía", "Fernández", LocalDate.of(2012, 1, 1),
                LocalDate.of(2026, 3, 1), new Domicilio("Goya", "San Martín", 100),
                new Usuario("lucia@mail.com", "hash", Rol.ALUMNO), new Curso(1, Division.A, 2026));
        AlumnoMateria notas = new AlumnoMateria(alumno, matematica);
        notas.setPrimerNota(new BigDecimal("8.00"));
        notas.setSegundaNota(new BigDecimal("7.50"));
        notas.setNotaFinal(new BigDecimal("10.00"));

        MateriaConNotas fila = new MateriaConNotas(matematica, notas);

        assertThat(fila.primerNota()).isEqualTo("8");
        assertThat(fila.segundaNota()).isEqualTo("7,5");
        assertThat(fila.notaFinal()).isEqualTo("10"); // Sin toPlainString se vería "1E+1"
    }

    @Test
    void sinNotasMuestraGuiones() {
        MateriaConNotas fila = new MateriaConNotas(matematica, null);

        assertThat(fila.primerNota()).isEqualTo("-");
        assertThat(fila.segundaNota()).isEqualTo("-");
        assertThat(fila.notaFinal()).isEqualTo("-");
    }
}
