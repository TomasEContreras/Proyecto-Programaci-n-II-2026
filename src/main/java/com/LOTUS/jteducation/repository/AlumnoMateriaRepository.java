package com.LOTUS.jteducation.repository;

import com.LOTUS.jteducation.entity.AlumnoMateria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlumnoMateriaRepository extends JpaRepository<AlumnoMateria, Long> {

    // "alumno.idAlumno": navega la relación y filtra por el id del alumno
    List<AlumnoMateria> findByAlumno_IdAlumno(Long idAlumno);

    // ¿El alumno tiene alguna nota cargada? (exists es más liviano que traer la lista)
    boolean existsByAlumno_IdAlumno(Long idAlumno);

    // ¿Algún alumno de ESTE curso tiene notas en ESTA materia? (alumno.curso.idCurso + materia.idMateria)
    boolean existsByAlumno_Curso_IdCursoAndMateria_IdMateria(Long idCurso, Long idMateria);
}
