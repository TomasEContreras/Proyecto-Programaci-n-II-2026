package com.LOTUS.jteducation.controller;

import com.LOTUS.jteducation.dto.AlumnoForm;
import com.LOTUS.jteducation.entity.Alumno;
import com.LOTUS.jteducation.exception.RecursoNoEncontradoException;
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
import org.springframework.web.bind.annotation.RequestParam;
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
    private static final String MSJ_INFO = "mensajeInfo";

    private final AlumnoService alumnoService;
    private final AlumnoMateriaService alumnoMateriaService;
    private final CursoService cursoService;

    private void cargarCursos(Model model) {
        model.addAttribute("cursos", cursoService.obtenerTodos());
    }

    @GetMapping
    public String listar(Model model,
            @RequestParam(name = "q", required = false) String busqueda,
            @PageableDefault(size = 10, sort = {"apellido", "nombre"}) Pageable pageable) {
        model.addAttribute("paginaAlumnos", alumnoService.buscarAlumnos(busqueda, pageable));
        model.addAttribute("busqueda", busqueda == null ? "" : busqueda.trim()); // Para el campo y los links de página
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
        // Una única salida: "vista" arranca en el formulario y solo cambia si todo salió bien
        String vista = VISTA_FORM;
        if (!resultado.hasErrors()) {
            try {
                alumnoService.crear(form);
                redirect.addFlashAttribute(MSJ_EXITO, "Alumno creado correctamente.");
                vista = REDIRECT_LISTA;
            } catch (ReglaDeNegocioException | IllegalArgumentException e) {
                resultado.reject("alumno.regla", e.getMessage());
            }
        }
        if (vista.equals(VISTA_FORM)) {
            cargarCursos(model); // Se vuelve al formulario: hace falta la lista de cursos otra vez
        }
        return vista;
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
        String vista = VISTA_FORM;
        if (!resultado.hasErrors()) {
            try {
                alumnoService.actualizar(id, form);
                redirect.addFlashAttribute(MSJ_EXITO, "Alumno actualizado correctamente.");
                vista = REDIRECT_LISTA;
            } catch (ReglaDeNegocioException | IllegalArgumentException e) {
                resultado.reject("alumno.regla", e.getMessage());
            }
        }
        if (vista.equals(VISTA_FORM)) {
            cargarCursos(model);
        }
        return vista;
    }

    // Si el alumno ya no existe, solo se avisa (lo que se quería ya pasó); si tiene notas, es un error.
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            alumnoService.eliminar(id);
            redirect.addFlashAttribute(MSJ_EXITO, "Alumno eliminado correctamente.");
        } catch (RecursoNoEncontradoException e) {
            redirect.addFlashAttribute(MSJ_INFO, "El alumno ya había sido eliminado.");
        } catch (ReglaDeNegocioException e) {
            redirect.addFlashAttribute(MSJ_ERROR, e.getMessage());
        }
        return REDIRECT_LISTA;
    }
}
