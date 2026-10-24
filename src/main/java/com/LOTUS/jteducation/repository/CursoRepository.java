package com.LOTUS.jteducation.repository;

import com.LOTUS.jteducation.entity.Curso;
import com.LOTUS.jteducation.entity.Division;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    // Ciclo más reciente primero, luego año y división
    List<Curso> findAllByOrderByCicloLectivoDescAnioAscDivisionAsc();

    // Sirve para crear: ¿ya existe esa combinación de año + división + ciclo lectivo?
    boolean existsByAnioAndDivisionAndCicloLectivo(int anio, Division division, int cicloLectivo);

    // Sirve para editar: busca OTRO curso con esa combinación, excluyendo el que se está editando
    boolean existsByAnioAndDivisionAndCicloLectivoAndIdCursoNot(int anio, Division division, int cicloLectivo, Long idCurso);
}
