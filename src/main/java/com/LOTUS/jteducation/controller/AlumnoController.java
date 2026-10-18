package com.LOTUS.jteducation.controller;

import com.LOTUS.jteducation.service.AlumnoMateriaService;
import com.LOTUS.jteducation.service.AlumnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/alumnos") // Prefijo común para todas las rutas de este controlador
@RequiredArgsConstructor
public class AlumnoController {

    private final AlumnoService alumnoService;
    private final AlumnoMateriaService alumnoMateriaService;

    // GET /alumnos?page=0&size=10  (valores por defecto: 10 por página, ordenados por apellido y nombre)
    @GetMapping
    public String listar(Model model,
            @PageableDefault(size = 10, sort = {"apellido", "nombre"}) Pageable pageable) {
        model.addAttribute("paginaAlumnos", alumnoService.obtenerAlumnos(pageable));
        return "alumnos/list";
    }

    // GET /alumnos/5  -> detalle del alumno con id 5
    @GetMapping("/{id}")
    public String detalle(@PathVariable("id") Long id, Model model) {
        model.addAttribute("alumno", alumnoService.buscarPorId(id));
        model.addAttribute("notas", alumnoMateriaService.obtenerPorAlumno(id));
        return "alumnos/detail";
    }
}
