package com.LOTUS.jteducation.service;

import com.LOTUS.jteducation.entity.Curso;
import com.LOTUS.jteducation.entity.Division;
import com.LOTUS.jteducation.entity.Materia;
import com.LOTUS.jteducation.exception.RecursoNoEncontradoException;
import com.LOTUS.jteducation.exception.ReglaDeNegocioException;
import com.LOTUS.jteducation.repository.AlumnoMateriaRepository;
import com.LOTUS.jteducation.repository.CursoRepository;
import com.LOTUS.jteducation.repository.MateriaRepository;
import java.time.Year;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CursoService {

    private static final String MSJ_REPETIDO = "Ya existe un curso con ese año, división y ciclo lectivo.";

    private final CursoRepository cursoRepository;
    private final MateriaRepository materiaRepository;
    private final AlumnoMateriaRepository alumnoMateriaRepository;

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

    // Regla de negocio: el ciclo lectivo lo asigna el sistema (año en curso), no el usuario.
    // Regla de negocio: no se repite la combinación año + división + ciclo lectivo
    @Transactional
    public Curso crear(int anio, Division division, Set<Long> idsMaterias) {
        int cicloLectivo = Year.now().getValue();
        if (cursoRepository.existsByAnioAndDivisionAndCicloLectivo(anio, division, cicloLectivo)) {
            throw new ReglaDeNegocioException(MSJ_REPETIDO);
        }
        Curso curso = new Curso(anio, division, cicloLectivo);
        curso.setMaterias(buscarMaterias(idsMaterias));
        return cursoRepository.save(curso);
    }

    // Al editar, el curso conserva su ciclo lectivo original (un 1° A de 2025 sigue siendo de 2025).
    // Se excluye el propio curso, para poder guardarlo sin cambiar nada
    @Transactional
    public Curso actualizar(Long id, int anio, Division division, Set<Long> idsMaterias) {
        Curso curso = buscarPorId(id);
        if (cursoRepository.existsByAnioAndDivisionAndCicloLectivoAndIdCursoNot(
                anio, division, curso.getCicloLectivo(), id)) {
            throw new ReglaDeNegocioException(MSJ_REPETIDO);
        }
        validarMateriasQuitadas(curso, idsMaterias);
        curso.setAnio(anio);
        curso.setDivision(division);
        curso.setMaterias(buscarMaterias(idsMaterias));
        return cursoRepository.save(curso);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("No existe el curso con id " + id);
        }
        try {
            // Hibernate borra solo las filas de curso_materia: el curso es el dueño de la relación
            cursoRepository.deleteById(id);
            cursoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ReglaDeNegocioException("No se puede eliminar el curso porque tiene alumnos asociados.");
        }
    }

    // Si se pidió un id que ya no existe (por ejemplo, otra persona borró la materia), se avisa en el formulario
    private List<Materia> buscarMaterias(Set<Long> idsMaterias) {
        List<Materia> materias = materiaRepository.findAllById(idsMaterias);
        if (materias.size() != idsMaterias.size()) {
            throw new ReglaDeNegocioException("Alguna de las materias elegidas ya no existe. Volvé a elegirlas.");
        }
        return materias;
    }

    // Regla de negocio: las notas solo pueden ser de materias del curso del alumno.
    // Por eso no se puede sacar del plan una materia en la que algún alumno del curso ya tiene notas.
    // Se compara por id: Materia no define equals, y el id es lo que llega del formulario.
    private void validarMateriasQuitadas(Curso curso, Set<Long> idsNuevos) {
        for (Materia actual : curso.getMaterias()) {
            boolean seQuita = !idsNuevos.contains(actual.getIdMateria());
            if (seQuita && alumnoMateriaRepository.existsByAlumno_Curso_IdCursoAndMateria_IdMateria(
                    curso.getIdCurso(), actual.getIdMateria())) {
                throw new ReglaDeNegocioException("No se puede quitar " + actual.getNombre()
                        + " del curso porque hay alumnos con notas en esa materia.");
            }
        }
    }
}
