package grupo1.socioclub.service;

import grupo1.socioclub.model.EstadoCuota;
import grupo1.socioclub.model.Socio;
import grupo1.socioclub.repository.SocioRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SocioServiceTest {

    @Mock
    private SocioRepository socioRepository;

    @InjectMocks
    private SocioService socioService;

    private Socio nuevoSocio(String dni) {
        Socio socio = new Socio();
        socio.setDni(dni);
        socio.setNombre("Ana");
        socio.setApellido("Paz");
        socio.setFechaNacimiento(LocalDate.now().minusYears(30));
        socio.setEstadoCuota(EstadoCuota.AL_DIA);
        return socio;
    }

    @Test
    void guardaUnSocioConDniNuevo() {
        Socio socio = nuevoSocio("30111222");
        when(socioRepository.existsByDni("30111222")).thenReturn(false);
        when(socioRepository.save(socio)).thenReturn(socio);

        assertEquals(socio, socioService.guardar(socio));
    }

    @Test
    void rechazaAltaConDniDuplicado() {
        when(socioRepository.existsByDni("30111222")).thenReturn(true);

        ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
                () -> socioService.guardar(nuevoSocio("30111222")));

        assertEquals("Ya existe un socio con ese DNI", error.getMessage());
        verify(socioRepository, never()).save(any());
    }

    @Test
    void actualizaLosDatosDelSocio() {
        Socio existente = nuevoSocio("30111222");
        when(socioRepository.existsByDniAndIdNot("30111222", 1L)).thenReturn(false);
        when(socioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(socioRepository.save(existente)).thenReturn(existente);

        Socio datos = nuevoSocio("30111222");
        datos.setNombre("Beatriz");
        datos.setEstadoCuota(EstadoCuota.MOROSO);

        Socio actualizado = socioService.actualizar(1L, datos).orElseThrow();

        assertEquals("Beatriz", actualizado.getNombre());
        assertEquals(EstadoCuota.MOROSO, actualizado.getEstadoCuota());
    }

    @Test
    void rechazaEdicionConDniDeOtroSocio() {
        when(socioRepository.existsByDniAndIdNot("30111222", 1L)).thenReturn(true);

        assertThrows(ReglaNegocioException.class,
                () -> socioService.actualizar(1L, nuevoSocio("30111222")));
        verify(socioRepository, never()).save(any());
    }

    @Test
    void editarUnSocioInexistenteDevuelveVacio() {
        when(socioRepository.existsByDniAndIdNot("30111222", 9L)).thenReturn(false);
        when(socioRepository.findById(9L)).thenReturn(Optional.empty());

        assertTrue(socioService.actualizar(9L, nuevoSocio("30111222")).isEmpty());
    }

    @Test
    void validaQueElDniTenga7u8DigitosNumericos() {
        Validator validador = Validation.buildDefaultValidatorFactory().getValidator();

        assertTrue(validador.validateProperty(nuevoSocio("1234567"), "dni").isEmpty());
        assertTrue(validador.validateProperty(nuevoSocio("12345678"), "dni").isEmpty());
        assertEquals(1, validador.validateProperty(nuevoSocio("12.345.678"), "dni").size());
        assertEquals(1, validador.validateProperty(nuevoSocio("123456"), "dni").size());
        assertEquals(1, validador.validateProperty(nuevoSocio("1234567a"), "dni").size());
    }
}
