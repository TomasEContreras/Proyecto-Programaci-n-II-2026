package com.LOTUS.jteducation.entity;

import com.LOTUS.jteducation.util.Validaciones;
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
