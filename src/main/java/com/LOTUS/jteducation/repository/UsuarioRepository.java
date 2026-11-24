package com.LOTUS.jteducation.repository;

import com.LOTUS.jteducation.entity.Rol;
import com.LOTUS.jteducation.entity.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Lo va a necesitar Spring Security para el login
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    // Usuarios de un rol, ordenados por email (lo usa la sección "Administradores")
    List<Usuario> findByRolOrderByEmailAsc(Rol rol);
}
