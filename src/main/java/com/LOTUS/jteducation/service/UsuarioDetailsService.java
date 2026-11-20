package com.LOTUS.jteducation.service;

import com.LOTUS.jteducation.entity.Usuario;
import com.LOTUS.jteducation.repository.UsuarioRepository;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Spring Security llama a este servicio al iniciar sesión: le pasa el email escrito en el login y
// espera los datos de ese usuario. Después compara la contraseña escrita con el hash usando el
// PasswordEncoder (BCrypt) de SeguridadConfig.
@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Los emails se guardan en minúsculas: se normaliza igual para que "Ana@Mail.com" también entre
        String emailNormalizado = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
        Usuario usuario = usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new UsernameNotFoundException("No existe un usuario con ese email."));
        // roles("ADMIN") se guarda como "ROLE_ADMIN": es lo que después controla hasRole("ADMIN")
        return User.withUsername(usuario.getEmail())
                .password(usuario.getPasswordHash())
                .roles(usuario.getRol().name())
                .build();
    }
}
