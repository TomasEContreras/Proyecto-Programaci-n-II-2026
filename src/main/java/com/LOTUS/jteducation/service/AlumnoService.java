package com.LOTUS.jteducation.service;

import com.LOTUS.jteducation.entity.Alumno;
import com.LOTUS.jteducation.exception.RecursoNoEncontradoException;
import com.LOTUS.jteducation.exception.ReglaDeNegocioException;
import com.LOTUS.jteducation.repository.AlumnoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;

    @Transactional(readOnly = true)
    public List<Alumno> obtenerTodos() {
        return alumnoRepository.findAll();
    }

    // NUEVO (apunte: "Paginado de recetas"): arma el Pageable y pide una sola página.
    // Para la home: los últimos alumnos inscriptos (id descendente).
    @Transactional(readOnly = true)
    public Page<Alumno> obtenerAlumnos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("idAlumno").descending());
        return obtenerAlumnos(pageable);
    }

    // NUEVO: sobrecarga que recibe directamente el Pageable (lo resuelve Spring desde ?page=X&size=Y)
    @Transactional(readOnly = true)
    public Page<Alumno> obtenerAlumnos(Pageable pageable) {
        return alumnoRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Alumno buscarPorId(Long id) {
        return alumnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el alumno con id " + id));
    }

    @Transactional
    public Alumno guardar(Alumno alumno) {
        alumnoRepository.findByDni(alumno.getDni())
                .filter(existente -> !existente.getIdAlumno().equals(alumno.getIdAlumno()))
                .ifPresent(existente -> {
                    throw new ReglaDeNegocioException("Ya existe un alumno con el DNI " + alumno.getDni());
                });
        return alumnoRepository.save(alumno);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!alumnoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("No existe el alumno con id " + id);
        }
        try {
            alumnoRepository.deleteById(id);
            alumnoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ReglaDeNegocioException("No se puede eliminar el alumno porque tiene notas asociadas.");
        }
    }
}
