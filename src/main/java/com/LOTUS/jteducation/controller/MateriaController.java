package com.LOTUS.jteducation.controller;

import com.LOTUS.jteducation.dto.MateriaForm;
import com.LOTUS.jteducation.entity.Materia;
import com.LOTUS.jteducation.exception.ReglaDeNegocioException;
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
@RequestMapping("/materias")
@RequiredArgsConstructor
public class MateriaController {

    private static final String VISTA_LISTA = "materias/list";
    private static final String VISTA_FORM = "materias/form";
    private static final String REDIRECT_LISTA = "redirect:/materias";
    private static final String MSJ_EXITO = "mensajeExito";
    private static final String MSJ_ERROR = "mensajeError";

    private final MateriaService materiaService;

    // GET /materias -> listado
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("materias", materiaService.obtenerTodas());
        return VISTA_LISTA;
    }

    // GET /materias/nueva -> formulario vacío
    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("materiaForm", new MateriaForm());
        return VISTA_FORM;
    }

    // POST + redirect: evita reenviar el formulario al refrescar.
    // Una única salida: "vista" arranca en el formulario y solo cambia si todo salió bien.
    @PostMapping
    public String crear(@Valid @ModelAttribute("materiaForm") MateriaForm form,
            BindingResult resultado, RedirectAttributes redirect) {
        String vista = VISTA_FORM;
        if (!resultado.hasErrors()) {
            try {
                materiaService.crear(form.getNombre());
                redirect.addFlashAttribute(MSJ_EXITO, "Materia creada correctamente.");
                vista = REDIRECT_LISTA;
            } catch (ReglaDeNegocioException e) {
                // Error de negocio (nombre repetido): se muestra junto al campo
                resultado.rejectValue("nombre", "nombre.repetido", e.getMessage());
            }
        }
        return vista;
    }

    // GET /materias/5/editar -> formulario con los datos cargados
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable("id") Long id, Model model) {
        Materia materia = materiaService.buscarPorId(id);
        model.addAttribute("materiaForm", new MateriaForm(materia.getNombre()));
        model.addAttribute("idMateria", id);
        return VISTA_FORM;
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable("id") Long id,
            @Valid @ModelAttribute("materiaForm") MateriaForm form,
            BindingResult resultado, Model model, RedirectAttributes redirect) {
        // El id se agrega siempre: si hay que volver al formulario, la vista sabe que es edición
        model.addAttribute("idMateria", id);
        String vista = VISTA_FORM;
        if (!resultado.hasErrors()) {
            try {
                materiaService.actualizar(id, form.getNombre());
                redirect.addFlashAttribute(MSJ_EXITO, "Materia actualizada correctamente.");
                vista = REDIRECT_LISTA;
            } catch (ReglaDeNegocioException e) {
                resultado.rejectValue("nombre", "nombre.repetido", e.getMessage());
            }
        }
        return vista;
    }

    // Modificar datos solo por POST, nunca por GET
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable("id") Long id, RedirectAttributes redirect) {
        try {
            materiaService.eliminar(id);
            redirect.addFlashAttribute(MSJ_EXITO, "Materia eliminada correctamente.");
        } catch (ReglaDeNegocioException e) {
            redirect.addFlashAttribute(MSJ_ERROR, e.getMessage());
        }
        return REDIRECT_LISTA;
    }
}
