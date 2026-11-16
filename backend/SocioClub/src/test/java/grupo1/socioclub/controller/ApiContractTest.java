package grupo1.socioclub.controller;

import grupo1.socioclub.model.Actividad;
import grupo1.socioclub.model.EstadoCuota;
import grupo1.socioclub.model.Inscripcion;
import grupo1.socioclub.model.Socio;
import grupo1.socioclub.repository.ActividadRepository;
import grupo1.socioclub.repository.InscripcionRepository;
import grupo1.socioclub.repository.SocioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiContractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private SocioRepository socioRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    private Socio socio;
    private Actividad actividad;

    @BeforeEach
    void prepararDatos() {
        inscripcionRepository.deleteAllInBatch();
        socioRepository.deleteAllInBatch();
        actividadRepository.deleteAllInBatch();

        socio = new Socio();
        socio.setDni("12345678");
        socio.setNombre("Ana");
        socio.setApellido("Pérez");
        socio.setFechaNacimiento(LocalDate.now().minusYears(25));
        socio.setEstadoCuota(EstadoCuota.AL_DIA);
        socio = socioRepository.save(socio);

        actividad = new Actividad();
        actividad.setNombre("Natación");
        actividad.setDisciplina("Natación");
        actividad.setProfesor("María");
        actividad.setDias("Lun");
        actividad.setHorario(LocalTime.of(10, 0));
        actividad.setEdadMinima(18);
        actividad.setEdadMaxima(60);
        actividad.setCupoMaximo(2);
        actividad.setCuotaMensual(new BigDecimal("1000.00"));
        actividad = actividadRepository.save(actividad);
    }

    @Test
    void actividadesIncluyeElCupoDisponible() throws Exception {
        mockMvc.perform(get("/api/actividades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cupoDisponible").value(2));
    }

    @Test
    void buscaSocioPorDni() throws Exception {
        mockMvc.perform(get("/api/socios/dni/{dni}", socio.getDni()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(socio.getId()))
                .andExpect(jsonPath("$.dni").value(socio.getDni()));
    }

    @Test
    void altaYBajaDeInscripcionDevuelvenLosCodigosDelContrato() throws Exception {
        String alta = mockMvc.perform(post("/api/inscripciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"socioId\":" + socio.getId()
                                + ",\"actividadId\":" + actividad.getId() + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("ACTIVA"))
                .andReturn().getResponse().getContentAsString();
        Long inscripcionId = objectMapper.readTree(alta).get("id").asLong();

        mockMvc.perform(put("/api/inscripciones/{id}/baja", inscripcionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("BAJA"));
    }

    @Test
    void reglaDeNegocioDevuelve409ConElMensaje() throws Exception {
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setSocio(socio);
        inscripcion.setActividad(actividad);
        inscripcion.setFechaInscripcion(LocalDate.now());
        inscripcionRepository.save(inscripcion);

        mockMvc.perform(post("/api/inscripciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"socioId\":" + socio.getId()
                                + ",\"actividadId\":" + actividad.getId() + "}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("El socio ya está inscripto en esta actividad"));
    }

    @Test
    void recursosInexistentesDevuelven404() throws Exception {
        mockMvc.perform(get("/api/actividades/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("No se encontró la actividad"));
    }

    @Test
    void corsPermiteElOrigenDeReact() throws Exception {
        mockMvc.perform(options("/api/actividades")
                        .header(HttpHeaders.ORIGIN, "http://localhost:3000")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                        containsString("http://localhost:3000")));
    }

}
