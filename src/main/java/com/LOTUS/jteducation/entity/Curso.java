
package com.LOTUS.jteducation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Curso {

    @Id // Marca un atributo como la clave primaria de la tabla. (idCurso)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que la base de datos asigna un nuevo valor de forma autoincremental.
    private Long idCurso; // El tipo de dato Long se usa para las claves primarias numericas
    private int anio;
    @Enumerated(EnumType.STRING) // STRING guarda el nombre del rol; el valor por defecto (ORDINAL) guardaría un número
    private Division division;
    private int cicloLectivo;
    
    // SETTERS
    
    public void setAnio(int anio) {
        if (anio <= 0 && anio > 5) { 
            throw new IllegalArgumentException("El anio ingresado no es el correcto");
        }
        this.anio = anio;
    }

    public void setDivision(Division division) {
        if (division == null) {
            throw new IllegalArgumentException("Debe existir una division");
        }
        this.division = division;
    }

    public void setCicloLectivo(int cicloLectivo) {
        if (cicloLectivo < 0) {
            throw new IllegalArgumentException("El ciclo lectivo ingresado no es el correcto");
        }
        this.cicloLectivo = cicloLectivo;
    }
    
    
}
