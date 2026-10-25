package com.LOTUS.jteducation.service;

import com.LOTUS.jteducation.dto.AlumnoForm;
import com.LOTUS.jteducation.entity.Alumno;
import com.LOTUS.jteducation.entity.Curso;
import com.LOTUS.jteducation.entity.Domicilio;
import com.LOTUS.jteducation.entity.Rol;
import com.LOTUS.jteducation.entity.Usuario;
import com.LOTUS.jteducation.exception.RecursoNoEncontradoException;
import com.LOTUS.jteducation.exception.ReglaDeNegocioException;
import com.LOTUS.jteducation.repository.AlumnoMateriaRepository;
import com.LOTUS.jteducation.repository.AlumnoRepository;
import com.LOTUS.jteducation.repository.CursoRepository;
import com.LOTUS.jteducation.repository.UsuarioRepository;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlumnoService {

    private static final String MSJ_DUPLICADO = "Ya existe un alumno con ese DNI o una cuenta con ese email.";

    private final AlumnoRepository alumnoRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlumnoMateriaRepository alumnoMateriaRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional(readOnly = true)
    public List<Alumno> obtenerTodos() { return alumnoRepository.findAll(); }

    @Transactional(readOnly = true)
    public Page<Alumno> obtenerAlumnos(int page, int size) {
        return obtenerAlumnos(PageRequest.of(page, size, Sort.by("idAlumno").descending()));
    }

    @Transactional(readOnly = true)
    public Page<Alumno> obtenerAlumnos(Pageable pageable) { return alumnoRepository.findAll(pageable); }

    @Transactional(readOnly = true)
    public List<Alumno> obtenerPorCurso(Long idCurso) {
        return alumnoRepository.findByCurso_IdCurso(idCurso, Sort.by("apellido", "nombre"));
    }

    @Transactional(readOnly = true)
    public Alumno buscarPorId(Long id) {
        return alumnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el alumno con id " + id));
    }

    @Transactional
    public Alumno crear(AlumnoForm form) {
        String dni = normalizar(form.getDni());
        String email = normalizar(form.getEmail()).toLowerCase(Locale.ROOT);
        // La regla de la contraseña se valida en texto plano, ANTES de hashearla (el hash siempre mide lo mismo)
        Usuario.validarPasswordPlana(form.getPassword());
        validarUnicos(dni, email, null);
        Curso curso = buscarCurso(form.getIdCurso());
        try {
            Usuario usuario = usuarioRepository.save(new Usuario(email,
                    passwordEncoder.encode(form.getPassword()), Rol.ALUMNO));
            Domicilio domicilio = new Domicilio(form.getCiudad(), form.getCalle(), form.getAltura());
            Alumno alumno = new Alumno(dni, form.getNombre(), form.getApellido(), form.getFechaNacimiento(),
                    form.getFechaInscripcion(), domicilio, usuario, curso);
            return alumnoRepository.saveAndFlush(alumno);
        } catch (DataIntegrityViolationException e) {
            // Dos altas simultáneas con el mismo DNI/email pasan validarUnicos; la base (UNIQUE) frena la segunda
            throw new ReglaDeNegocioException(MSJ_DUPLICADO);
        }
    }

    @Transactional
    public Alumno actualizar(Long id, AlumnoForm form) {
        Alumno alumno = buscarPorId(id);
        String dni = normalizar(form.getDni());
        String email = normalizar(form.getEmail()).toLowerCase(Locale.ROOT);
        validarUnicos(dni, email, alumno);
        Curso curso = buscarCurso(form.getIdCurso());
        validarCambioDeCurso(alumno, curso);

        alumno.setDni(dni);
        alumno.setNombre(form.getNombre());
        alumno.setApellido(form.getApellido());
        alumno.setFechaNacimiento(form.getFechaNacimiento());
        alumno.setFechaInscripcion(form.getFechaInscripcion());
        alumno.setCurso(curso);
        alumno.getDomicilio().setCiudad(form.getCiudad());
        alumno.getDomicilio().setCalle(form.getCalle());
        alumno.getDomicilio().setAltura(form.getAltura());
        alumno.getUsuario().setEmail(email);
        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            Usuario.validarPasswordPlana(form.getPassword());
            alumno.getUsuario().setPasswordHash(passwordEncoder.encode(form.getPassword()));
        }
        try {
            return alumnoRepository.saveAndFlush(alumno); // flush: el UPDATE se ejecuta acá y se puede capturar
        } catch (DataIntegrityViolationException e) {
            throw new ReglaDeNegocioException(MSJ_DUPLICADO);
        }
    }

    // "actual" es el alumno que se edita (null al crear): su propio DNI y email no cuentan como repetidos
    private void validarUnicos(String dni, String email, Alumno actual) {
        alumnoRepository.findByDni(dni)
                .filter(a -> actual == null || !a.getIdAlumno().equals(actual.getIdAlumno()))
                .ifPresent(a -> { throw new ReglaDeNegocioException("Ya existe un alumno con el DNI " + dni + "."); });
        usuarioRepository.findByEmail(email)
                .filter(u -> actual == null || !u.getId().equals(actual.getUsuario().getId()))
                .ifPresent(u -> { throw new ReglaDeNegocioException("Ya existe una cuenta con ese email."); });
    }

    // Si otra persona borró el curso mientras se completaba el formulario, se avisa en el mismo formulario
    private Curso buscarCurso(Long idCurso) {
        return cursoRepository.findById(idCurso)
                .orElseThrow(() -> new ReglaDeNegocioException("El curso seleccionado ya no existe. Elegí otro."));
    }

    // Regla de negocio: las notas solo pueden ser de materias del curso del alumno.
    // Si ya tiene notas, cambiarlo de curso dejaría notas de materias que el curso nuevo quizás no tiene.
    private void validarCambioDeCurso(Alumno alumno, Curso cursoNuevo) {
        boolean cambiaDeCurso = !alumno.getCurso().getIdCurso().equals(cursoNuevo.getIdCurso());
        if (cambiaDeCurso && alumnoMateriaRepository.existsByAlumno_IdAlumno(alumno.getIdAlumno())) {
            throw new ReglaDeNegocioException(
                    "No se puede cambiar de curso a un alumno que ya tiene notas cargadas.");
        }
    }

    @Transactional
    public void eliminar(Long id) {
        Alumno alumno = buscarPorId(id);
        try {
            alumnoRepository.delete(alumno);
            alumnoRepository.flush();
            // El domicilio tiene ciclo de vida propio del alumno; la cuenta se elimina explícitamente.
            usuarioRepository.delete(alumno.getUsuario());
            usuarioRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ReglaDeNegocioException("No se puede eliminar el alumno porque tiene notas asociadas.");
        }
    }

    private String normalizar(String valor) { return valor == null ? "" : valor.trim(); }
}
