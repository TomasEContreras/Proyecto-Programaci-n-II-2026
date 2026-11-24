package com.LOTUS.jteducation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.LOTUS.jteducation.dto.CambioContrasenaForm;
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
import org.springframework.security.crypto.password.PasswordEncoder;

// Mockito reemplaza el repositorio por un "doble" (mock): así se prueba solo la lógica del servicio,
// sin base de datos. Con when(...).thenReturn(...) se le dice qué devolver.
@ExtendWith(MockitoExtension.class)
class CuentaServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private CuentaService servicio;
    private Usuario ana;

    @BeforeEach
    void preparar() {
        servicio = new CuentaService(usuarioRepository, encoder);
        ana = new Usuario("ana@mail.com", encoder.encode("Actual1234"), Rol.ALUMNO);
        when(usuarioRepository.findByEmail("ana@mail.com")).thenReturn(Optional.of(ana));
    }

    private CambioContrasenaForm form(String actual, String nueva, String confirmacion) {
        CambioContrasenaForm form = new CambioContrasenaForm();
        form.setActual(actual);
        form.setNueva(nueva);
        form.setConfirmacion(confirmacion);
        return form;
    }

    @Test
    void cambiaLaContrasenaYLaGuardaHasheada() {
        servicio.cambiarContrasena("ana@mail.com", form("Actual1234", "Nueva12345", "Nueva12345"));

        assertThat(ana.getPasswordHash()).startsWith("$2a$");
        assertThat(encoder.matches("Nueva12345", ana.getPasswordHash())).isTrue();
    }

    @Test
    void rechazaSiLaContrasenaActualEsIncorrecta() {
        assertThatThrownBy(() -> servicio.cambiarContrasena("ana@mail.com", form("Mal12345", "Nueva12345", "Nueva12345")))
                .isInstanceOf(ReglaDeNegocioException.class)
                .hasMessageContaining("actual no es correcta");
    }

    @Test
    void rechazaSiLasNuevasNoCoinciden() {
        assertThatThrownBy(() -> servicio.cambiarContrasena("ana@mail.com", form("Actual1234", "Nueva12345", "Otra12345")))
                .isInstanceOf(ReglaDeNegocioException.class)
                .hasMessageContaining("no coinciden");
    }

    @Test
    void rechazaSiLaNuevaEsIgualALaActual() {
        assertThatThrownBy(() -> servicio.cambiarContrasena("ana@mail.com", form("Actual1234", "Actual1234", "Actual1234")))
                .isInstanceOf(ReglaDeNegocioException.class)
                .hasMessageContaining("distinta");
    }
}
