package usco.edu.co.parcial2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import usco.edu.co.parcial2.dto.AsignaturaRequest;
import usco.edu.co.parcial2.dto.HorarioRequest;
import usco.edu.co.parcial2.model.Asignatura;
import usco.edu.co.parcial2.service.AsignaturaService;

@RestController
@RequestMapping("/api/asignaturas")
@Tag(name = "Asignaturas", description = "Servicios web para consultar y administrar asignaturas del colegio")
public class AsignaturaRestController {

    private final AsignaturaService asignaturaService;

    public AsignaturaRestController(AsignaturaService asignaturaService) {
        this.asignaturaService = asignaturaService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('RECTOR','ESTUDIANTE')")
    @Operation(summary = "Listar todas las asignaturas", description = "Permite al rector y al estudiante consultar las asignaturas registradas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de asignaturas obtenido correctamente"),
            @ApiResponse(responseCode = "403", description = "El usuario no tiene permisos para consultar este servicio")
    })
    public List<Asignatura> listar() {
        return asignaturaService.listarTodas();
    }

    @GetMapping("/mis")
    @PreAuthorize("hasRole('DOCENTE')")
    @Operation(summary = "Listar asignaturas del docente autenticado", description = "Consulta solo las asignaturas que pertenecen al docente que inicio sesion.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de asignaturas del docente obtenido correctamente"),
            @ApiResponse(responseCode = "403", description = "Solo los docentes pueden consultar este servicio"),
            @ApiResponse(responseCode = "404", description = "No existe un docente asociado al usuario autenticado")
    })
    public List<Asignatura> listarMisAsignaturas(Principal principal) {
        return asignaturaService.listarPorDocente(principal.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('RECTOR')")
    @Operation(summary = "Crear asignatura", description = "Registra una asignatura con nombre, descripcion, salon, dia, horario y docente encargado. Solo el rector puede crear.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Asignatura creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o cruce de horario/salon/docente"),
            @ApiResponse(responseCode = "403", description = "Solo el rector puede crear asignaturas"),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado")
    })
    public Asignatura crear(@Valid @RequestBody AsignaturaRequest request) {
        return asignaturaService.crear(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECTOR')")
    @Operation(summary = "Actualizar asignatura", description = "Modifica todos los datos de una asignatura existente. Solo el rector puede actualizar.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Asignatura actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o cruce de horario/salon/docente"),
            @ApiResponse(responseCode = "403", description = "Solo el rector puede actualizar asignaturas"),
            @ApiResponse(responseCode = "404", description = "Asignatura o docente no encontrado")
    })
    public Asignatura actualizar(
            @Parameter(description = "ID de la asignatura que se desea actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody AsignaturaRequest request
    ) {
        return asignaturaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('RECTOR')")
    @Operation(summary = "Eliminar asignatura", description = "Elimina una asignatura registrada. Solo el rector puede eliminar.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Asignatura eliminada correctamente"),
            @ApiResponse(responseCode = "403", description = "Solo el rector puede eliminar asignaturas"),
            @ApiResponse(responseCode = "404", description = "Asignatura no encontrada")
    })
    public void eliminar(
            @Parameter(description = "ID de la asignatura que se desea eliminar", example = "1")
            @PathVariable Long id
    ) {
        asignaturaService.eliminar(id);
    }

    @PutMapping("/{id}/horario")
    @PreAuthorize("hasRole('DOCENTE')")
    @Operation(summary = "Actualizar horario como docente", description = "Permite al docente modificar el dia y horario solo de las asignaturas que tiene a cargo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Horario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Horario invalido o cruce con otro salon/docente"),
            @ApiResponse(responseCode = "403", description = "El docente no tiene permiso sobre esa asignatura"),
            @ApiResponse(responseCode = "404", description = "Asignatura o docente no encontrado")
    })
    public Asignatura actualizarHorario(
            @Parameter(description = "ID de la asignatura cuyo horario se desea actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody HorarioRequest request,
            Principal principal
    ) {
        return asignaturaService.actualizarHorarioComoDocente(id, request, principal.getName());
    }
}
