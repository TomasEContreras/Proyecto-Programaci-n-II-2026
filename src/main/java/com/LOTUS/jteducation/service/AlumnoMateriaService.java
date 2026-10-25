package com.LOTUS.jteducation.service;

import com.LOTUS.jteducation.dto.MateriaConNotas;
import com.LOTUS.jteducation.dto.NotaForm;
import com.LOTUS.jteducation.entity.Alumno;
import com.LOTUS.jteducation.entity.AlumnoMateria;
import com.LOTUS.jteducation.entity.Materia;
import com.LOTUS.jteducation.exception.ReglaDeNegocioException;
import com.LOTUS.jteducation.repository.AlumnoMateriaRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlumnoMateriaService {

    private final AlumnoMateriaRepository alumnoMateriaRepository;
    private final AlumnoService alumnoService;
    private final MateriaService materiaService;

    // Las materias del alumno son las del PLAN DE SU CURSO; a cada una se le agregan sus notas, si tiene.
    // Así aparecen también las materias sin notas cargadas todavía.
    @Transactional(readOnly = true)
    public List<MateriaConNotas> obtenerPlanConNotas(Alumno alumno) {
        // Una sola consulta para todas las notas; el Map permite encontrar la de cada materia por su id
        Map<Long, AlumnoMateria> notasPorMateria = alumnoMateriaRepository
                .findByAlumno_IdAlumno(alumno.getIdAlumno()).stream()
                .collect(Collectors.toMap(n -> n.getMateria().getIdMateria(), Function.identity()));

        return alumno.getCurso().getMaterias().stream()
                .map(materia -> new MateriaConNotas(materia, notasPorMateria.get(materia.getIdMateria())))
                .toList();
    }

    // Regla de negocio: solo se cargan notas de materias del plan del curso del alumno
    @Transactional(readOnly = true)
    public Materia buscarMateriaDelPlan(Alumno alumno, Long idMateria) {
        Materia materia = materiaService.buscarPorId(idMateria); // 404 si la materia no existe
        boolean esDelPlan = alumno.getCurso().getMaterias().stream()
                .anyMatch(m -> m.getIdMateria().equals(idMateria));
        if (!esDelPlan) {
            throw new ReglaDeNegocioException(materia.getNombre() + " no forma parte del plan del curso del alumno.");
        }
        return materia;
    }

    // Optional: puede no haber notas todavía; el que llama decide qué hacer en ese caso
    @Transactional(readOnly = true)
    public Optional<AlumnoMateria> buscarNotas(Long idAlumno, Long idMateria) {
        return alumnoMateriaRepository.findByAlumno_IdAlumnoAndMateria_IdMateria(idAlumno, idMateria);
    }

    // Crea la fila de notas si no existía, o la actualiza.
    // Si se dejan las tres notas vacías, se borra la fila: el alumno queda "sin notas" en esa materia.
    @Transactional
    public void guardarNotas(Long idAlumno, Long idMateria, NotaForm form) {
        Alumno alumno = alumnoService.buscarPorId(idAlumno);
        Materia materia = buscarMateriaDelPlan(alumno, idMateria);
        Optional<AlumnoMateria> existentes = buscarNotas(idAlumno, idMateria);

        if (form.estaVacio()) {
            existentes.ifPresent(alumnoMateriaRepository::delete);
            return;
        }
        AlumnoMateria notas = existentes.orElseGet(() -> new AlumnoMateria(alumno, materia));
        // Los setters de la entidad vuelven a validar el rango 1-10 (por si se llama sin pasar por el formulario)
        notas.setPrimerNota(form.getPrimerNota());
        notas.setSegundaNota(form.getSegundaNota());
        notas.setNotaFinal(form.getNotaFinal());
        alumnoMateriaRepository.save(notas);
    }
}
