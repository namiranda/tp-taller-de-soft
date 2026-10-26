package grupo1.socioclub.controller;

import grupo1.socioclub.service.ActividadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
