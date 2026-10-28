package grupo1.socioclub.service;

import grupo1.socioclub.model.Actividad;
import grupo1.socioclub.repository.ActividadRepository;
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

    public Actividad guardar(Actividad actividad) {
        return actividadRepository.save(actividad);
    }

    public Optional<Actividad> buscarPorId(Long id) {
        return actividadRepository.findById(id);
    }

    public Optional<Actividad> actualizar(Long id, Actividad datos) {
        return actividadRepository.findById(id).map(actividad -> {
            actividad.setNombre(datos.getNombre());
            actividad.setDisciplina(datos.getDisciplina());
            actividad.setProfesor(datos.getProfesor());
            actividad.setDias(datos.getDias());
            actividad.setHorario(datos.getHorario());
            actividad.setEdadMinima(datos.getEdadMinima());
            actividad.setEdadMaxima(datos.getEdadMaxima());
            actividad.setCupoMaximo(datos.getCupoMaximo());
            actividad.setCuotaMensual(datos.getCuotaMensual());
            return actividadRepository.save(actividad);
        });
    }
}
