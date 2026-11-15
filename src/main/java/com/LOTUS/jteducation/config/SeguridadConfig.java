package com.LOTUS.jteducation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// @Configuration: clase donde se declaran objetos que Spring tiene que crear y administrar.
@Configuration
public class SeguridadConfig {

    // @Bean: Spring crea UN solo encoder y lo inyecta en quien pida un PasswordEncoder.
    // Se devuelve la interfaz: los servicios no saben que es BCrypt, así se puede cambiar el algoritmo
    // tocando solo esta línea.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
