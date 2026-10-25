package com.LOTUS.jteducation.service;

import com.LOTUS.jteducation.entity.Materia;
import com.LOTUS.jteducation.exception.RecursoNoEncontradoException;
import com.LOTUS.jteducation.exception.ReglaDeNegocioException;
import com.LOTUS.jteducation.repository.MateriaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MateriaService {

    private static final String MSJ_REPETIDA = "Ya existe una materia con ese nombre.";

    private final MateriaRepository materiaRepository;

    @Transactional(readOnly = true)
    public List<Materia> obtenerTodas() {
        return materiaRepository.findAll(Sort.by("nombre")); // Orden alfabético: listado y checkboxes
    }

    @Transactional(readOnly = true)
    public Materia buscarPorId(Long id) {
        return materiaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la materia con id " + id));
    }
    
    // Regla se negocio: no se repiten materias
    @Transactional
    public Materia crear(String nombre) {
        String nombreLimpio = normalizar(nombre);
        if (materiaRepository.existsByNombreIgnoreCase(nombreLimpio)) {
            throw new ReglaDeNegocioException(MSJ_REPETIDA);
        }
        return guardar(new Materia(nombreLimpio));
    }
    
    // Al editar se excluye la propia materia, para poder guardarla sin cambiar el nombre
    @Transactional
    public Materia actualizar(Long id, String nombre) {
        Materia materia = buscarPorId(id);
        String nombreLimpio = normalizar(nombre);
        if (materiaRepository.existsByNombreIgnoreCaseAndIdMateriaNot(nombreLimpio, id)) {
            throw new ReglaDeNegocioException(MSJ_REPETIDA);
        }
        materia.setNombre(nombreLimpio);
        return guardar(materia);
    }
    
    @Transactional
    public void eliminar(Long id) {
        if (!materiaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("No existe la materia con id " + id);
        }
        try {
            materiaRepository.deleteById(id);
            materiaRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ReglaDeNegocioException("No se puede eliminar la materia porque está asignada a un curso o tiene notas asociadas.");
        }
    }
    
    // La base también tiene UNIQUE (nombre): si dos personas guardan el mismo nombre a la vez,
    // las dos pasan el chequeo de arriba y la base frena a la segunda. saveAndFlush ejecuta el INSERT/UPDATE
    // acá mismo, así el error se puede capturar y mostrar en el formulario.
    private Materia guardar(Materia materia) {
        try {
            return materiaRepository.saveAndFlush(materia);
        } catch (DataIntegrityViolationException e) {
            throw new ReglaDeNegocioException(MSJ_REPETIDA);
        }
    }

    // Evita NullPointerException si llega null y quita espacios sobrantes
    private String normalizar(String nombre) {
        return nombre == null ? "" : nombre.trim();
    }
    
}
