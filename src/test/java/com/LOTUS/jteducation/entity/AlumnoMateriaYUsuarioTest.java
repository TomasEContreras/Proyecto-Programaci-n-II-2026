package com.LOTUS.jteducation.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class AlumnoMateriaYUsuarioTest {

    private AlumnoMateria notas() {
        Alumno alumno = new Alumno("40000000", "Lucía", "Fernández", LocalDate.of(2012, 1, 1),
                LocalDate.of(2026, 3, 1), new Domicilio("Goya", "San Martín", 100),
                new Usuario("lucia@mail.com", "hash", Rol.ALUMNO), new Curso(1, Division.A, 2026));
        return new AlumnoMateria(alumno, new Materia("Matemática"));
    }

    @Test
    void lasNotasVanDe1A10YPuedenQuedarVacias() {
        AlumnoMateria notas = notas();

        notas.setPrimerNota(new BigDecimal("1"));
        notas.setSegundaNota(new BigDecimal("10"));
        notas.setNotaFinal(null); // Todavía no se tomó

        assertThat(notas.getSegundaNota()).isEqualByComparingTo("10");
        assertThat(notas.getNotaFinal()).isNull();
    }

    @Test
    void rechazaNotasFueraDeRango() {
        AlumnoMateria notas = notas();

        assertThatThrownBy(() -> notas.setPrimerNota(new BigDecimal("0.99"))).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> notas.setNotaFinal(new BigDecimal("10.5"))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void laContrasenaTieneQueTenerAlMenos8Caracteres() {
        assertThatThrownBy(() -> Usuario.validarPasswordPlana("1234567")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Usuario.validarPasswordPlana("   ")).isInstanceOf(IllegalArgumentException.class);
        assertThatCode(() -> Usuario.validarPasswordPlana("12345678")).doesNotThrowAnyException();
    }

    @Test
    void elEmailTieneQueTenerArroba() {
        assertThatThrownBy(() -> new Usuario("sin-arroba", "hash", Rol.ADMIN)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Usuario("a@b.com", "hash", null)).isInstanceOf(IllegalArgumentException.class);
    }
}
