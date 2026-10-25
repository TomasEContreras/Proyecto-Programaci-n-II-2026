package com.LOTUS.jteducation.controller;

import com.LOTUS.jteducation.dto.NotaForm;
import com.LOTUS.jteducation.entity.Alumno;
import com.LOTUS.jteducation.exception.ReglaDeNegocioException;
import com.LOTUS.jteducation.service.AlumnoMateriaService;
import com.LOTUS.jteducation.service.AlumnoService;
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

// Carga de notas de UN alumno en UNA materia. La URL refleja esa jerarquía:
// /alumnos/5/notas/2 = las notas del alumno 5 en la materia 2.
@Controller
@RequestMapping("/alumnos/{idAlumno}/notas/{idMateria}")
@RequiredArgsConstructor
public class NotaController {

    private static final String VISTA_FORM = "notas/form";
    private static final String MSJ_EXITO = "mensajeExito";

    private final AlumnoService alumnoService;
    private final AlumnoMateriaService alumnoMateriaService;

    // La vista siempre necesita saber de qué alumno y de qué materia se trata (para el título y los links)
    private void cargarAlumnoYMateria(Long idAlumno, Long idMateria, Model model) {
        Alumno alumno = alumnoService.buscarPorId(idAlumno);
        model.addAttribute("alumno", alumno);
        model.addAttribute("materia", alumnoMateriaService.buscarMateriaDelPlan(alumno, idMateria));
    }

    // GET /alumnos/5/notas/2 -> formulario con las notas actuales (o vacío si todavía no tiene)
    @GetMapping
    public String editar(@PathVariable("idAlumno") Long idAlumno, @PathVariable("idMateria") Long idMateria,
            Model model) {
        cargarAlumnoYMateria(idAlumno, idMateria, model);
        model.addAttribute("notaForm",
                NotaForm.desde(alumnoMateriaService.buscarNotas(idAlumno, idMateria).orElse(null)));
        return VISTA_FORM;
    }

    // POST + redirect a la ficha del alumno. Una única salida, como en el resto de los controladores.
    @PostMapping
    public String guardar(@PathVariable("idAlumno") Long idAlumno, @PathVariable("idMateria") Long idMateria,
            @Valid @ModelAttribute("notaForm") NotaForm form, BindingResult resultado,
            Model model, RedirectAttributes redirect) {
        String vista = VISTA_FORM;
        if (!resultado.hasErrors()) {
            try {
                alumnoMateriaService.guardarNotas(idAlumno, idMateria, form);
                redirect.addFlashAttribute(MSJ_EXITO,
                        form.estaVacio() ? "Notas borradas correctamente." : "Notas guardadas correctamente.");
                vista = "redirect:/alumnos/" + idAlumno;
            } catch (ReglaDeNegocioException | IllegalArgumentException e) {
                resultado.reject("nota.regla", e.getMessage());
            }
        }
        if (vista.equals(VISTA_FORM)) {
            cargarAlumnoYMateria(idAlumno, idMateria, model); // Se vuelve al formulario: hace falta el título
        }
        return vista;
    }
}
