package grupo1.socioclub.controller;

import grupo1.socioclub.service.ActividadService;
import grupo1.socioclub.service.ReglaNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
