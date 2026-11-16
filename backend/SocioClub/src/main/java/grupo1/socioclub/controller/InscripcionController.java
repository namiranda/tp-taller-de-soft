package grupo1.socioclub.controller;

import grupo1.socioclub.model.InscripcionForm;
import grupo1.socioclub.service.ActividadService;
import grupo1.socioclub.service.InscripcionService;
import grupo1.socioclub.service.ReglaNegocioException;
import grupo1.socioclub.service.RecursoNoEncontradoException;
import grupo1.socioclub.service.SocioService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InscripcionController {

    private final InscripcionService inscripcionService;
    private final SocioService socioService;
    private final ActividadService actividadService;

    public InscripcionController(InscripcionService inscripcionService,
                                 SocioService socioService,
                                 ActividadService actividadService) {
        this.inscripcionService = inscripcionService;
        this.socioService = socioService;
        this.actividadService = actividadService;
    }

    @GetMapping("/inscripciones")
    public String listar(Model model) {
        model.addAttribute("inscripciones", inscripcionService.listarTodas());
        return "inscripciones/lista";
    }

    @GetMapping("/inscripciones/alta")
    public String nueva(Model model) {
        cargarOpciones(model);
        model.addAttribute("formulario", new InscripcionForm());
        return "inscripciones/formulario";
    }

    @PostMapping("/inscripciones")
    public String crear(@ModelAttribute("formulario") InscripcionForm formulario, Model model) {
        if (formulario.getSocioId() == null || formulario.getActividadId() == null) {
            cargarOpciones(model);
            model.addAttribute("error", "Seleccione un socio y una actividad");
            return "inscripciones/formulario";
        }
        try {
            inscripcionService.inscribir(formulario.getSocioId(), formulario.getActividadId());
        } catch (ReglaNegocioException | RecursoNoEncontradoException | IllegalArgumentException e) {
            cargarOpciones(model);
            model.addAttribute("error", e.getMessage());
            return "inscripciones/formulario";
        }
        return "redirect:/inscripciones";
    }

    @PostMapping("/inscripciones/{id}/baja")
    public String darDeBaja(@PathVariable Long id, RedirectAttributes atributos) {
        try {
            inscripcionService.darDeBaja(id);
            atributos.addFlashAttribute("mensaje", "Inscripción dada de baja correctamente");
        } catch (RecursoNoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return "redirect:/inscripciones";
    }

    private void cargarOpciones(Model model) {
        model.addAttribute("socios", socioService.listarTodos());
        model.addAttribute("actividades", actividadService.listarTodas());
    }
}
