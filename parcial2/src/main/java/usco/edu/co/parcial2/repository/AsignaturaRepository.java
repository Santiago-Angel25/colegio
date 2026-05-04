package usco.edu.co.parcial2.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import usco.edu.co.parcial2.model.Asignatura;
import usco.edu.co.parcial2.model.Docente;

public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {
    List<Asignatura> findByDocente(Docente docente);
    List<Asignatura> findBySalon(Integer salon);
}
