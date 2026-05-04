package usco.edu.co.parcial2.service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalTime;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usco.edu.co.parcial2.dto.AsignaturaRequest;
import usco.edu.co.parcial2.dto.HorarioRequest;
import usco.edu.co.parcial2.model.Asignatura;
import usco.edu.co.parcial2.model.DiaSemana;
import usco.edu.co.parcial2.model.Docente;
import usco.edu.co.parcial2.model.Usuario;
import usco.edu.co.parcial2.repository.AsignaturaRepository;
import usco.edu.co.parcial2.repository.DocenteRepository;
import usco.edu.co.parcial2.repository.UsuarioRepository;

@Service
public class AsignaturaService {

    private final AsignaturaRepository asignaturaRepository;
    private final DocenteRepository docenteRepository;
    private final UsuarioRepository usuarioRepository;

    public AsignaturaService(
            AsignaturaRepository asignaturaRepository,
            DocenteRepository docenteRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.asignaturaRepository = asignaturaRepository;
        this.docenteRepository = docenteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Asignatura> listarTodas() {
        return asignaturaRepository.findAll();
    }

    public List<Asignatura> listarPorDocente(String username) {
        return asignaturaRepository.findByDocente(docenteActual(username));
    }

    public List<Asignatura> listarPorSalon(Integer salon) {
        return asignaturaRepository.findBySalon(salon);
    }

    public Asignatura buscar(Long id) {
        return asignaturaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Asignatura no encontrada"));
    }

    @Transactional
    public Asignatura crear(AsignaturaRequest request) {
        Asignatura asignatura = new Asignatura();
        asignarDatos(asignatura, request);
        return asignaturaRepository.save(asignatura);
    }

    @Transactional
    public Asignatura actualizar(Long id, AsignaturaRequest request) {
        Asignatura asignatura = buscar(id);
        asignarDatos(asignatura, request);
        return asignaturaRepository.save(asignatura);
    }

    @Transactional
    public void eliminar(Long id) {
        asignaturaRepository.delete(buscar(id));
    }

    @Transactional
    public Asignatura actualizarHorarioComoDocente(Long id, HorarioRequest request, String username) {
        Asignatura asignatura = buscar(id);
        Docente docente = docenteActual(username);

        if (!asignatura.getDocente().getId().equals(docente.getId())) {
            throw new AccessDeniedException("Solo puede actualizar asignaturas a su cargo");
        }

        LocalTime horaInicio = LocalTime.parse(request.getHoraInicio());
        LocalTime horaFin = LocalTime.parse(request.getHoraFin());
        validarHorario(horaInicio, horaFin);
        validarCruces(id, request.getDiaSemana(), asignatura.getSalon(), asignatura.getDocente(), horaInicio, horaFin);

        asignatura.setDiaSemana(request.getDiaSemana());
        asignatura.setHoraInicio(horaInicio);
        asignatura.setHoraFin(horaFin);
        return asignaturaRepository.save(asignatura);
    }

    private void asignarDatos(Asignatura asignatura, AsignaturaRequest request) {
        LocalTime horaInicio = LocalTime.parse(request.getHoraInicio());
        LocalTime horaFin = LocalTime.parse(request.getHoraFin());
        validarHorario(horaInicio, horaFin);
        validarSalon(request.getSalon());

        Docente docente = docenteRepository.findById(request.getDocenteId())
                .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado"));

        validarCruces(asignatura.getId(), request.getDiaSemana(), request.getSalon(), docente, horaInicio, horaFin);

        asignatura.setNombre(request.getNombre().trim());
        asignatura.setDescripcion(request.getDescripcion().trim());
        asignatura.setSalon(request.getSalon());
        asignatura.setDiaSemana(request.getDiaSemana());
        asignatura.setHoraInicio(horaInicio);
        asignatura.setHoraFin(horaFin);
        asignatura.setDocente(docente);
    }

    private void validarSalon(Integer salon) {
        int piso = salon / 100;
        int numero = salon % 100;

        if (piso < 1 || piso > 5 || numero < 1 || numero > 3) {
            throw new IllegalArgumentException("Seleccione un salon valido entre 101-103, 201-203, 301-303, 401-403 o 501-503");
        }
    }

    private void validarHorario(LocalTime horaInicio, LocalTime horaFin) {
        if (!horaFin.isAfter(horaInicio)) {
            throw new IllegalArgumentException("La hora de finalizacion debe ser mayor a la hora de inicio");
        }
    }

    private void validarCruces(
            Long asignaturaId,
            DiaSemana diaSemana,
            Integer salon,
            Docente docente,
            LocalTime horaInicio,
            LocalTime horaFin
    ) {
        for (Asignatura existente : asignaturaRepository.findAll()) {
            if (asignaturaId != null && asignaturaId.equals(existente.getId())) {
                continue;
            }

            if (existente.getDiaSemana() == null || existente.getDiaSemana() != diaSemana) {
                continue;
            }

            boolean seCruza = existente.getHoraInicio().isBefore(horaFin)
                    && horaInicio.isBefore(existente.getHoraFin());

            if (!seCruza) {
                continue;
            }

            if (existente.getSalon().equals(salon)) {
                throw new IllegalArgumentException(
                        "El salon " + salon + " ya tiene clase ese dia en ese horario"
                );
            }

            if (existente.getDocente().getId().equals(docente.getId())) {
                throw new IllegalArgumentException(
                        "El docente ya tiene una clase asignada ese dia en ese horario"
                );
            }
        }
    }

    private Docente docenteActual(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        return docenteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado para el usuario actual"));
    }
}
