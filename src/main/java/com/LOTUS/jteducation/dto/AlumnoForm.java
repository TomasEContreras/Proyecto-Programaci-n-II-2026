package com.LOTUS.jteducation.dto;

import com.LOTUS.jteducation.entity.Alumno;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/** Datos de entrada del formulario; no se enlaza directamente el formulario con una entidad JPA. */
@Getter
@Setter
@NoArgsConstructor
public class AlumnoForm {
    @NotBlank(message = "El DNI es obligatorio")
    @jakarta.validation.constraints.Pattern(regexp = "\\d{7,8}", message = "El DNI debe tener 7 u 8 dígitos")
    private String dni;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 45, message = "El nombre no puede superar los 45 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 45, message = "El apellido no puede superar los 45 caracteres")
    private String apellido;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    // ISO (yyyy-MM-dd): es el único formato que entiende <input type="date">. Sin esto, Spring usa el
    // formato del idioma ("1/1/12") y al editar el campo aparece vacío.
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaNacimiento;

    @NotNull(message = "La fecha de inscripción es obligatoria")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaInscripcion;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 45, message = "La ciudad no puede superar los 45 caracteres")
    private String ciudad;

    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 45, message = "La calle no puede superar los 45 caracteres")
    private String calle;

    @NotNull(message = "La altura es obligatoria")
    @Positive(message = "La altura debe ser mayor que cero")
    private Integer altura;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Ingresá un email válido")
    @Size(max = 100, message = "El email no puede superar los 100 caracteres")
    private String email;

    // Solo se exige al crear. En edición, dejarlo vacío conserva la contraseña actual.
    private String password;

    @NotNull(message = "Seleccioná un curso")
    private Long idCurso;

    public static AlumnoForm desde(Alumno alumno) {
        AlumnoForm form = new AlumnoForm();
        form.setDni(alumno.getDni());
        form.setNombre(alumno.getNombre());
        form.setApellido(alumno.getApellido());
        form.setFechaNacimiento(alumno.getFechaNacimiento());
        form.setFechaInscripcion(alumno.getFechaInscripcion());
        form.setCiudad(alumno.getDomicilio().getCiudad());
        form.setCalle(alumno.getDomicilio().getCalle());
        form.setAltura(alumno.getDomicilio().getAltura());
        form.setEmail(alumno.getUsuario().getEmail());
        form.setIdCurso(alumno.getCurso().getIdCurso());
        return form;
    }
}
