package grupo1.socioclub.api.dto;

import grupo1.socioclub.model.Actividad;

import java.math.BigDecimal;
import java.time.LocalTime;

public record ActividadResponse(
        Long id,
        String nombre,
        String disciplina,
        String profesor,
        String dias,
        LocalTime horario,
        Integer edadMinima,
        Integer edadMaxima,
        Integer cupoMaximo,
        BigDecimal cuotaMensual,
        long cupoDisponible) {

    public static ActividadResponse desde(Actividad actividad, long inscripcionesActivas) {
        return new ActividadResponse(actividad.getId(), actividad.getNombre(), actividad.getDisciplina(),
                actividad.getProfesor(), actividad.getDias(), actividad.getHorario(), actividad.getEdadMinima(),
                actividad.getEdadMaxima(), actividad.getCupoMaximo(), actividad.getCuotaMensual(),
                Math.max(0, actividad.getCupoMaximo() - inscripcionesActivas));
    }
}
