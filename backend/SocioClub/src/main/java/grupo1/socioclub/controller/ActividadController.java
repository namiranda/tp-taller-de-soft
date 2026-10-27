package grupo1.socioclub.controller;

import grupo1.socioclub.model.Actividad;
import grupo1.socioclub.service.ActividadService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

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
}
