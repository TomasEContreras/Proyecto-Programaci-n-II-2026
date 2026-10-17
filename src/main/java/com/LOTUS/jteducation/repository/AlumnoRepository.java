
package com.LOTUS.jteducation.repository;

import com.LOTUS.jteducation.entity.Alumno;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Esto nos dará acceso a todos los métodos CRUD para la entidad Alumno sin escribir código.
@Repository 
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    
    // CREATE - UPDATE
    @Override
    <S extends Alumno> S save(S entity);

    @Override
    Optional<Alumno> findById(Long id);

    @Override
    List<Alumno> findAll();
    
    // DELETE
    @Override
    void deleteById(Long id);
    
}

// PAG 26-27