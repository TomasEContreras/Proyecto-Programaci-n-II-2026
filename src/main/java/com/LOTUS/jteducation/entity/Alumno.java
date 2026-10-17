
package com.LOTUS.jteducation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // Le dice a JPA que esta clase es una Entidad
@Getter // Lombok genera automáticamente el método getter para cada uno de los atributos

@NoArgsConstructor // Lombok genera un constructor sin argumentos
@AllArgsConstructor // Lombok genera un constructor con un argumento para cada atributos
public class Alumno {
    
    @Id // Marca un atributo como la clave primaria de la tabla. (id_alumnno)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que la base de datos asigna un nuevo valor de forma autoincremental.
    private Long id_alumno; // El tipo de dato Long se usa para las claves primarias numericas
    private String dni;
    private String nombre;
    private String apellido;
    private LocalDate fecha_nacimiento;
    private Domicilio domicilio;
    
    
    // Setters
    // "parametro.trim()" elimina espacios en blanco que se encuentran en el principio y final de la cadena
    
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) { 
            throw new IllegalArgumentException("El nombre del alumno no puede estar vacio");
        }
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) { 
            throw new IllegalArgumentException("El apellido del alumno no puede estar vacio");
        }
        this.apellido = apellido;
    }
    
    public void setDni(String dni) {
        if (dni == null || dni.trim().isEmpty()) { 
            throw new IllegalArgumentException("El dni del alumno no puede estar vacio");
        }
        this.dni = dni;
    }

    public void setFechaNacimiento(LocalDate fecha_nacimiento) {
        if (fecha_nacimiento == null) { 
            throw new IllegalArgumentException("La fecha de nacimiento del alumno no puede estar vacio");
        }
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public void setDomicilio(Domicilio domicilio) {
        if (domicilio == null) {
            throw new IllegalArgumentException("El alumno debe tener un domicilio");
        }
        this.domicilio = domicilio;
    }
    
}

//PAG 25-26