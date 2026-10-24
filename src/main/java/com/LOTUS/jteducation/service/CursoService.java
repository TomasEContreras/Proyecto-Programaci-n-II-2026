package com.LOTUS.jteducation.service;

import com.LOTUS.jteducation.entity.Curso;
import com.LOTUS.jteducation.entity.Division;
import com.LOTUS.jteducation.exception.RecursoNoEncontradoException;
import com.LOTUS.jteducation.exception.ReglaDeNegocioException;
import com.LOTUS.jteducation.repository.CursoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CursoService {

    private static final String MSJ_REPETIDO = "Ya existe un curso con ese año, división y ciclo lectivo.";

    private final CursoRepository cursoRepository;

    // Ciclo más reciente primero, luego año y división, para que el listado sea predecible
    @Transactional(readOnly = true)
    public List<Curso> obtenerTodos() {
        return cursoRepository.findAllByOrderByCicloLectivoDescAnioAscDivisionAsc();
    }

    @Transactional(readOnly = true)
    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el curso con id " + id));
    }

    // Regla de negocio: no se repite la combinación año + división + ciclo lectivo
    @Transactional
    public Curso crear(int anio, Division division, int cicloLectivo) {
        if (cursoRepository.existsByAnioAndDivisionAndCicloLectivo(anio, division, cicloLectivo)) {
            throw new ReglaDeNegocioException(MSJ_REPETIDO);
        }
        return cursoRepository.save(new Curso(anio, division, cicloLectivo));
    }

    // Al editar se excluye el propio curso, para poder guardarlo sin cambiar nada
    @Transactional
    public Curso actualizar(Long id, int anio, Division division, int cicloLectivo) {
        Curso curso = buscarPorId(id);
        if (cursoRepository.existsByAnioAndDivisionAndCicloLectivoAndIdCursoNot(anio, division, cicloLectivo, id)) {
            throw new ReglaDeNegocioException(MSJ_REPETIDO);
        }
        curso.setAnio(anio);
        curso.setDivision(division);
        curso.setCicloLectivo(cicloLectivo);
        return cursoRepository.save(curso);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("No existe el curso con id " + id);
        }
        try {
            cursoRepository.deleteById(id);
            cursoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ReglaDeNegocioException("No se puede eliminar el curso porque tiene alumnos asociados.");
        }
    }

}
