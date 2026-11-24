package com.LOTUS.jteducation.controller;

import com.LOTUS.jteducation.entity.Alumno;
import com.LOTUS.jteducation.entity.Rol;
import com.LOTUS.jteducation.service.AlumnoService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final AlumnoService alumnoService;

    @GetMapping("/")
    public String index(Principal principal, Model model) {
        // Después del login todos llegan acá: el alumno no tiene inicio de gestión, va a su ficha
        if (GlobalControllerAdvice.tieneRol(principal, Rol.ALUMNO)) {
            return "redirect:/mi-ficha";
        }
        // Primera página (0) con los 6 últimos alumnos inscriptos; getContent() extrae la lista del Page
        List<Alumno> ultimosAlumnos = alumnoService.obtenerAlumnos(0, 6).getContent();
        model.addAttribute("alumnos", ultimosAlumnos);
        return "index";
    }
}
