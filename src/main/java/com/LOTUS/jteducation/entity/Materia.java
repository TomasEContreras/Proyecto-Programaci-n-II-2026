
package com.LOTUS.jteducation.entity;

import jakarta.persistence.Entity;
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
public class Materia {

    @Id // Marca un atributo como la clave primaria de la tabla. (idMateria)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que la base de datos asigna un nuevo valor de forma autoincremental.
    private Long idMateria; // El tipo de dato Long se usa para las claves primarias numericas
    private String nombre;
    
    // SETTERS
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) { 
            throw new IllegalArgumentException("El nombre del alumno no puede estar vacio");
        }
        this.nombre = nombre;
    }
    
}
