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
public class Domicilio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDomicilio;
    private String ciudad;
    private String calle;
    private int altura;

    public Domicilio(String ciudad, String calle, int altura) {
        this.ciudad = validarCiudad(ciudad);
        this.calle = validarCalle(calle);
        this.altura = validarAltura(altura);
    }

    public void setCiudad(String ciudad) {
        this.ciudad = validarCiudad(ciudad);
    }

    public void setCalle(String calle) {
        this.calle = validarCalle(calle);
    }

    public void setAltura(int altura) {
        this.altura = validarAltura(altura);
    }

    private static String validarCiudad(String ciudad) {
        return Validaciones.texto(ciudad, Validaciones.TEXTO_MAX, "El nombre de la ciudad");
    }

    private static String validarCalle(String calle) {
        return Validaciones.texto(calle, Validaciones.TEXTO_MAX, "El nombre de la calle");
    }

    private static int validarAltura(int altura) {
        return Validaciones.entero(altura, 1, Integer.MAX_VALUE, "La altura ingresada no es correcta.");
    }
}
