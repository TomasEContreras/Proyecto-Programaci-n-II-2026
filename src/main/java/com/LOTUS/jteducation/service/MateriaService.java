package com.LOTUS.jteducation.service;

import com.LOTUS.jteducation.entity.Materia;
import com.LOTUS.jteducation.exception.RecursoNoEncontradoException;
import com.LOTUS.jteducation.exception.ReglaDeNegocioException;
import com.LOTUS.jteducation.repository.MateriaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MateriaService {

    private final MateriaRepository materiaRepository;

    @Transactional(readOnly = true)
    public List<Materia> obtenerTodas() {
        return materiaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Materia buscarPorId(Long id) {
        return materiaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la materia con id " + id));
    }

    @Transactional
    public Materia guardar(Materia materia) {
        return materiaRepository.save(materia);
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
            throw new ReglaDeNegocioException("No se puede eliminar la materia porque tiene notas asociadas.");
        }
    }
}
