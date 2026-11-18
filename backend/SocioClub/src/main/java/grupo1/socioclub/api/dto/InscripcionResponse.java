package grupo1.socioclub.api.dto;

import grupo1.socioclub.model.EstadoInscripcion;
import grupo1.socioclub.model.Inscripcion;

import java.time.LocalDate;

public record InscripcionResponse(
        Long id,
        Long socioId,
        String socio,
        Long actividadId,
        String actividad,
        LocalDate fechaInscripcion,
        EstadoInscripcion estado) {

    public static InscripcionResponse desde(Inscripcion inscripcion) {
        return new InscripcionResponse(inscripcion.getId(), inscripcion.getSocio().getId(),
                inscripcion.getSocio().getApellido() + ", " + inscripcion.getSocio().getNombre(),
                inscripcion.getActividad().getId(), inscripcion.getActividad().getNombre(),
                inscripcion.getFechaInscripcion(), inscripcion.getEstado());
    }
}
