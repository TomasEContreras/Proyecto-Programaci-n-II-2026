package com.LOTUS.jteducation.entity;

import com.LOTUS.jteducation.util.Validaciones;
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

    private static final String DNI_REGEX = "\\d{7,8}"; // DNI argentino: 7 u 8 dígitos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlumno; // camelCase en Java; Spring lo traduce solo a la columna id_alumno

    private String dni;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;    // -> fecha_nacimiento
    private LocalDate fechaInscripcion;   // -> fecha_inscripcion

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
        this.dni = validarDni(dni);
        this.nombre = validarNombre(nombre);
        this.apellido = validarApellido(apellido);
        this.fechaNacimiento = validarFechaNacimiento(fechaNacimiento);
        this.fechaInscripcion = validarFechaInscripcion(fechaInscripcion);
        this.domicilio = validarDomicilio(domicilio);
        this.usuario = validarUsuario(usuario);
        this.curso = validarCurso(curso);
    }

    public void setDni(String dni) {
        this.dni = validarDni(dni);
    }

    public void setNombre(String nombre) {
        this.nombre = validarNombre(nombre);
    }

    public void setApellido(String apellido) {
        this.apellido = validarApellido(apellido);
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = validarFechaNacimiento(fechaNacimiento);
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = validarFechaInscripcion(fechaInscripcion);
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = validarDomicilio(domicilio);
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = validarUsuario(usuario);
    }

    public void setCurso(Curso curso) {
        this.curso = validarCurso(curso);
    }

    // ===== Validaciones privadas: las comparten el constructor y los setters =====

    private static String validarDni(String dni) {
        String limpio = (dni == null) ? "" : dni.trim();
        if (!limpio.matches(DNI_REGEX)) {
            throw new IllegalArgumentException("El DNI debe tener 7 u 8 dígitos.");
        }
        return limpio;
    }

    private static String validarNombre(String nombre) {
        return Validaciones.texto(nombre, Validaciones.TEXTO_MAX, "El nombre del alumno");
    }

    private static String validarApellido(String apellido) {
        return Validaciones.texto(apellido, Validaciones.TEXTO_MAX, "El apellido del alumno");
    }

    private static LocalDate validarFechaNacimiento(LocalDate fecha) {
        if (fecha == null || !fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento debe ser anterior a hoy.");
        }
        return fecha;
    }

    private static LocalDate validarFechaInscripcion(LocalDate fecha) {
        return Validaciones.requerido(fecha, "La fecha de inscripción no puede estar vacía.");
    }

    private static Domicilio validarDomicilio(Domicilio domicilio) {
        return Validaciones.requerido(domicilio, "El alumno debe tener un domicilio.");
    }

    private static Usuario validarUsuario(Usuario usuario) {
        return Validaciones.requerido(usuario, "El alumno debe tener un usuario.");
    }

    private static Curso validarCurso(Curso curso) {
        return Validaciones.requerido(curso, "El alumno debe pertenecer a un curso.");
    }
}
