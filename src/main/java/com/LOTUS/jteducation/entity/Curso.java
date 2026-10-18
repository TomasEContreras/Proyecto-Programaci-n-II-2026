package com.LOTUS.jteducation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    public Curso(int anio, Division division, int cicloLectivo) {
        setAnio(anio);
        setDivision(division);
        setCicloLectivo(cicloLectivo);
    }

    public void setAnio(int anio) {
        // Antes: (anio <= 0 && anio > 5) -> nunca era verdadero, no validaba nada. Es un OR.
        if (anio < ANIO_MIN || anio > ANIO_MAX) {
            throw new IllegalArgumentException("El año debe estar entre " + ANIO_MIN + " y " + ANIO_MAX + ".");
        }
        this.anio = anio;
    }

    public void setDivision(Division division) {
        if (division == null) {
            throw new IllegalArgumentException("Debe existir una división.");
        }
        this.division = division;
    }

    public void setCicloLectivo(int cicloLectivo) {
        if (cicloLectivo < CICLO_MIN) {
            throw new IllegalArgumentException("El ciclo lectivo ingresado no es correcto.");
        }
        this.cicloLectivo = cicloLectivo;
    }
}
