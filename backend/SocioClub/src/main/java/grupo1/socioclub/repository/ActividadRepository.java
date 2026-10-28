package grupo1.socioclub.repository;

import grupo1.socioclub.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    @Query(value = "SELECT COUNT(*) FROM inscripcion WHERE actividad_id = :actividadId AND estado = 'ACTIVA'",
            nativeQuery = true)
    long contarInscripcionesActivas(@Param("actividadId") Long actividadId);
}
