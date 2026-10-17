
package com.LOTUS.jteducation.service;

import com.LOTUS.jteducation.entity.Alumno;
import com.LOTUS.jteducation.repository.AlumnoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // Le dice a Spring que esta clase es un componente de servicio
public class AlumnoService {
    
    @Autowired // Para que el servicio pueda comunicarse con la base de datos.

    private AlumnoRepository alumnoRepository;
    
    public List<Alumno> obtenerTodosLosAlumnos() {
        return alumnoRepository.findAll();
    }
    
    public Optional<Alumno> borrarAlumnoPorId(Long Id) {
        Optional<Alumno> Borrado = buscarAlumnoPorId(Id);
        alumnoRepository.deleteById(Id);
        return Borrado;
    }
    
    public Optional<Alumno> buscarAlumnoPorId(Long Id) {
        return alumnoRepository.findById(Id);
    }
    
    public Alumno agregarAlumno(Alumno alumno) {
        return alumnoRepository.save(alumno);
    }
    
}

// PAG 27