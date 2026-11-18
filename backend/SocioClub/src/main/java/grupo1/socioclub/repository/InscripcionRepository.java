package grupo1.socioclub.repository;

import grupo1.socioclub.model.EstadoInscripcion;
import grupo1.socioclub.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    @Query("SELECT i FROM Inscripcion i JOIN FETCH i.socio JOIN FETCH i.actividad "
            + "WHERE i.socio.id = :socioId AND i.estado = :estado "
            + "ORDER BY i.fechaInscripcion DESC, i.id DESC")
    List<Inscripcion> findBySocioIdAndEstado(@Param("socioId") Long socioId,
                                             @Param("estado") EstadoInscripcion estado);

    boolean existsBySocioIdAndActividadIdAndEstado(Long socioId, Long actividadId, EstadoInscripcion estado);

    long countBySocioIdAndEstado(Long socioId, EstadoInscripcion estado);

    long countByActividadIdAndEstado(Long actividadId, EstadoInscripcion estado);

    @Query("SELECT i FROM Inscripcion i JOIN FETCH i.socio JOIN FETCH i.actividad "
            + "ORDER BY i.fechaInscripcion DESC, i.id DESC")
    List<Inscripcion> listarConSocioYActividad();

    @Query("SELECT i FROM Inscripcion i JOIN FETCH i.socio JOIN FETCH i.actividad WHERE i.id = :id")
    Optional<Inscripcion> buscarConSocioYActividad(@Param("id") Long id);
}
