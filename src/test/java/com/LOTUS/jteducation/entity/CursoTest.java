package com.LOTUS.jteducation.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class CursoTest {

    @Test
    void elAnioTieneQueEstarEntre1Y6() {
        assertThatThrownBy(() -> new Curso(0, Division.A, 2026)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Curso(7, Division.A, 2026)).isInstanceOf(IllegalArgumentException.class);
        assertThat(new Curso(6, Division.A, 2026).getAnio()).isEqualTo(6);
    }

    @Test
    void laDivisionEsObligatoriaYElCicloDesde2000() {
        assertThatThrownBy(() -> new Curso(1, null, 2026)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Curso(1, Division.A, 1999)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void elPlanDeMateriasSoloSeCambiaConSetMaterias() {
        Curso curso = new Curso(4, Division.A, 2026);
        curso.setMaterias(List.of(new Materia("Matemática"), new Materia("Química")));

        assertThat(curso.getMaterias()).hasSize(2);
        // getMaterias devuelve una vista de solo lectura: desde afuera no se puede modificar
        assertThatThrownBy(() -> curso.getMaterias().clear()).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void setMateriasReemplazaElPlanYNoAceptaNull() {
        Curso curso = new Curso(4, Division.A, 2026);
        curso.setMaterias(List.of(new Materia("Matemática"), new Materia("Química")));
        curso.setMaterias(List.of(new Materia("Historia")));

        assertThat(curso.getMaterias()).extracting(Materia::getNombre).containsExactly("Historia");
        assertThatThrownBy(() -> curso.setMaterias(null)).isInstanceOf(IllegalArgumentException.class);
    }
}
