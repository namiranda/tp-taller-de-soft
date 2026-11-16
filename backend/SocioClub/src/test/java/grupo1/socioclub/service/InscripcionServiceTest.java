package grupo1.socioclub.service;

import grupo1.socioclub.model.Actividad;
import grupo1.socioclub.model.EstadoCuota;
import grupo1.socioclub.model.EstadoInscripcion;
import grupo1.socioclub.model.Inscripcion;
import grupo1.socioclub.model.Socio;
import grupo1.socioclub.repository.ActividadRepository;
import grupo1.socioclub.repository.InscripcionRepository;
import grupo1.socioclub.repository.SocioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InscripcionServiceTest {

    @Mock
    private InscripcionRepository inscripcionRepository;

    @Mock
    private SocioRepository socioRepository;

    @Mock
    private ActividadRepository actividadRepository;

    @InjectMocks
    private InscripcionService inscripcionService;

    private Socio socio;
    private Actividad actividad;

    @BeforeEach
    void prepararDatos() {
        socio = new Socio();
        socio.setId(1L);
        socio.setFechaNacimiento(LocalDate.now().minusYears(25));
        socio.setEstadoCuota(EstadoCuota.AL_DIA);

        actividad = new Actividad();
        actividad.setId(2L);
        actividad.setEdadMinima(18);
        actividad.setEdadMaxima(60);
        actividad.setCupoMaximo(10);

        lenient().when(socioRepository.findById(1L)).thenReturn(Optional.of(socio));
        lenient().when(actividadRepository.findById(2L)).thenReturn(Optional.of(actividad));
        lenient().when(inscripcionRepository.countByActividadIdAndEstado(2L, EstadoInscripcion.ACTIVA))
                .thenReturn(0L);
        lenient().when(inscripcionRepository.existsBySocioIdAndActividadIdAndEstado(
                1L, 2L, EstadoInscripcion.ACTIVA)).thenReturn(false);
        lenient().when(inscripcionRepository.countBySocioIdAndEstado(1L, EstadoInscripcion.ACTIVA))
                .thenReturn(0L);
        lenient().when(inscripcionRepository.save(any(Inscripcion.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void inscribirGuardaLaInscripcionActiva() {
        Inscripcion inscripcion = inscripcionService.inscribir(1L, 2L);

        assertEquals(socio, inscripcion.getSocio());
        assertEquals(actividad, inscripcion.getActividad());
        assertEquals(EstadoInscripcion.ACTIVA, inscripcion.getEstado());
        assertEquals(LocalDate.now(), inscripcion.getFechaInscripcion());
        verify(inscripcionRepository).save(any(Inscripcion.class));
    }

    @Test
    void rechazaCuandoLaActividadNoTieneCupo() {
        when(inscripcionRepository.countByActividadIdAndEstado(2L, EstadoInscripcion.ACTIVA))
                .thenReturn(10L);

        ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
                () -> inscripcionService.inscribir(1L, 2L));

        assertEquals("La actividad no tiene cupo disponible", error.getMessage());
    }

    @Test
    void rechazaInscripcionActivaDuplicada() {
        when(inscripcionRepository.existsBySocioIdAndActividadIdAndEstado(
                1L, 2L, EstadoInscripcion.ACTIVA)).thenReturn(true);

        ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
                () -> inscripcionService.inscribir(1L, 2L));

        assertEquals("El socio ya está inscripto en esta actividad", error.getMessage());
    }

    @Test
    void rechazaEdadFueraDelRangoPermitido() {
        socio.setFechaNacimiento(LocalDate.now().minusYears(17));

        ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
                () -> inscripcionService.inscribir(1L, 2L));

        assertEquals("La actividad admite socios de entre 18 y 60 años", error.getMessage());
    }

    @Test
    void rechazaSocioMoroso() {
        socio.setEstadoCuota(EstadoCuota.MOROSO);

        ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
                () -> inscripcionService.inscribir(1L, 2L));

        assertEquals("Debe regularizar su cuota en secretaría", error.getMessage());
    }

    @Test
    void rechazaCuandoElSocioYaTieneTresActividadesActivas() {
        when(inscripcionRepository.countBySocioIdAndEstado(1L, EstadoInscripcion.ACTIVA))
                .thenReturn(3L);

        ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
                () -> inscripcionService.inscribir(1L, 2L));

        assertEquals("Alcanzó el máximo de 3 actividades", error.getMessage());
    }

    @Test
    void permiteDarDeBajaUnaInscripcionAunqueElSocioSeaMoroso() {
        socio.setEstadoCuota(EstadoCuota.MOROSO);
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setId(3L);
        inscripcion.setSocio(socio);
        inscripcion.setEstado(EstadoInscripcion.ACTIVA);
        inscripcion.setActividad(actividad);
        when(inscripcionRepository.buscarConSocioYActividad(3L)).thenReturn(Optional.of(inscripcion));
        when(inscripcionRepository.save(inscripcion)).thenReturn(inscripcion);

        Inscripcion resultado = inscripcionService.darDeBaja(3L);

        assertEquals(EstadoInscripcion.BAJA, resultado.getEstado());
        verify(inscripcionRepository).save(inscripcion);
    }
}
