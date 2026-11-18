package grupo1.socioclub.service;

import grupo1.socioclub.model.Actividad;
import grupo1.socioclub.model.EstadoCuota;
import grupo1.socioclub.model.EstadoInscripcion;
import grupo1.socioclub.model.Inscripcion;
import grupo1.socioclub.model.Socio;
import grupo1.socioclub.repository.ActividadRepository;
import grupo1.socioclub.repository.InscripcionRepository;
import grupo1.socioclub.repository.SocioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final SocioRepository socioRepository;
    private final ActividadRepository actividadRepository;

    public InscripcionService(InscripcionRepository inscripcionRepository,
                              SocioRepository socioRepository,
                              ActividadRepository actividadRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.socioRepository = socioRepository;
        this.actividadRepository = actividadRepository;
    }

    @Transactional
    public Inscripcion inscribir(Long socioId, Long actividadId) {
        Socio socio = socioRepository.findById(socioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el socio seleccionado"));
        Actividad actividad = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró la actividad seleccionada"));

        validarNuevaInscripcion(socio, actividad);

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setSocio(socio);
        inscripcion.setActividad(actividad);
        inscripcion.setFechaInscripcion(LocalDate.now());
        inscripcion.setEstado(EstadoInscripcion.ACTIVA);
        return inscripcionRepository.save(inscripcion);
    }

    private void validarNuevaInscripcion(Socio socio, Actividad actividad) {
        Long socioId = socio.getId();
        Long actividadId = actividad.getId();
        if (inscripcionRepository.countByActividadIdAndEstado(actividadId, EstadoInscripcion.ACTIVA)
                >= actividad.getCupoMaximo()) {
            throw new ReglaNegocioException("La actividad no tiene cupo disponible");
        }
        if (inscripcionRepository.existsBySocioIdAndActividadIdAndEstado(
                socioId, actividadId, EstadoInscripcion.ACTIVA)) {
            throw new ReglaNegocioException("El socio ya está inscripto en esta actividad");
        }

        int edad = Period.between(socio.getFechaNacimiento(), LocalDate.now()).getYears();
        if (edad < actividad.getEdadMinima() || edad > actividad.getEdadMaxima()) {
            throw new ReglaNegocioException("La actividad admite socios de entre "
                    + actividad.getEdadMinima() + " y " + actividad.getEdadMaxima() + " años");
        }
        if (socio.getEstadoCuota() == EstadoCuota.MOROSO) {
            throw new ReglaNegocioException("Debe regularizar su cuota en secretaría");
        }
        if (inscripcionRepository.countBySocioIdAndEstado(socioId, EstadoInscripcion.ACTIVA) >= 3) {
            throw new ReglaNegocioException("Alcanzó el máximo de 3 actividades");
        }
    }

    @Transactional(readOnly = true)
    public List<Inscripcion> listarTodas() {
        return inscripcionRepository.listarConSocioYActividad();
    }

    @Transactional(readOnly = true)
    public List<Inscripcion> listarActivasPorSocio(Long socioId) {
        if (!socioRepository.existsById(socioId)) {
            throw new RecursoNoEncontradoException("No se encontró el socio");
        }
        return inscripcionRepository.findBySocioIdAndEstado(socioId, EstadoInscripcion.ACTIVA);
    }

    @Transactional
    public Inscripcion darDeBaja(Long inscripcionId) {
        Inscripcion inscripcion = inscripcionRepository.buscarConSocioYActividad(inscripcionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró la inscripción"));
        if (inscripcion.getEstado() == EstadoInscripcion.ACTIVA) {
            inscripcion.setEstado(EstadoInscripcion.BAJA);
            return inscripcionRepository.save(inscripcion);
        }
        return inscripcion;
    }

    @Transactional(readOnly = true)
    public Inscripcion buscarPorId(Long id) {
        return inscripcionRepository.buscarConSocioYActividad(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró la inscripción"));
    }
}
