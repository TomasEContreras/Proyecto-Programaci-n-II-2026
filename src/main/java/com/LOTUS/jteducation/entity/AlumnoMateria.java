package com.LOTUS.jteducation.entity;

import com.LOTUS.jteducation.util.Validaciones;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * RELACIÓN N:M entre Alumno y Materia.
 * Como la relación tiene datos propios (las notas), se modela como una ENTIDAD con dos
 * @ManyToOne, en lugar de un @ManyToMany (que no permite guardar atributos en la relación).
 */
@Entity
@Table(name = "alumno_materia",
        uniqueConstraints = @UniqueConstraint(columnNames = {"alumno_id_alumno", "materia_id_materia"}))
@Getter
@NoArgsConstructor
public class AlumnoMateria {

    private static final BigDecimal NOTA_MIN = BigDecimal.ONE;
    private static final BigDecimal NOTA_MAX = BigDecimal.TEN;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_al_mat")
    private Long idAlMat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "alumno_id_alumno", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "materia_id_materia", nullable = false)
    private Materia materia;

    // BigDecimal es el tipo correcto para DECIMAL(4,2). Pueden ser null mientras no se hayan cargado.
    private BigDecimal primerNota;
    private BigDecimal segundaNota;
    private BigDecimal notaFinal;

    public AlumnoMateria(Alumno alumno, Materia materia) {
        this.alumno = validarAlumno(alumno);
        this.materia = validarMateria(materia);
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = validarAlumno(alumno);
    }

    public void setMateria(Materia materia) {
        this.materia = validarMateria(materia);
    }

    public void setPrimerNota(BigDecimal nota) {
        this.primerNota = validarNota(nota);
    }

    public void setSegundaNota(BigDecimal nota) {
        this.segundaNota = validarNota(nota);
    }

    public void setNotaFinal(BigDecimal nota) {
        this.notaFinal = validarNota(nota);
    }

    private static Alumno validarAlumno(Alumno alumno) {
        return Validaciones.requerido(alumno, "Debe indicarse el alumno.");
    }

    private static Materia validarMateria(Materia materia) {
        return Validaciones.requerido(materia, "Debe indicarse la materia.");
    }

    // La nota puede ser null (todavía no cargada); si existe, debe estar entre 1 y 10
    private static BigDecimal validarNota(BigDecimal nota) {
        if (nota != null && (nota.compareTo(NOTA_MIN) < 0 || nota.compareTo(NOTA_MAX) > 0)) {
            throw new IllegalArgumentException("La nota debe estar entre 1 y 10.");
        }
        return nota;
    }
}
