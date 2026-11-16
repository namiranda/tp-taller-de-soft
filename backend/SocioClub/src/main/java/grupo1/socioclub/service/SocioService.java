package grupo1.socioclub.service;

import grupo1.socioclub.model.Socio;
import grupo1.socioclub.repository.SocioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocioService {

    private final SocioRepository socioRepository;

    public SocioService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    public Page<Socio> listar(String apellido, Pageable pageable) {
        if (apellido == null || apellido.isBlank()) {
            return socioRepository.findAll(pageable);
        }
        return socioRepository.findByApellidoContainingIgnoreCase(apellido.trim(), pageable);
    }

    public List<Socio> listarTodos() {
        return socioRepository.findAllByOrderByApellidoAsc();
    }

    public java.util.Optional<Socio> buscarPorId(Long id) {
        return socioRepository.findById(id);
    }

    public java.util.Optional<Socio> buscarPorDni(String dni) {
        return socioRepository.findByDni(dni);
    }

    public Socio guardar(Socio socio) {
        if (socioRepository.existsByDni(socio.getDni())) {
            throw new ReglaNegocioException("Ya existe un socio con ese DNI");
        }
        return socioRepository.save(socio);
    }

    public java.util.Optional<Socio> actualizar(Long id, Socio datos) {
        if (datos.getDni() != null && socioRepository.existsByDniAndIdNot(datos.getDni(), id)) {
            throw new ReglaNegocioException("Ya existe un socio con ese DNI");
        }
        return socioRepository.findById(id).map(socio -> {
            socio.setDni(datos.getDni());
            socio.setNombre(datos.getNombre());
            socio.setApellido(datos.getApellido());
            socio.setFechaNacimiento(datos.getFechaNacimiento());
            socio.setEmail(datos.getEmail());
            socio.setTelefono(datos.getTelefono());
            socio.setEstadoCuota(datos.getEstadoCuota());
            return socioRepository.save(socio);
        });
    }

    public boolean eliminar(Long id) {
        return socioRepository.findById(id).map(socio -> {
            try {
                socioRepository.delete(socio);
            } catch (DataIntegrityViolationException e) {
                throw new ReglaNegocioException("No se puede eliminar el socio porque tiene inscripciones registradas");
            }
            return true;
        }).orElse(false);
    }
}
