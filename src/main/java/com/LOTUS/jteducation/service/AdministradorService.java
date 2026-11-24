package com.LOTUS.jteducation.service;

import com.LOTUS.jteducation.dto.AdministradorForm;
import com.LOTUS.jteducation.entity.Rol;
import com.LOTUS.jteducation.entity.Usuario;
import com.LOTUS.jteducation.exception.RecursoNoEncontradoException;
import com.LOTUS.jteducation.exception.ReglaDeNegocioException;
import com.LOTUS.jteducation.repository.UsuarioRepository;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Gestión de las cuentas con rol ADMIN. Las cuentas de alumnos se crean y se borran junto con el alumno
// (AlumnoService), por eso acá solo se trabaja con usuarios ADMIN.
@Service
@RequiredArgsConstructor
public class AdministradorService {

    private static final String MSJ_EMAIL_REPETIDO = "Ya existe una cuenta con ese email.";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findByRolOrderByEmailAsc(Rol.ADMIN);
    }

    @Transactional
    public Usuario crear(AdministradorForm form) {
        if (!form.getPassword().equals(form.getConfirmacion())) {
            throw new ReglaDeNegocioException("Las contraseñas no coinciden.");
        }
        // La regla de la contraseña se valida en texto plano, ANTES de hashearla
        Usuario.validarPasswordPlana(form.getPassword());
        // Los emails se guardan en minúsculas, igual que los de los alumnos
        String email = form.getEmail().trim().toLowerCase(Locale.ROOT);
        if (usuarioRepository.existsByEmail(email)) {
            throw new ReglaDeNegocioException(MSJ_EMAIL_REPETIDO);
        }
        try {
            return usuarioRepository.saveAndFlush(
                    new Usuario(email, passwordEncoder.encode(form.getPassword()), Rol.ADMIN));
        } catch (DataIntegrityViolationException e) {
            // Dos altas simultáneas con el mismo email pasan el chequeo de arriba: la base (UNIQUE) frena la segunda
            throw new ReglaDeNegocioException(MSJ_EMAIL_REPETIDO);
        }
    }

    // emailActual = el administrador que tiene la sesión iniciada
    @Transactional
    public void eliminar(Long id, String emailActual) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + id));
        if (usuario.getRol() != Rol.ADMIN) {
            // Una cuenta de alumno se borra eliminando al alumno, nunca desde acá
            throw new ReglaDeNegocioException("Esa cuenta no es de un administrador.");
        }
        // Regla de negocio: nadie se borra a sí mismo. Así siempre queda al menos un administrador
        // (el que está usando el sistema) y nadie se queda afuera por error.
        if (usuario.getEmail().equals(emailActual)) {
            throw new ReglaDeNegocioException("No podés eliminar tu propia cuenta.");
        }
        usuarioRepository.delete(usuario);
    }
}
