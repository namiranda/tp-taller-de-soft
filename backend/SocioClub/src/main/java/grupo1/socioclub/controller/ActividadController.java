package grupo1.socioclub.controller;

import grupo1.socioclub.model.Actividad;
import grupo1.socioclub.service.ActividadService;
import grupo1.socioclub.service.ReglaNegocioException;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ActividadController {

    private final ActividadService actividadService;

    public ActividadController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/actividades";
    }

    @GetMapping("/actividades")
    public String listar(Model model) {
        model.addAttribute("actividades", actividadService.listarTodas());
        return "actividades/lista";
    }

    @GetMapping("/actividades/alta")
    public String nueva(Model model) {
        model.addAttribute("actividad", new Actividad());
        return "actividades/formulario";
    }

    @PostMapping("/actividades")
    public String crear(@Valid @ModelAttribute Actividad actividad, BindingResult resultado) {
        if (actividad.getEdadMinima() != null
                && actividad.getEdadMaxima() != null
                && actividad.getEdadMinima() > actividad.getEdadMaxima()) {
            resultado.rejectValue("edadMinima", "edad.rango",
                    "La edad mínima no puede ser mayor que la edad máxima");
        }

        if (resultado.hasErrors()) {
            return "actividades/formulario";
        }

        actividadService.guardar(actividad);
        return "redirect:/actividades";
    }

    @GetMapping("/actividades/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("actividad", actividadService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return "actividades/editar";
    }

    @PostMapping("/actividades/{id}/editar")
    public String actualizar(@PathVariable Long id, @ModelAttribute Actividad actividad) {
        if (actividadService.actualizar(id, actividad).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/actividades";
    }

    @PostMapping("/actividades/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes atributos) {
        try {
            if (actividadService.eliminar(id).isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            atributos.addFlashAttribute("mensaje", "Actividad eliminada correctamente");
        } catch (ReglaNegocioException e) {
            atributos.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/actividades";
    }
}
