package com.LOTUS.jteducation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMateria;
    private String nombre;

    public Materia(String nombre) {
        setNombre(nombre);
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la materia no puede estar vacío."); // decía "del alumno"
        }
        if (nombre.trim().length() > 45) { // El VARCHAR de la tabla es de 45
            throw new IllegalArgumentException("El nombre de la materia no puede superar los 45 caracteres.");
        }
        this.nombre = nombre.trim();
    }
}
