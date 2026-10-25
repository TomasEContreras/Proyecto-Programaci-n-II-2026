package com.LOTUS.jteducation.entity;

import com.LOTUS.jteducation.util.Validaciones;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OrderBy;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Curso {

    public static final int ANIO_MIN = 1;
    public static final int ANIO_MAX = 6; // Ajustalo si la escuela tiene otra cantidad de años
    public static final int CICLO_MIN = 2000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCurso;
    private int anio;
    @Enumerated(EnumType.STRING)
    private Division division;
    private int cicloLectivo;

    // RELACIÓN N:M. El plan de materias del curso: los alumnos "heredan" estas materias.
    // Es un @ManyToMany (y no una entidad intermedia) porque curso_materia no tiene datos propios.
    // Set: una materia no puede estar dos veces en el mismo curso.
    // @OrderBy + LinkedHashSet: se leen ordenadas por nombre y el Set conserva ese orden.
    @ManyToMany
    @JoinTable(name = "curso_materia",
            joinColumns = @JoinColumn(name = "curso_id_curso"),
            inverseJoinColumns = @JoinColumn(name = "materia_id_materia"))
    @OrderBy("nombre")
    private Set<Materia> materias = new LinkedHashSet<>();

    public Curso(int anio, Division division, int cicloLectivo) {
        this.anio = validarAnio(anio);
        this.division = validarDivision(division);
        this.cicloLectivo = validarCicloLectivo(cicloLectivo);
    }

    public void setAnio(int anio) {
        this.anio = validarAnio(anio);
    }

    public void setDivision(Division division) {
        this.division = validarDivision(division);
    }

    public void setCicloLectivo(int cicloLectivo) {
        this.cicloLectivo = validarCicloLectivo(cicloLectivo);
    }

    // Se devuelve una vista de solo lectura: el plan se cambia únicamente con setMaterias
    public Set<Materia> getMaterias() {
        return Collections.unmodifiableSet(materias);
    }

    // Se reemplaza el CONTENIDO y no la colección: Hibernate sigue la misma instancia para saber qué cambió
    public void setMaterias(Collection<Materia> materias) {
        Validaciones.requerido(materias, "El plan de materias no puede ser nulo.");
        this.materias.clear();
        this.materias.addAll(materias);
    }

    private static int validarAnio(int anio) {
        return Validaciones.entero(anio, ANIO_MIN, ANIO_MAX,
                "El año debe estar entre " + ANIO_MIN + " y " + ANIO_MAX + ".");
    }

    private static Division validarDivision(Division division) {
        return Validaciones.requerido(division, "Debe existir una división.");
    }

    private static int validarCicloLectivo(int cicloLectivo) {
        return Validaciones.entero(cicloLectivo, CICLO_MIN, Integer.MAX_VALUE,
                "El ciclo lectivo ingresado no es correcto.");
    }
}
