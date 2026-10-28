package grupo1.socioclub.service;

import grupo1.socioclub.model.Actividad;
import grupo1.socioclub.repository.ActividadRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActividadService {

    private final ActividadRepository actividadRepository;

    public ActividadService(ActividadRepository actividadRepository) {
        this.actividadRepository = actividadRepository;
    }

    public List<Actividad> listarTodas() {
        return actividadRepository.findAll(Sort.by("nombre"));
    }

    public Actividad guardar(Actividad actividad) {
        return actividadRepository.save(actividad);
    }
}
