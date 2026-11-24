package com.LOTUS.jteducation.repository;

import com.LOTUS.jteducation.entity.Alumno;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// save, findById, findAll, deleteById, etc. ya vienen heredados: NO hace falta redeclararlos.
@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    // Query derivada: SELECT ... FROM alumno WHERE dni = ?
    Optional<Alumno> findByDni(String dni);

    // Buscador del listado. Con @Query se escribe la consulta en JPQL: se parece a SQL pero usa las
    // clases y atributos de Java (Alumno, apellido), no las tablas. ":texto" es un parámetro: Spring lo
    // reemplaza de forma segura, así que no hay riesgo de inyección SQL.
    @Query("SELECT a FROM Alumno a WHERE LOWER(a.apellido) LIKE LOWER(CONCAT('%', :texto, '%'))"
            + " OR LOWER(a.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))"
            + " OR a.dni LIKE CONCAT('%', :texto, '%')")
    Page<Alumno> buscar(@Param("texto") String texto, Pageable pageable);

    // "usuario.email": navega la relación 1:1 con Usuario. Lo usa "Mi ficha" para encontrar al alumno logueado
    Optional<Alumno> findByUsuario_Email(String email);

    // "curso.idCurso": navega la relación y filtra por el id del curso
    List<Alumno> findByCurso_IdCurso(Long idCurso, Sort orden);
}
