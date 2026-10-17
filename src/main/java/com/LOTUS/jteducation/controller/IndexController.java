//
//package com.LOTUS.jteducation.controller;
//
//import com.LOTUS.jteducation.entity.Alumno;
//import com.LOTUS.jteducation.service.AlumnoService; // NUEVO
//import java.util.List; // NUEVO
//import org.springframework.beans.factory.annotation.Autowired; //NUEVO
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model; // NUEVO
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller // Le dice a JPA que esta clase es un Controlador
//public class IndexController {
//    
//    @Autowired // para que el controlador pueda comunicarse con la base de datos.
//    private AlumnoService alumnoService;
//    
//    @GetMapping("/")
//    public String index(Model model) { // A través de este objeto (model) Spring Boot transfiere datos desde el CONTROLADOR a la VISTA.
//        List<Alumno> listaDeAlumnos = alumnoService.obtenerTodosLosAlumnos(); //NUEVO
//        model.addAttribute("alumnos", listaDeAlumnos); 
//        return "index";
//    }
//    
//}

package com.LOTUS.jteducation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
}

// PAG 28