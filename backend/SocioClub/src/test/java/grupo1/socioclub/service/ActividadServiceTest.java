package grupo1.socioclub.service;

import grupo1.socioclub.model.Actividad;
import grupo1.socioclub.repository.ActividadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActividadServiceTest {

    @Mock
    private ActividadRepository actividadRepository;

    @InjectMocks
    private ActividadService actividadService;

    @Test
    void eliminarBorraLaActividadSinInscriptosActivos() {
        Actividad actividad = new Actividad();
        when(actividadRepository.findById(1L)).thenReturn(Optional.of(actividad));
        when(actividadRepository.contarInscripcionesActivas(1L)).thenReturn(0L);

        actividadService.eliminar(1L);

        verify(actividadRepository).delete(actividad);
    }

    @Test
    void eliminarRechazaLaActividadConInscriptosActivos() {
        when(actividadRepository.findById(1L)).thenReturn(Optional.of(new Actividad()));
        when(actividadRepository.contarInscripcionesActivas(1L)).thenReturn(3L);

        assertThrows(ReglaNegocioException.class, () -> actividadService.eliminar(1L));

        verify(actividadRepository, never()).delete(any());
    }

    @Test
    void eliminarRechazaLaActividadConHistorialDeInscripciones() {
        Actividad actividad = new Actividad();
        when(actividadRepository.findById(1L)).thenReturn(Optional.of(actividad));
        when(actividadRepository.contarInscripcionesActivas(1L)).thenReturn(0L);
        doThrow(DataIntegrityViolationException.class).when(actividadRepository).delete(actividad);

        assertThrows(ReglaNegocioException.class, () -> actividadService.eliminar(1L));
    }

    @Test
    void eliminarDevuelveVacioSiLaActividadNoExiste() {
        when(actividadRepository.findById(99L)).thenReturn(Optional.empty());

        assertTrue(actividadService.eliminar(99L).isEmpty());

        verify(actividadRepository, never()).delete(any());
    }
}
