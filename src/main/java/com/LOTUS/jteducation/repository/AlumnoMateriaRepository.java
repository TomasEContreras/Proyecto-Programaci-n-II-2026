package com.LOTUS.jteducation.repository;

import com.LOTUS.jteducation.entity.AlumnoMateria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlumnoMateriaRepository extends JpaRepository<AlumnoMateria, Long> {

    // "alumno.idAlumno": navega la relación y filtra por el id del alumno
    List<AlumnoMateria> findByAlumno_IdAlumno(Long idAlumno);

    // Las notas de UN alumno en UNA materia (como mucho una fila: hay un UNIQUE alumno + materia)
    Optional<AlumnoMateria> findByAlumno_IdAlumnoAndMateria_IdMateria(Long idAlumno, Long idMateria);

    // ¿El alumno tiene alguna nota cargada? (exists es más liviano que traer la lista)
    boolean existsByAlumno_IdAlumno(Long idAlumno);

    // ¿Algún alumno de ESTE curso tiene notas en ESTA materia? (alumno.curso.idCurso + materia.idMateria)
    boolean existsByAlumno_Curso_IdCursoAndMateria_IdMateria(Long idCurso, Long idMateria);
}
