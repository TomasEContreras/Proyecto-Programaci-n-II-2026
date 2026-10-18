package com.LOTUS.jteducation.service;

import com.LOTUS.jteducation.entity.AlumnoMateria;
import com.LOTUS.jteducation.repository.AlumnoMateriaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlumnoMateriaService {

    private final AlumnoMateriaRepository alumnoMateriaRepository;

    // Materias de un alumno con sus notas (la relación N:M)
    @Transactional(readOnly = true)
    public List<AlumnoMateria> obtenerPorAlumno(Long idAlumno) {
        return alumnoMateriaRepository.findByAlumno_IdAlumno(idAlumno);
    }
}
