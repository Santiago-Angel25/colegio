package usco.edu.co.parcial2.controller;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import usco.edu.co.parcial2.model.Asignatura;
import usco.edu.co.parcial2.model.Estudiante;
import usco.edu.co.parcial2.model.Usuario;
import usco.edu.co.parcial2.repository.DocenteRepository;
import usco.edu.co.parcial2.repository.EstudianteRepository;
import usco.edu.co.parcial2.repository.UsuarioRepository;
import usco.edu.co.parcial2.service.AsignaturaService;

@Controller
public class VistaController {

    private final AsignaturaService asignaturaService;
    private final DocenteRepository docenteRepository;
    private final EstudianteRepository estudianteRepository;
    private final UsuarioRepository usuarioRepository;

    public VistaController(
            AsignaturaService asignaturaService,
            DocenteRepository docenteRepository,
            EstudianteRepository estudianteRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.asignaturaService = asignaturaService;
        this.docenteRepository = docenteRepository;
        this.estudianteRepository = estudianteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/rector")
    public String rector(Model model) {
        model.addAttribute("asignaturas", asignaturaService.listarTodas());
        model.addAttribute("docentes", docenteRepository.findAll());
        model.addAttribute("totalAsignaturas", asignaturaService.listarTodas().size());
        model.addAttribute("totalDocentes", docenteRepository.count());
        return "rector";
    }

    @GetMapping("/docente")
    public String docente(Model model, Principal principal) {
        model.addAttribute("asignaturas", asignaturaService.listarPorDocente(principal.getName()));
        return "docente";
    }

    @GetMapping("/estudiante")
    public String estudiante(Model model, Principal principal) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm");
        List<Map<String, String>> filas = new ArrayList<>();
        Usuario usuario = usuarioRepository.findByUsername(principal.getName()).orElseThrow();
        Estudiante estudiante = estudianteRepository.findByUsuario(usuario).orElseThrow();

        asignaturaService.listarPorSalon(estudiante.getSalon()).stream()
                .filter(asignatura -> asignatura.getDiaSemana() != null)
                .sorted(Comparator
                        .comparing(Asignatura::getHoraInicio)
                        .thenComparing(asignatura -> asignatura.getDiaSemana().ordinal()))
                .forEach(asignatura -> {
                    String bloque = asignatura.getHoraInicio().format(formato)
                            + " - "
                            + asignatura.getHoraFin().format(formato);

                    Map<String, String> fila = filas.stream()
                            .filter(actual -> bloque.equals(actual.get("hora")))
                            .findFirst()
                            .orElseGet(() -> {
                                Map<String, String> nueva = new HashMap<>();
                                nueva.put("hora", bloque);
                                filas.add(nueva);
                                return nueva;
                            });

                    fila.put(asignatura.getDiaSemana().name(), asignatura.getNombre());
                });

        model.addAttribute("filasHorario", filas);
        model.addAttribute("estudiante", estudiante);
        return "estudiante";
    }

    @GetMapping("/sin-permiso")
    public String sinPermiso() {
        return "sin-permiso";
    }
}
