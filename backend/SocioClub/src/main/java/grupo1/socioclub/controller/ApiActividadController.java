package grupo1.socioclub.controller;

import grupo1.socioclub.api.dto.ActividadResponse;
import grupo1.socioclub.model.Actividad;
import grupo1.socioclub.service.ActividadService;
import grupo1.socioclub.service.RecursoNoEncontradoException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/actividades")
public class ApiActividadController {

    private final ActividadService actividadService;

    public ApiActividadController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    @GetMapping
    public List<ActividadResponse> listar(@RequestParam(required = false) String disciplina) {
        List<Actividad> actividades = disciplina == null || disciplina.isBlank()
                ? actividadService.listarTodas()
                : actividadService.listarPorDisciplina(disciplina.trim());
        return actividades.stream().map(this::aResponse).toList();
    }

    @GetMapping("/{id}")
    public ActividadResponse buscar(@PathVariable Long id) {
        Actividad actividad = actividadService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró la actividad"));
        return aResponse(actividad);
    }

    @PostMapping
    public ResponseEntity<ActividadResponse> crear(@Valid @RequestBody Actividad actividad) {
        validarRangoEdades(actividad);
        Actividad guardada = actividadService.guardar(actividad);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(guardada.getId()).toUri();
        return ResponseEntity.created(location).body(aResponse(guardada));
    }

    @PutMapping("/{id}")
    public ActividadResponse actualizar(@PathVariable Long id, @Valid @RequestBody Actividad datos) {
        validarRangoEdades(datos);
        Actividad actualizada = actividadService.actualizar(id, datos)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró la actividad"));
        return aResponse(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (actividadService.eliminar(id).isEmpty()) {
            throw new RecursoNoEncontradoException("No se encontró la actividad");
        }
        return ResponseEntity.noContent().build();
    }

    private void validarRangoEdades(Actividad actividad) {
        if (actividad.getEdadMinima() != null && actividad.getEdadMaxima() != null
                && actividad.getEdadMinima() > actividad.getEdadMaxima()) {
            throw new IllegalArgumentException("La edad mínima no puede ser mayor que la edad máxima");
        }
    }

    private ActividadResponse aResponse(Actividad actividad) {
        return ActividadResponse.desde(actividad, actividadService.contarInscripcionesActivas(actividad.getId()));
    }
}
