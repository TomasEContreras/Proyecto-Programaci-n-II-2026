
package com.LOTUS.jteducation.repository;

import com.LOTUS.jteducation.entity.Materia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


// Esto nos dará acceso a todos los métodos CRUD para la entidad sin escribir código.
@Repository 
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    
    // CREATE - UPDATE
    @Override
    <S extends Materia> S save(S entity);

    @Override
    Optional<Materia> findById(Long id);

    @Override
    List<Materia> findAll();
    
    // DELETE
    @Override
    void deleteById(Long id);
    
}
