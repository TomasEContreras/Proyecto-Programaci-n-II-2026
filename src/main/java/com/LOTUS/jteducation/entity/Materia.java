package com.LOTUS.jteducation.entity;

import com.LOTUS.jteducation.util.Validaciones;
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

    // El constructor NO llama a los setters (son públicos y sobrescribibles): usa la validación privada
    public Materia(String nombre) {
        this.nombre = validarNombre(nombre);
    }

    public void setNombre(String nombre) {
        this.nombre = validarNombre(nombre);
    }

    // Una única regla, compartida por el constructor y el setter
    private static String validarNombre(String nombre) {
        return Validaciones.texto(nombre, Validaciones.TEXTO_MAX, "El nombre de la materia");
    }
}
