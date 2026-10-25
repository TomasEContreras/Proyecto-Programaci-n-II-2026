package com.LOTUS.jteducation.controller;

import com.LOTUS.jteducation.dto.AlumnoForm;
import com.LOTUS.jteducation.entity.Alumno;
import com.LOTUS.jteducation.exception.ReglaDeNegocioException;
import com.LOTUS.jteducation.service.AlumnoMateriaService;
import com.LOTUS.jteducation.service.AlumnoService;
import com.LOTUS.jteducation.service.CursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/alumnos")
@RequiredArgsConstructor
public class AlumnoController {
    private static final String VISTA_LISTA = "alumnos/list";
    private static final String VISTA_FORM = "alumnos/form";
    private static final String REDIRECT_LISTA = "redirect:/alumnos";
    private static final String MSJ_EXITO = "mensajeExito";
    private static final String MSJ_ERROR = "mensajeError";

    private final AlumnoService alumnoService;
    private final AlumnoMateriaService alumnoMateriaService;
    private final CursoService cursoService;

    private void cargarCursos(Model model) {
        model.addAttribute("cursos", cursoService.obtenerTodos());
    }

    @GetMapping
    public String listar(Model model,
            @PageableDefault(size = 10, sort = {"apellido", "nombre"}) Pageable pageable) {
        model.addAttribute("paginaAlumnos", alumnoService.obtenerAlumnos(pageable));
        return VISTA_LISTA;
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Alumno alumno = alumnoService.buscarPorId(id);
        model.addAttribute("alumno", alumno);
        model.addAttribute("materias", alumnoMateriaService.obtenerPlanConNotas(alumno));
        return "alumnos/detail";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        cargarCursos(model);
        model.addAttribute("alumnoForm", new AlumnoForm());
        return VISTA_FORM;
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("alumnoForm") AlumnoForm form,
            BindingResult resultado, Model model, RedirectAttributes redirect) {
        if (!resultado.hasErrors()) {
            try {
                alumnoService.crear(form);
                redirect.addFlashAttribute(MSJ_EXITO, "Alumno creado correctamente.");
                return REDIRECT_LISTA;
            } catch (ReglaDeNegocioException | IllegalArgumentException e) {
                resultado.reject("alumno.regla", e.getMessage());
            }
        }
        cargarCursos(model);
        return VISTA_FORM;
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        cargarCursos(model);
        model.addAttribute("alumnoForm", AlumnoForm.desde(alumnoService.buscarPorId(id)));
        model.addAttribute("idAlumno", id);
        return VISTA_FORM;
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("alumnoForm") AlumnoForm form,
            BindingResult resultado, Model model, RedirectAttributes redirect) {
        model.addAttribute("idAlumno", id);
        if (!resultado.hasErrors()) {
            try {
                alumnoService.actualizar(id, form);
                redirect.addFlashAttribute(MSJ_EXITO, "Alumno actualizado correctamente.");
                return REDIRECT_LISTA;
            } catch (ReglaDeNegocioException | IllegalArgumentException e) {
                resultado.reject("alumno.regla", e.getMessage());
            }
        }
        cargarCursos(model);
        return VISTA_FORM;
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            alumnoService.eliminar(id);
            redirect.addFlashAttribute(MSJ_EXITO, "Alumno eliminado correctamente.");
        } catch (ReglaDeNegocioException e) {
            redirect.addFlashAttribute(MSJ_ERROR, e.getMessage());
        }
        return REDIRECT_LISTA;
    }
}
