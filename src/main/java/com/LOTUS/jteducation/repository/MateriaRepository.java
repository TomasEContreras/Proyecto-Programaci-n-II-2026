package com.LOTUS.jteducation.repository;

import com.LOTUS.jteducation.entity.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    boolean existsByNombreIgnoreCase(String nombre); // Sirve para crear, y ignora mayúsculas (“Historia” = “historia”).
    
    boolean existsByNombreIgnoreCaseAndIdMateriaNot(String nombre, Long idMateria); // Sirve para editar: busca otra materia con ese nombre, excluyendo la que estás editando.
    
}
