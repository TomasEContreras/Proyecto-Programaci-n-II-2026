package com.LOTUS.jteducation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.LOTUS.jteducation.dto.AdministradorForm;
import com.LOTUS.jteducation.entity.Rol;
import com.LOTUS.jteducation.entity.Usuario;
import com.LOTUS.jteducation.exception.ReglaDeNegocioException;
import com.LOTUS.jteducation.repository.UsuarioRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AdministradorServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private AdministradorService servicio;

    @BeforeEach
    void preparar() {
        servicio = new AdministradorService(usuarioRepository, new BCryptPasswordEncoder());
    }

    private AdministradorForm form(String email, String password, String confirmacion) {
        AdministradorForm form = new AdministradorForm();
        form.setEmail(email);
        form.setPassword(password);
        form.setConfirmacion(confirmacion);
        return form;
    }

    @Test
    void creaElAdministradorConEmailEnMinusculasYRolAdmin() {
        when(usuarioRepository.existsByEmail("secretaria@jteducation.com")).thenReturn(false);
        when(usuarioRepository.saveAndFlush(any(Usuario.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        Usuario creado = servicio.crear(form(" Secretaria@JTEducation.com ", "Clave1234", "Clave1234"));

        assertThat(creado.getEmail()).isEqualTo("secretaria@jteducation.com");
        assertThat(creado.getRol()).isEqualTo(Rol.ADMIN);
        assertThat(creado.getPasswordHash()).isNotEqualTo("Clave1234"); // Nunca en texto plano
    }

    @Test
    void noCreaSiLasContrasenasNoCoinciden() {
        assertThatThrownBy(() -> servicio.crear(form("a@b.com", "Clave1234", "Otra12345")))
                .isInstanceOf(ReglaDeNegocioException.class);
        verify(usuarioRepository, never()).saveAndFlush(any());
    }

    @Test
    void noCreaSiElEmailYaExiste() {
        when(usuarioRepository.existsByEmail("admin@jteducation.com")).thenReturn(true);

        assertThatThrownBy(() -> servicio.crear(form("admin@jteducation.com", "Clave1234", "Clave1234")))
                .isInstanceOf(ReglaDeNegocioException.class)
                .hasMessageContaining("Ya existe");
    }

    @Test
    void nadiePuedeEliminarSuPropiaCuenta() {
        Usuario admin = new Usuario("admin@jteducation.com", "hash", Rol.ADMIN);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));

        assertThatThrownBy(() -> servicio.eliminar(1L, "admin@jteducation.com"))
                .isInstanceOf(ReglaDeNegocioException.class)
                .hasMessageContaining("propia cuenta");
        verify(usuarioRepository, never()).delete(any());
    }

    @Test
    void noBorraCuentasDeAlumnos() {
        Usuario alumna = new Usuario("lucia@mail.com", "hash", Rol.ALUMNO);
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(alumna));

        assertThatThrownBy(() -> servicio.eliminar(2L, "admin@jteducation.com"))
                .isInstanceOf(ReglaDeNegocioException.class);
        verify(usuarioRepository, never()).delete(any());
    }

    @Test
    void eliminaAOtroAdministrador() {
        Usuario otro = new Usuario("secretaria@jteducation.com", "hash", Rol.ADMIN);
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(otro));

        servicio.eliminar(3L, "admin@jteducation.com");

        verify(usuarioRepository).delete(otro);
    }
}
