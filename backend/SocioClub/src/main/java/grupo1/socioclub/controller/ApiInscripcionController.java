package grupo1.socioclub.controller;

import grupo1.socioclub.api.dto.InscripcionResponse;
import grupo1.socioclub.model.InscripcionForm;
import grupo1.socioclub.service.InscripcionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/inscripciones")
public class ApiInscripcionController {

    private final InscripcionService inscripcionService;

    public ApiInscripcionController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @PostMapping
    public ResponseEntity<InscripcionResponse> crear(@Valid @RequestBody InscripcionForm formulario) {
        var inscripcion = inscripcionService.inscribir(formulario.getSocioId(), formulario.getActividadId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(inscripcion.getId()).toUri();
        return ResponseEntity.created(location).body(InscripcionResponse.desde(inscripcion));
    }

    @PutMapping("/{id}/baja")
    public InscripcionResponse darDeBaja(@PathVariable Long id) {
        return InscripcionResponse.desde(inscripcionService.darDeBaja(id));
    }
}
