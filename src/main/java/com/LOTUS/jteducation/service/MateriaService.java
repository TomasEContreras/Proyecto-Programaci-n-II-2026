
package com.LOTUS.jteducation.service;

import com.LOTUS.jteducation.entity.Materia;
import com.LOTUS.jteducation.repository.MateriaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MateriaService {
    
    @Autowired // Para que el servicio pueda comunicarse con la base de datos.

    private MateriaRepository materiaRepository;
    
    public List<Materia> obtenerTodasLasMaterias() {
        return materiaRepository.findAll();
    }
    
    public Optional<Materia> borrarMateriaPorId(Long Id) {
        Optional<Materia> Borrado = buscarMateriaPorId(Id);
        materiaRepository.deleteById(Id);
        return Borrado;
    }
    
    public Optional<Materia> buscarMateriaPorId(Long Id) {
        return materiaRepository.findById(Id);
    }
    
    public Materia agregarMateria(Materia materia) {
        return materiaRepository.save(materia);
    }
    
}
