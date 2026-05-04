package usco.edu.co.parcial2.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import usco.edu.co.parcial2.model.Estudiante;
import usco.edu.co.parcial2.model.Usuario;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByUsuario(Usuario usuario);
}
