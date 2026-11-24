package com.LOTUS.jteducation.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.LOTUS.jteducation.dto.AlumnoForm;
import com.LOTUS.jteducation.entity.Alumno;
import com.LOTUS.jteducation.entity.Curso;
import com.LOTUS.jteducation.entity.Division;
import com.LOTUS.jteducation.entity.Domicilio;
import com.LOTUS.jteducation.entity.Rol;
import com.LOTUS.jteducation.entity.Usuario;
import com.LOTUS.jteducation.exception.ReglaDeNegocioException;
import com.LOTUS.jteducation.repository.AlumnoMateriaRepository;
import com.LOTUS.jteducation.repository.AlumnoRepository;
import com.LOTUS.jteducation.repository.CursoRepository;
import com.LOTUS.jteducation.repository.UsuarioRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AlumnoServiceTest {

    @Mock private AlumnoRepository alumnoRepository;
    @Mock private CursoRepository cursoRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private AlumnoMateriaRepository alumnoMateriaRepository;

    private AlumnoService servicio;

    @BeforeEach
    void preparar() {
        servicio = new AlumnoService(alumnoRepository, cursoRepository, usuarioRepository,
                alumnoMateriaRepository, new BCryptPasswordEncoder());
    }

    // Los ids los genera la base; en un test sin base se asignan con ReflectionTestUtils (no hay setId a propósito)
    private Curso curso(long id, int anio) {
        Curso curso = new Curso(anio, Division.A, 2026);
        ReflectionTestUtils.setField(curso, "idCurso", id);
        return curso;
    }

    private AlumnoForm form(String password, long idCurso) {
        AlumnoForm form = new AlumnoForm();
        form.setDni("40000000");
        form.setNombre("Lucía");
        form.setApellido("Fernández");
        form.setFechaNacimiento(LocalDate.of(2012, 1, 1));
        form.setFechaInscripcion(LocalDate.of(2026, 3, 1));
        form.setCiudad("Goya");
        form.setCalle("San Martín");
        form.setAltura(100);
        form.setEmail("lucia@mail.com");
        form.setPassword(password);
        form.setIdCurso(idCurso);
        return form;
    }

    @Test
    void noCreaUnAlumnoConContrasenaCorta() {
        assertThatThrownBy(() -> servicio.crear(form("1234567", 1L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("8");
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void noCreaUnAlumnoConDniRepetido() {
        Alumno otro = new Alumno("40000000", "Otro", "Alumno", LocalDate.of(2012, 1, 1), LocalDate.of(2026, 3, 1),
                new Domicilio("Goya", "Mitre", 1), new Usuario("otro@mail.com", "hash", Rol.ALUMNO), curso(1L, 1));
        ReflectionTestUtils.setField(otro, "idAlumno", 9L);
        when(alumnoRepository.findByDni("40000000")).thenReturn(Optional.of(otro));

        assertThatThrownBy(() -> servicio.crear(form("Clave1234", 1L)))
                .isInstanceOf(ReglaDeNegocioException.class)
                .hasMessageContaining("DNI");
    }

    @Test
    void noCambiaDeCursoAUnAlumnoConNotas() {
        Usuario usuario = new Usuario("lucia@mail.com", "hash", Rol.ALUMNO);
        ReflectionTestUtils.setField(usuario, "id", 2L);
        Alumno lucia = new Alumno("40000000", "Lucía", "Fernández", LocalDate.of(2012, 1, 1),
                LocalDate.of(2026, 3, 1), new Domicilio("Goya", "San Martín", 100), usuario, curso(1L, 1));
        ReflectionTestUtils.setField(lucia, "idAlumno", 1L);

        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(lucia));
        when(alumnoRepository.findByDni("40000000")).thenReturn(Optional.of(lucia));
        when(usuarioRepository.findByEmail("lucia@mail.com")).thenReturn(Optional.of(usuario));
        when(cursoRepository.findById(2L)).thenReturn(Optional.of(curso(2L, 2)));
        when(alumnoMateriaRepository.existsByAlumno_IdAlumno(1L)).thenReturn(true); // Tiene notas

        assertThatThrownBy(() -> servicio.actualizar(1L, form("", 2L)))
                .isInstanceOf(ReglaDeNegocioException.class)
                .hasMessageContaining("cambiar de curso");
        verify(alumnoRepository, never()).saveAndFlush(any());
    }
}
