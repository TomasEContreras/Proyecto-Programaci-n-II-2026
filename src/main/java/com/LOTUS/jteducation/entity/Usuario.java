package com.LOTUS.jteducation.entity;

import com.LOTUS.jteducation.util.Validaciones;
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

    // Las constantes static NO son columnas: JPA las ignora.
    public static final int LONG_MIN_PASS = 8;
    private static final int EMAIL_MAX = 100;       // VARCHAR(100)
    private static final int HASH_MAX = 255;        // VARCHAR(255)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario") // El campo se llama "id", la columna "id_usuario"
    private Long id;

    @Column(nullable = false, unique = true, length = EMAIL_MAX)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING) // Guarda "ADMIN"/"ALUMNO" y no un número
    @Column(nullable = false)
    private Rol rol;

    public Usuario(String email, String passwordHash, Rol rol) {
        this.email = validarEmail(email);
        this.passwordHash = validarPasswordHash(passwordHash);
        this.rol = validarRol(rol);
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
        this.email = validarEmail(email);
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = validarPasswordHash(passwordHash);
    }

    public void setRol(Rol rol) {
        this.rol = validarRol(rol);
    }

    private static String validarEmail(String email) {
        String limpio = Validaciones.texto(email, EMAIL_MAX, "El email");
        if (!limpio.contains("@")) {
            throw new IllegalArgumentException("El email ingresado no es válido.");
        }
        return limpio;
    }

    private static String validarPasswordHash(String passwordHash) {
        return Validaciones.texto(passwordHash, HASH_MAX, "El hash de la contraseña");
    }

    private static Rol validarRol(Rol rol) {
        return Validaciones.requerido(rol, "El usuario debe tener un rol.");
    }
}
