package com.LOTUS.jteducation.repository;

import com.LOTUS.jteducation.entity.Alumno;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// save, findById, findAll, deleteById, etc. ya vienen heredados: NO hace falta redeclararlos.
@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    // Query derivada: SELECT ... FROM alumno WHERE dni = ?
    Optional<Alumno> findByDni(String dni);

    // "usuario.email": navega la relación 1:1 con Usuario. Lo usa "Mi ficha" para encontrar al alumno logueado
    Optional<Alumno> findByUsuario_Email(String email);

    // "curso.idCurso": navega la relación y filtra por el id del curso
    List<Alumno> findByCurso_IdCurso(Long idCurso, Sort orden);
}
