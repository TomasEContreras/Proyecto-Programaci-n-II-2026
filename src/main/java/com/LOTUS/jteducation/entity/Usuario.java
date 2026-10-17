
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
public class Usuario {
    
    @Id // Marca un atributo como la clave primaria de la tabla. (id_alumnno)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que la base de datos asigna un nuevo valor de forma autoincremental.
    private Long idUsuario;
    private String email;
    private String passwordHash;
    @Enumerated(EnumType.STRING) // STRING guarda el nombre del rol; el valor por defecto (ORDINAL) guardaría un número
    private Rol rol;
    
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) { 
            throw new IllegalArgumentException("El email no puede estar vacio");
        }
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.trim().isEmpty()) { 
            throw new IllegalArgumentException("El usuario debe contener una contraseña");
        }
        this.passwordHash = passwordHash;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
    
}
