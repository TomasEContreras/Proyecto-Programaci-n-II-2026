package com.LOTUS.jteducation.controller;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Solo muestra el formulario: el POST /login lo procesa Spring Security (ver SeguridadConfig)
@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Principal principal) {
        // Si ya hay una sesión iniciada no tiene sentido volver a loguearse
        return principal != null ? "redirect:/" : "login";
    }
}
