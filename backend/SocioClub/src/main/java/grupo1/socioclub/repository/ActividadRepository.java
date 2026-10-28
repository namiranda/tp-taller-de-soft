package grupo1.socioclub.repository;

import grupo1.socioclub.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    // Metodo derivado: SELECT ... FROM actividad WHERE disciplina = ?
    List<Actividad> findByDisciplina(String disciplina);

    @Query("SELECT COUNT(i) FROM Inscripcion i "
            + "WHERE i.actividad.id = :actividadId "
            + "AND i.estado = grupo1.socioclub.model.EstadoInscripcion.ACTIVA")
    long contarInscripcionesActivas(@Param("actividadId") Long actividadId);
}
