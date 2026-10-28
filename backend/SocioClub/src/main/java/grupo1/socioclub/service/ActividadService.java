package grupo1.socioclub.service;

import grupo1.socioclub.model.Actividad;
import grupo1.socioclub.repository.ActividadRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActividadService {

    private final ActividadRepository actividadRepository;

    public ActividadService(ActividadRepository actividadRepository) {
        this.actividadRepository = actividadRepository;
    }

    public List<Actividad> listarTodas() {
        return actividadRepository.findAll(Sort.by("nombre"));
    }

    public Optional<Actividad> eliminar(Long id) {
        return actividadRepository.findById(id).map(actividad -> {
            if (actividadRepository.contarInscripcionesActivas(id) > 0) {
                throw new ReglaNegocioException("No se puede eliminar la actividad porque tiene inscriptos activos");
            }
            try {
                actividadRepository.delete(actividad);
            } catch (DataIntegrityViolationException e) {
                throw new ReglaNegocioException("No se puede eliminar la actividad porque tiene inscripciones registradas");
            }
            return actividad;
        });
    }
}
