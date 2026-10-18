package com.LOTUS.jteducation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Getter
@NoArgsConstructor
public class Usuario {

    // Las constantes static NO son columnas: JPA las ignora. Van fuera del camino de @Id.
    public static final int LONG_MIN_PASS = 8;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario") // El campo se llama "id", la columna "id_usuario"
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING) // Guarda "ADMIN"/"ALUMNO" y no un número
    @Column(nullable = false)
    private Rol rol;

    public Usuario(String email, String passwordHash, Rol rol) {
        setEmail(email);
        setPasswordHash(passwordHash);
        setRol(rol);
    }

    // Esta regla se aplica sobre la contraseña EN TEXTO PLANO, ANTES de hashearla
    // (el hash siempre mide lo mismo, no tiene sentido validar su largo).
    public static void validarPasswordPlana(String passwordPlana) {
        if (passwordPlana == null || passwordPlana.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }
        if (passwordPlana.length() < LONG_MIN_PASS) {
            throw new IllegalArgumentException("La contraseña debe tener mínimo " + LONG_MIN_PASS + " caracteres.");
        }
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("El email ingresado no es válido.");
        }
        this.email = email.trim();
    }

    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("El hash de la contraseña no puede estar vacío.");
        }
        this.passwordHash = passwordHash;
    }

    public void setRol(Rol rol) {
        if (rol == null) { // Antes la condición estaba invertida: rechazaba los roles válidos
            throw new IllegalArgumentException("El usuario debe tener un rol.");
        }
        this.rol = rol;
    }
}
