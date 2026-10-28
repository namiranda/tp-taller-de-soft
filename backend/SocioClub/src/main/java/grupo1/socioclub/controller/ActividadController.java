package grupo1.socioclub.controller;

import grupo1.socioclub.model.Actividad;
import grupo1.socioclub.service.ActividadService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
}
