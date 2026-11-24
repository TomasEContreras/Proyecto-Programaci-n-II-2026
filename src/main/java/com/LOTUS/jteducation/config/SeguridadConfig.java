package com.LOTUS.jteducation.config;

import com.LOTUS.jteducation.entity.Rol;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.CsrfException;

// @Configuration: clase donde se declaran objetos que Spring tiene que crear y administrar.
// @EnableWebSecurity: activa la configuración web de Spring Security definida en esta clase.
@Configuration
@EnableWebSecurity
public class SeguridadConfig {

    // @Bean: Spring crea UN solo encoder y lo inyecta en quien pida un PasswordEncoder.
    // Se devuelve la interfaz: los servicios no saben que es BCrypt, así se puede cambiar el algoritmo
    // tocando solo esta línea.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Spring Security intercepta cada pedido ANTES de que llegue al controlador y decide si se permite.
    @Bean
    public SecurityFilterChain filtroDeSeguridad(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(permisos -> permisos
                // Las reglas se evalúan en orden: de la más específica a la más general
                .requestMatchers("/css/**", "/images/**", "/login", "/error").permitAll()
                .requestMatchers("/mi-ficha").hasRole(Rol.ALUMNO.name())
                .requestMatchers("/").authenticated() // El inicio redirige a cada uno a su página (IndexController)
                .requestMatchers("/cambiar-contrasena").authenticated() // Cada uno cambia la suya (los dos roles)
                // Toda la gestión (alumnos, cursos, materias y notas) es solo para el administrador.
                // hasRole("ADMIN") busca el permiso "ROLE_ADMIN" que le da UsuarioDetailsService.
                // Un ALUMNO logueado que intenta entrar recibe un error 403 (prohibido).
                .anyRequest().hasRole(Rol.ADMIN.name()))
            .formLogin(login -> login
                .loginPage("/login")             // Nuestra propia página (templates/login.html)
                .usernameParameter("email")      // El usuario se identifica con su email
                .defaultSuccessUrl("/", true)
                .permitAll())
            .logout(logout -> logout
                .logoutSuccessUrl("/login?salir") // Cierra la sesión y borra la cookie JSESSIONID
                .deleteCookies("JSESSIONID")
                .permitAll())
            // Al iniciar sesión se cambia el id de la sesión (protege contra "session fixation")
            .sessionManagement(sesion -> sesion.sessionFixation(fijacion -> fijacion.changeSessionId()))
            .exceptionHandling(errores -> errores.accessDeniedHandler(manejarAccesoDenegado()));
        // CSRF queda ACTIVADO (valor por defecto): Thymeleaf agrega el token oculto en cada th:action
        return http.build();
    }

    // Spring Security responde 403 en dos casos distintos:
    // - el usuario no tiene el rol necesario -> se muestra "No tenés permiso" (comportamiento normal);
    // - el token CSRF no coincide: pasa si se envía un formulario que quedó abierto después de cerrar
    //   sesión (o de que la sesión venza). Ahí no es falta de permiso: se manda al login para entrar de nuevo.
    private AccessDeniedHandler manejarAccesoDenegado() {
        AccessDeniedHandler porDefecto = new AccessDeniedHandlerImpl();
        return (pedido, respuesta, excepcion) -> {
            if (excepcion instanceof CsrfException) {
                respuesta.sendRedirect(pedido.getContextPath() + "/login?expirada");
            } else {
                porDefecto.handle(pedido, respuesta, excepcion);
            }
        };
    }
}
