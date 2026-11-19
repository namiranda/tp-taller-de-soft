package grupo1.socioclub.controller;

import grupo1.socioclub.model.EstadoCuota;
import grupo1.socioclub.model.Socio;
import grupo1.socioclub.repository.InscripcionRepository;
import grupo1.socioclub.repository.SocioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SocioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SocioRepository socioRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    private Socio existente;

    @BeforeEach
    void prepararDatos() {
        inscripcionRepository.deleteAllInBatch();
        socioRepository.deleteAllInBatch();

        existente = new Socio();
        existente.setDni("30111222");
        existente.setNombre("Ana");
        existente.setApellido("Paz");
        existente.setFechaNacimiento(LocalDate.of(1990, 5, 20));
        existente.setEstadoCuota(EstadoCuota.AL_DIA);
        existente = socioRepository.save(existente);
    }

    private MockHttpServletRequestBuilder formulario(MockHttpServletRequestBuilder peticion, String dni) {
        return peticion
                .param("dni", dni)
                .param("nombre", "Luis")
                .param("apellido", "Gomez")
                .param("fechaNacimiento", "1985-03-10")
                .param("email", "luis@example.com")
                .param("telefono", "3511234567")
                .param("estadoCuota", "MOROSO");
    }

    @Test
    void muestraElFormularioDeAlta() throws Exception {
        mockMvc.perform(get("/socios/alta"))
                .andExpect(status().isOk())
                .andExpect(view().name("socios/formulario"))
                .andExpect(content().string(containsString("Nuevo socio")));
    }

    @Test
    void creaUnSocioValido() throws Exception {
        mockMvc.perform(formulario(post("/socios"), "40222333"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/socios"));

        assertEquals(EstadoCuota.MOROSO, socioRepository.findByDni("40222333").orElseThrow().getEstadoCuota());
    }

    @Test
    void rechazaUnDniConPuntosSinGuardar() throws Exception {
        mockMvc.perform(formulario(post("/socios"), "40.222.333"))
                .andExpect(status().isOk())
                .andExpect(view().name("socios/formulario"))
                .andExpect(content().string(containsString("El DNI debe tener 7 u 8 dígitos, sin puntos")));

        assertEquals(1, socioRepository.count());
    }

    @Test
    void rechazaUnDniDuplicado() throws Exception {
        mockMvc.perform(formulario(post("/socios"), "30111222"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Ya existe un socio con ese DNI")));

        assertEquals(1, socioRepository.count());
    }

    @Test
    void muestraElFormularioDeEdicionConLosDatosActuales() throws Exception {
        mockMvc.perform(get("/socios/{id}/editar", existente.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Editar socio")))
                .andExpect(content().string(containsString("30111222")));
    }

    @Test
    void actualizaUnSocio() throws Exception {
        mockMvc.perform(formulario(post("/socios/{id}/editar", existente.getId()), "30111222"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/socios"));

        Socio actualizado = socioRepository.findById(existente.getId()).orElseThrow();
        assertEquals("Luis", actualizado.getNombre());
        assertEquals(EstadoCuota.MOROSO, actualizado.getEstadoCuota());
    }

    @Test
    void editarUnSocioInexistenteDevuelve404() throws Exception {
        mockMvc.perform(get("/socios/{id}/editar", 99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void elListadoMuestraBotonesDeAltaYEdicion() throws Exception {
        mockMvc.perform(get("/socios"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/socios/alta")))
                .andExpect(content().string(containsString("/socios/" + existente.getId() + "/editar")));
    }
}
