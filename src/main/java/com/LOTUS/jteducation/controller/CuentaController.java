package com.LOTUS.jteducation.controller;

import com.LOTUS.jteducation.dto.CambioContrasenaForm;
import com.LOTUS.jteducation.entity.Rol;
import com.LOTUS.jteducation.exception.ReglaDeNegocioException;
import com.LOTUS.jteducation.service.CuentaService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Cuenta propia: la usan los dos roles (SeguridadConfig la permite a cualquier usuario logueado).
// El usuario siempre sale de la sesión (Principal), nunca de la URL.
@Controller
@RequestMapping("/cambiar-contrasena")
@RequiredArgsConstructor
public class CuentaController {

    private static final String VISTA_FORM = "cuenta/contrasena";

    private final CuentaService cuentaService;

    @GetMapping
    public String formulario(Model model) {
        model.addAttribute("cambioContrasenaForm", new CambioContrasenaForm());
        return VISTA_FORM;
    }

    @PostMapping
    public String cambiar(@Valid @ModelAttribute("cambioContrasenaForm") CambioContrasenaForm form,
            BindingResult resultado, Principal principal, RedirectAttributes redirect) {
        String vista = VISTA_FORM;
        if (!resultado.hasErrors()) {
            try {
                cuentaService.cambiarContrasena(principal.getName(), form);
                redirect.addFlashAttribute("mensajeExito", "Contraseña cambiada correctamente.");
                // Directo a la página de cada rol: el mensaje flash sobrevive a UNA sola redirección
                vista = GlobalControllerAdvice.tieneRol(principal, Rol.ALUMNO) ? "redirect:/mi-ficha" : "redirect:/";
            } catch (ReglaDeNegocioException | IllegalArgumentException e) {
                resultado.reject("cuenta.regla", e.getMessage());
            }
        }
        // Por seguridad, las contraseñas nunca se devuelven al formulario
        form.setActual(null);
        form.setNueva(null);
        form.setConfirmacion(null);
        return vista;
    }
}
