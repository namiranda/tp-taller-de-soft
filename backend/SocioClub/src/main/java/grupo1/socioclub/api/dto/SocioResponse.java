package grupo1.socioclub.api.dto;

import grupo1.socioclub.model.EstadoCuota;
import grupo1.socioclub.model.Socio;

import java.time.LocalDate;

public record SocioResponse(
        Long id,
        String dni,
        String nombre,
        String apellido,
        LocalDate fechaNacimiento,
        String email,
        String telefono,
        EstadoCuota estadoCuota) {

    public static SocioResponse desde(Socio socio) {
        return new SocioResponse(socio.getId(), socio.getDni(), socio.getNombre(), socio.getApellido(),
                socio.getFechaNacimiento(), socio.getEmail(), socio.getTelefono(), socio.getEstadoCuota());
    }
}
