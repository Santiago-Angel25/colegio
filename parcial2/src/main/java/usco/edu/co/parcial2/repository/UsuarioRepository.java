package usco.edu.co.parcial2.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import usco.edu.co.parcial2.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
