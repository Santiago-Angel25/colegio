package usco.edu.co.parcial2.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import usco.edu.co.parcial2.model.Docente;
import usco.edu.co.parcial2.model.Usuario;

public interface DocenteRepository extends JpaRepository<Docente, Long> {
    Optional<Docente> findByUsuario(Usuario usuario);
}
