package com.LOTUS.jteducation.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlumno; // camelCase en Java; Spring lo traduce solo a la columna id_alumno

    private String dni;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;    // -> fecha_nacimiento
    private LocalDate fechaInscripcion;   // -> fecha_inscripcion (NOT NULL en la BD, antes faltaba)

    // RELACIÓN 1:1. cascade = ALL: al guardar/borrar el alumno, se guarda/borra su domicilio.
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "domicilio_id_domicilio", nullable = false)
    private Domicilio domicilio;

    // RELACIÓN 1:1. Sin cascade: el usuario se crea primero (con su rol y su contraseña hasheada).
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id_usuario", nullable = false)
    private Usuario usuario;

    // RELACIÓN 1:N. Muchos alumnos pertenecen a un curso.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "curso_id_curso", nullable = false)
    private Curso curso;

    public Alumno(String dni, String nombre, String apellido, LocalDate fechaNacimiento,
            LocalDate fechaInscripcion, Domicilio domicilio, Usuario usuario, Curso curso) {
        setDni(dni);
        setNombre(nombre);
        setApellido(apellido);
        setFechaNacimiento(fechaNacimiento);
        setFechaInscripcion(fechaInscripcion);
        setDomicilio(domicilio);
        setUsuario(usuario);
        setCurso(curso);
    }

    public void setDni(String dni) {
        if (dni == null || !dni.trim().matches("\\d{7,8}")) { // DNI argentino: 7 u 8 dígitos
            throw new IllegalArgumentException("El DNI debe tener 7 u 8 dígitos.");
        }
        this.dni = dni.trim();
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del alumno no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido del alumno no puede estar vacío.");
        }
        this.apellido = apellido.trim();
    }

    // Se llamaba setFechaNacimiento pero el campo era fecha_nacimiento: no coincidían.
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null || !fechaNacimiento.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento debe ser anterior a hoy.");
        }
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        if (fechaInscripcion == null) {
            throw new IllegalArgumentException("La fecha de inscripción no puede estar vacía.");
        }
        this.fechaInscripcion = fechaInscripcion;
    }

    public void setDomicilio(Domicilio domicilio) {
        if (domicilio == null) {
            throw new IllegalArgumentException("El alumno debe tener un domicilio.");
        }
        this.domicilio = domicilio;
    }

    public void setUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El alumno debe tener un usuario.");
        }
        this.usuario = usuario;
    }

    public void setCurso(Curso curso) {
        if (curso == null) {
            throw new IllegalArgumentException("El alumno debe pertenecer a un curso.");
        }
        this.curso = curso;
    }
}
