package com.LOTUS.jteducation.controller;

import com.LOTUS.jteducation.dto.CursoForm;
import com.LOTUS.jteducation.entity.Curso;
import com.LOTUS.jteducation.entity.Division;
import com.LOTUS.jteducation.exception.ReglaDeNegocioException;
import com.LOTUS.jteducation.service.AlumnoService;
import com.LOTUS.jteducation.service.CursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/cursos")
@RequiredArgsConstructor
public class CursoController {

    private static final String VISTA_LISTA = "cursos/list";
    private static final String VISTA_FORM = "cursos/form";
    private static final String VISTA_DETALLE = "cursos/detail";
    private static final String REDIRECT_LISTA = "redirect:/cursos";
    private static final String MSJ_EXITO = "mensajeExito";
    private static final String MSJ_ERROR = "mensajeError";

    private final CursoService cursoService;
    private final AlumnoService alumnoService;

    // Las divisiones disponibles para el <select> del formulario: se agregan al modelo en todos los métodos
    @ModelAttribute("divisiones")
    public Division[] divisiones() {
        return Division.values();
    }

    // GET /cursos -> listado
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("cursos", cursoService.obtenerTodos());
        return VISTA_LISTA;
    }

    // GET /cursos/5 -> detalle del curso con sus alumnos
    @GetMapping("/{id}")
    public String detalle(@PathVariable("id") Long id, Model model) {
        model.addAttribute("curso", cursoService.buscarPorId(id));
        model.addAttribute("alumnos", alumnoService.obtenerPorCurso(id));
        return VISTA_DETALLE;
    }

    // GET /cursos/nuevo -> formulario vacío
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("cursoForm", new CursoForm());
        return VISTA_FORM;
    }

    // POST + redirect: evita reenviar el formulario al refrescar.
    // Una única salida: "vista" arranca en el formulario y solo cambia si todo salió bien.
    @PostMapping
    public String crear(@Valid @ModelAttribute("cursoForm") CursoForm form,
            BindingResult resultado, RedirectAttributes redirect) {
        String vista = VISTA_FORM;
        if (!resultado.hasErrors()) {
            try {
                cursoService.crear(form.getAnio(), form.getDivision(), form.getCicloLectivo());
                redirect.addFlashAttribute(MSJ_EXITO, "Curso creado correctamente.");
                vista = REDIRECT_LISTA;
            } catch (ReglaDeNegocioException e) {
                // Error de negocio (curso repetido): es un error del formulario completo, no de un campo
                resultado.reject("curso.repetido", e.getMessage());
            }
        }
        return vista;
    }

    // GET /cursos/5/editar -> formulario con los datos cargados
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable("id") Long id, Model model) {
        Curso curso = cursoService.buscarPorId(id);
        model.addAttribute("cursoForm", new CursoForm(curso.getAnio(), curso.getDivision(), curso.getCicloLectivo()));
        model.addAttribute("idCurso", id);
        return VISTA_FORM;
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable("id") Long id,
            @Valid @ModelAttribute("cursoForm") CursoForm form,
            BindingResult resultado, Model model, RedirectAttributes redirect) {
        // El id se agrega siempre: si hay que volver al formulario, la vista sabe que es edición
        model.addAttribute("idCurso", id);
        String vista = VISTA_FORM;
        if (!resultado.hasErrors()) {
            try {
                cursoService.actualizar(id, form.getAnio(), form.getDivision(), form.getCicloLectivo());
                redirect.addFlashAttribute(MSJ_EXITO, "Curso actualizado correctamente.");
                vista = REDIRECT_LISTA;
            } catch (ReglaDeNegocioException e) {
                resultado.reject("curso.repetido", e.getMessage());
            }
        }
        return vista;
    }

    // Modificar datos solo por POST, nunca por GET
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable("id") Long id, RedirectAttributes redirect) {
        try {
            cursoService.eliminar(id);
            redirect.addFlashAttribute(MSJ_EXITO, "Curso eliminado correctamente.");
        } catch (ReglaDeNegocioException e) {
            redirect.addFlashAttribute(MSJ_ERROR, e.getMessage());
        }
        return REDIRECT_LISTA;
    }
}
