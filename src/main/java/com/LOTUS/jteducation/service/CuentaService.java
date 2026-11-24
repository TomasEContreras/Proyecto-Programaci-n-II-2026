package com.LOTUS.jteducation.service;

import com.LOTUS.jteducation.dto.CambioContrasenaForm;
import com.LOTUS.jteducation.entity.Usuario;
import com.LOTUS.jteducation.exception.RecursoNoEncontradoException;
import com.LOTUS.jteducation.exception.ReglaDeNegocioException;
import com.LOTUS.jteducation.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Operaciones sobre la cuenta del usuario logueado (sirve para los dos roles)
@Service
@RequiredArgsConstructor
public class CuentaService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // email = el de la sesión iniciada: cada uno solo puede cambiar SU contraseña
    @Transactional
    public void cambiarContrasena(String email, CambioContrasenaForm form) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la cuenta."));
        // Se pide la actual para que nadie pueda cambiarla si encuentra una sesión abierta en otra compu.
        // El hash no se puede "deshacer": matches() hashea lo escrito y lo compara con el guardado.
        if (!passwordEncoder.matches(form.getActual(), usuario.getPasswordHash())) {
            throw new ReglaDeNegocioException("La contraseña actual no es correcta.");
        }
        if (!form.getNueva().equals(form.getConfirmacion())) {
            throw new ReglaDeNegocioException("Las contraseñas nuevas no coinciden.");
        }
        if (form.getNueva().equals(form.getActual())) {
            throw new ReglaDeNegocioException("La nueva contraseña tiene que ser distinta de la actual.");
        }
        Usuario.validarPasswordPlana(form.getNueva()); // Misma regla que al crear la cuenta
        usuario.setPasswordHash(passwordEncoder.encode(form.getNueva()));
        // No hace falta save(): el usuario está "administrado" por JPA dentro de la transacción y
        // el cambio se guarda solo al terminar el método
    }
}
