package com.LOTUS.jteducation.controller;

import com.LOTUS.jteducation.entity.Alumno;
import com.LOTUS.jteducation.service.AlumnoService;
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
    public String index(Model model) {
        // Primera página (0) con los 6 últimos alumnos inscriptos; getContent() extrae la lista del Page
        List<Alumno> ultimosAlumnos = alumnoService.obtenerAlumnos(0, 6).getContent();
        model.addAttribute("alumnos", ultimosAlumnos);
        return "index";
    }
}
