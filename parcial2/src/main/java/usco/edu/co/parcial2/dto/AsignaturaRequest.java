package usco.edu.co.parcial2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import usco.edu.co.parcial2.model.DiaSemana;

@Schema(description = "Datos requeridos para crear o actualizar una asignatura")
public class AsignaturaRequest {

    @NotBlank
    @Size(max = 30)
    @Pattern(regexp = "^[A-Za-z0-9 ]+$")
    @Schema(description = "Nombre alfanumerico de la asignatura. Maximo 30 caracteres.", example = "Matematicas")
    private String nombre;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Descripcion textual de la asignatura. Maximo 100 caracteres.", example = "Clase de matematicas basicas")
    private String descripcion;

    @NotNull
    @Min(101)
    @Max(503)
    @Schema(description = "Salon donde se dicta la clase. Valores permitidos: 101-103, 201-203, 301-303, 401-403, 501-503.", example = "101")
    private Integer salon;

    @NotNull
    @Schema(description = "Dia de la semana en que se dicta la asignatura.", example = "LUNES")
    private DiaSemana diaSemana;

    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$")
    @Schema(description = "Hora de inicio en formato HH:mm.", example = "07:00")
    private String horaInicio;

    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$")
    @Schema(description = "Hora de finalizacion en formato HH:mm.", example = "08:00")
    private String horaFin;

    @NotNull
    @Schema(description = "ID del docente encargado de la asignatura.", example = "1")
    private Long docenteId;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getSalon() {
        return salon;
    }

    public void setSalon(Integer salon) {
        this.salon = salon;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public Long getDocenteId() {
        return docenteId;
    }

    public void setDocenteId(Long docenteId) {
        this.docenteId = docenteId;
    }
}
