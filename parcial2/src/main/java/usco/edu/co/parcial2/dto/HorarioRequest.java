package usco.edu.co.parcial2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import usco.edu.co.parcial2.model.DiaSemana;

@Schema(description = "Datos requeridos para actualizar el dia y horario de una asignatura")
public class HorarioRequest {

    @NotNull
    @Schema(description = "Nuevo dia de la semana.", example = "MARTES")
    private DiaSemana diaSemana;

    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$")
    @Schema(description = "Nueva hora de inicio en formato HH:mm.", example = "09:00")
    private String horaInicio;

    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$")
    @Schema(description = "Nueva hora de finalizacion en formato HH:mm.", example = "10:00")
    private String horaFin;

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
}
