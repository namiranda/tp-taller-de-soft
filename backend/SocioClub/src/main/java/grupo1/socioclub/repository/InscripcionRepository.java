package grupo1.socioclub.repository;

import grupo1.socioclub.model.EstadoInscripcion;
import grupo1.socioclub.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    List<Inscripcion> findBySocioIdAndEstado(Long socioId, EstadoInscripcion estado);

    boolean existsBySocioIdAndActividadIdAndEstado(Long socioId, Long actividadId, EstadoInscripcion estado);

    long countBySocioIdAndEstado(Long socioId, EstadoInscripcion estado);
}
