package com.LOTUS.jteducation.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

// Tests unitarios: prueban la clase sola, sin Spring ni base de datos, así corren en milisegundos.
class AlumnoTest {

    private Alumno alumnoValido() {
        return new Alumno("40000000", "Lucía", "Fernández", LocalDate.of(2012, 1, 1), LocalDate.of(2026, 3, 1),
                new Domicilio("Goya", "San Martín", 100),
                new Usuario("lucia@mail.com", "hash", Rol.ALUMNO),
                new Curso(1, Division.A, 2026));
    }

    @Test
    void creaUnAlumnoConDatosValidosYLimpiaLosEspacios() {
        Alumno alumno = new Alumno(" 40000000 ", "  Lucía ", "Fernández", LocalDate.of(2012, 1, 1),
                LocalDate.of(2026, 3, 1), new Domicilio("Goya", "San Martín", 100),
                new Usuario("lucia@mail.com", "hash", Rol.ALUMNO), new Curso(1, Division.A, 2026));

        assertThat(alumno.getDni()).isEqualTo("40000000");
        assertThat(alumno.getNombre()).isEqualTo("Lucía");
    }

    @Test
    void rechazaUnDniConLetrasOConLargoIncorrecto() {
        Alumno alumno = alumnoValido();

        assertThatThrownBy(() -> alumno.setDni("4000000A"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("7 u 8 dígitos");
        assertThatThrownBy(() -> alumno.setDni("123456")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> alumno.setDni(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void siElSetterRechazaElDatoElAlumnoNoCambia() {
        Alumno alumno = alumnoValido();

        assertThatThrownBy(() -> alumno.setNombre("   ")).isInstanceOf(IllegalArgumentException.class);

        assertThat(alumno.getNombre()).isEqualTo("Lucía");
    }

    @Test
    void laFechaDeNacimientoTieneQueSerAnteriorAHoy() {
        Alumno alumno = alumnoValido();

        assertThatThrownBy(() -> alumno.setFechaNacimiento(LocalDate.now()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> alumno.setFechaNacimiento(LocalDate.now().plusDays(1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void elNombreNoPuedeSuperarLos45Caracteres() {
        Alumno alumno = alumnoValido();

        assertThatThrownBy(() -> alumno.setNombre("a".repeat(46)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("45");
    }

    @Test
    void lasRelacionesSonObligatorias() {
        Domicilio domicilio = new Domicilio("Goya", "San Martín", 100);
        Usuario usuario = new Usuario("lucia@mail.com", "hash", Rol.ALUMNO);
        Curso curso = new Curso(1, Division.A, 2026);
        LocalDate nacimiento = LocalDate.of(2012, 1, 1);
        LocalDate inscripcion = LocalDate.of(2026, 3, 1);

        assertThatThrownBy(() -> new Alumno("40000000", "Lucía", "Fernández", nacimiento, inscripcion, null, usuario, curso))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Alumno("40000000", "Lucía", "Fernández", nacimiento, inscripcion, domicilio, null, curso))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> alumnoValido().setCurso(null)).isInstanceOf(IllegalArgumentException.class);
    }
}
