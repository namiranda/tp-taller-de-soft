package grupo1.socioclub.controller;

import grupo1.socioclub.api.dto.InscripcionResponse;
import grupo1.socioclub.api.dto.SocioResponse;
import grupo1.socioclub.model.Socio;
import grupo1.socioclub.service.InscripcionService;
import grupo1.socioclub.service.RecursoNoEncontradoException;
import grupo1.socioclub.service.SocioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/socios")
public class ApiSocioController {

    private final SocioService socioService;
    private final InscripcionService inscripcionService;

    public ApiSocioController(SocioService socioService, InscripcionService inscripcionService) {
        this.socioService = socioService;
        this.inscripcionService = inscripcionService;
    }

    @GetMapping("/dni/{dni}")
    public SocioResponse buscarPorDni(@PathVariable String dni) {
        Socio socio = socioService.buscarPorDni(dni)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el socio"));
        return SocioResponse.desde(socio);
    }

    @GetMapping("/{id}/inscripciones")
    public List<InscripcionResponse> listarInscripcionesActivas(@PathVariable Long id) {
        return inscripcionService.listarActivasPorSocio(id).stream()
                .map(InscripcionResponse::desde).toList();
    }
}
