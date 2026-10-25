package com.LOTUS.jteducation.service;

import com.LOTUS.jteducation.dto.MateriaConNotas;
import com.LOTUS.jteducation.entity.Alumno;
import com.LOTUS.jteducation.entity.AlumnoMateria;
import com.LOTUS.jteducation.repository.AlumnoMateriaRepository;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlumnoMateriaService {

    private final AlumnoMateriaRepository alumnoMateriaRepository;

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
}
