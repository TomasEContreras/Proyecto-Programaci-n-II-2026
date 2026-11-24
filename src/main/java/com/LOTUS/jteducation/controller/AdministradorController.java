package com.LOTUS.jteducation.controller;

import com.LOTUS.jteducation.dto.AdministradorForm;
import com.LOTUS.jteducation.exception.ReglaDeNegocioException;
import com.LOTUS.jteducation.service.AdministradorService;
import jakarta.validation.Valid;
import java.security.Principal;
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

// Solo el ADMIN llega acá: SeguridadConfig deja toda la gestión para ese rol.
@Controller
@RequestMapping("/administradores")
@RequiredArgsConstructor
public class AdministradorController {

    private static final String VISTA_LISTA = "administradores/list";
    private static final String VISTA_FORM = "administradores/form";
    private static final String REDIRECT_LISTA = "redirect:/administradores";
    private static final String MSJ_EXITO = "mensajeExito";
    private static final String MSJ_ERROR = "mensajeError";

    private final AdministradorService administradorService;

    // GET /administradores -> listado
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("administradores", administradorService.obtenerTodos());
        return VISTA_LISTA;
    }

    // GET /administradores/nuevo -> formulario vacío
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("administradorForm", new AdministradorForm());
        return VISTA_FORM;
    }

    // POST + redirect. Una única salida: "vista" arranca en el formulario y solo cambia si todo salió bien.
    @PostMapping
    public String crear(@Valid @ModelAttribute("administradorForm") AdministradorForm form,
            BindingResult resultado, RedirectAttributes redirect) {
        String vista = VISTA_FORM;
        if (!resultado.hasErrors()) {
            try {
                administradorService.crear(form);
                redirect.addFlashAttribute(MSJ_EXITO, "Administrador creado correctamente.");
                vista = REDIRECT_LISTA;
            } catch (ReglaDeNegocioException | IllegalArgumentException e) {
                // Email repetido o contraseñas distintas: error del formulario completo
                resultado.reject("administrador.regla", e.getMessage());
            }
        }
        // Por seguridad, las contraseñas nunca se devuelven al formulario: hay que escribirlas de nuevo
        form.setPassword(null);
        form.setConfirmacion(null);
        return vista;
    }

    // Modificar datos solo por POST, nunca por GET
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable("id") Long id, Principal principal, RedirectAttributes redirect) {
        try {
            administradorService.eliminar(id, principal.getName());
            redirect.addFlashAttribute(MSJ_EXITO, "Administrador eliminado correctamente.");
        } catch (ReglaDeNegocioException e) {
            redirect.addFlashAttribute(MSJ_ERROR, e.getMessage());
        }
        return REDIRECT_LISTA;
    }
}
