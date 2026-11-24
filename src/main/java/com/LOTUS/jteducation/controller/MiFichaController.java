package com.LOTUS.jteducation.controller;

import com.LOTUS.jteducation.entity.Alumno;
import com.LOTUS.jteducation.service.AlumnoMateriaService;
import com.LOTUS.jteducation.service.AlumnoService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Usuario final (ALUMNO): solo puede ver SU ficha.
// El alumno se busca por el email de la sesión (Principal), nunca por un id en la URL:
// así un alumno no puede ver la ficha de otro cambiando un número en la dirección.
@Controller
@RequiredArgsConstructor
public class MiFichaController {

    private final AlumnoService alumnoService;
    private final AlumnoMateriaService alumnoMateriaService;

    @GetMapping("/mi-ficha")
    public String miFicha(Principal principal, Model model) {
        Alumno alumno = alumnoService.buscarPorEmail(principal.getName());
        model.addAttribute("alumno", alumno);
        model.addAttribute("materias", alumnoMateriaService.obtenerPlanConNotas(alumno));
        return "alumnos/mi-ficha";
    }
}
