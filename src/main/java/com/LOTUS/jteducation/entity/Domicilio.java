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
public class Domicilio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDomicilio;
    private String ciudad;
    private String calle;
    private int altura;

    // Constructor que reutiliza los setters, así la validación no se puede saltear
    // (@AllArgsConstructor asignaba los campos directamente, sin validar).
    public Domicilio(String ciudad, String calle, int altura) {
        setCiudad(ciudad);
        setCalle(calle);
        setAltura(altura);
    }

    public void setCiudad(String ciudad) {
        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la ciudad no puede estar vacío.");
        }
        this.ciudad = ciudad.trim();
    }

    public void setCalle(String calle) {
        if (calle == null || calle.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la calle no puede estar vacío.");
        }
        this.calle = calle.trim();
    }

    public void setAltura(int altura) {
        if (altura <= 0) { // Una altura 0 tampoco tiene sentido
            throw new IllegalArgumentException("La altura ingresada no es correcta.");
        }
        this.altura = altura;
    }
}
