package com.LOTUS.jteducation.controller;

import com.LOTUS.jteducation.dto.CursoForm;
import com.LOTUS.jteducation.entity.Curso;
import com.LOTUS.jteducation.entity.Division;
import com.LOTUS.jteducation.exception.ReglaDeNegocioException;
import com.LOTUS.jteducation.service.AlumnoService;
import com.LOTUS.jteducation.service.CursoService;
import com.LOTUS.jteducation.service.MateriaService;
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
    private final MateriaService materiaService;

    // Las divisiones disponibles para el <select> del formulario: se agregan al modelo en todos los métodos
    @ModelAttribute("divisiones")
    public Division[] divisiones() {
        return Division.values();
    }

    // Las materias para los checkboxes: solo se cargan cuando se muestra el formulario
    // (con @ModelAttribute se consultaría la base también en el listado y el detalle)
    private void cargarMaterias(Model model) {
        model.addAttribute("materias", materiaService.obtenerTodas());
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
        cargarMaterias(model);
        model.addAttribute("cursoForm", new CursoForm());
        return VISTA_FORM;
    }

    // POST + redirect: evita reenviar el formulario al refrescar.
    // Una única salida: "vista" arranca en el formulario y solo cambia si todo salió bien.
    @PostMapping
    public String crear(@Valid @ModelAttribute("cursoForm") CursoForm form,
            BindingResult resultado, Model model, RedirectAttributes redirect) {
        String vista = VISTA_FORM;
        if (!resultado.hasErrors()) {
            try {
                cursoService.crear(form.getAnio(), form.getDivision(), form.getIdsMaterias());
                redirect.addFlashAttribute(MSJ_EXITO, "Curso creado correctamente.");
                vista = REDIRECT_LISTA;
            } catch (ReglaDeNegocioException | IllegalArgumentException e) {
                // Error de negocio (curso repetido, materia inexistente): es un error del formulario completo
                resultado.reject("curso.regla", e.getMessage());
            }
        }
        if (vista.equals(VISTA_FORM)) {
            cargarMaterias(model); // Se vuelve al formulario: hacen falta los checkboxes otra vez
        }
        return vista;
    }

    // GET /cursos/5/editar -> formulario con los datos cargados
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable("id") Long id, Model model) {
        // Primero el curso: si no existe, salta el 404 sin consultar las materias
        Curso curso = cursoService.buscarPorId(id);
        model.addAttribute("cursoForm", CursoForm.desde(curso));
        model.addAttribute("idCurso", id);
        model.addAttribute("cicloLectivo", curso.getCicloLectivo()); // Solo se muestra: no se edita
        cargarMaterias(model);
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
                cursoService.actualizar(id, form.getAnio(), form.getDivision(), form.getIdsMaterias());
                redirect.addFlashAttribute(MSJ_EXITO, "Curso actualizado correctamente.");
                vista = REDIRECT_LISTA;
            } catch (ReglaDeNegocioException | IllegalArgumentException e) {
                // Curso repetido o materia con notas que no se puede quitar: error del formulario completo
                resultado.reject("curso.regla", e.getMessage());
            }
        }
        if (vista.equals(VISTA_FORM)) {
            model.addAttribute("cicloLectivo", cursoService.buscarPorId(id).getCicloLectivo());
            cargarMaterias(model);
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
