
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

@NoArgsConstructor
@AllArgsConstructor
public class Domicilio {
    
    @Id // Marca un atributo como la clave primaria de la tabla. (id_alumnno)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que la base de datos asigna un nuevo valor de forma autoincremental.
    private Long idDomicilio;
    private String ciudad;
    private String calle;
    private int altura;
    
    
    public void setCiudad(String ciudad) {
        if (ciudad == null || ciudad.trim().isEmpty()) { 
            throw new IllegalArgumentException("El nombre de la ciudad no puede estar vacio");
        }
        this.ciudad = ciudad;
    }

    public void setCalle(String calle) {
        if (calle == null || calle.trim().isEmpty()) { 
            throw new IllegalArgumentException("El nombre de la calle no puede estar vacio");
        }
        this.calle = calle;
    }

    public void setAltura(int altura) {
        if (altura < 0) { 
            throw new IllegalArgumentException("La altura ingresada no es correcta");
        }
        this.altura = altura;
    }
    
}
