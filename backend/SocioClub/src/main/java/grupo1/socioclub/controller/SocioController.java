package grupo1.socioclub.controller;

import grupo1.socioclub.model.EstadoCuota;
import grupo1.socioclub.model.Socio;
import grupo1.socioclub.service.ReglaNegocioException;
import grupo1.socioclub.service.SocioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SocioController {

    private final SocioService socioService;

    public SocioController(SocioService socioService) {
        this.socioService = socioService;
    }

    @GetMapping("/socios")
    public String listar(@RequestParam(defaultValue = "") String apellido,
                         @PageableDefault(size = 10, sort = "apellido") Pageable pageable,
                         Model model) {
        model.addAttribute("socios", socioService.listar(apellido, pageable));
        model.addAttribute("apellido", apellido);
        return "socios/lista";
    }

    @GetMapping("/socios/alta")
    public String nuevo(Model model) {
        Socio socio = new Socio();
        socio.setEstadoCuota(EstadoCuota.AL_DIA);
        return formulario(model, socio, "/socios", "Nuevo socio");
    }

    @PostMapping("/socios")
    public String crear(@Valid @ModelAttribute("socio") Socio socio, BindingResult resultado,
                        Model model, RedirectAttributes atributos) {
        if (!resultado.hasErrors()) {
            try {
                socioService.guardar(socio);
                atributos.addFlashAttribute("mensaje", "Socio creado correctamente");
                return "redirect:/socios";
            } catch (ReglaNegocioException e) {
                resultado.rejectValue("dni", "dni.duplicado", e.getMessage());
            }
        }
        return formulario(model, socio, "/socios", "Nuevo socio");
    }

    @GetMapping("/socios/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Socio socio = socioService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return formulario(model, socio, "/socios/" + id + "/editar", "Editar socio");
    }

    @PostMapping("/socios/{id}/editar")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("socio") Socio socio,
                             BindingResult resultado, Model model, RedirectAttributes atributos) {
        if (!resultado.hasErrors()) {
            try {
                if (socioService.actualizar(id, socio).isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
                atributos.addFlashAttribute("mensaje", "Socio actualizado correctamente");
                return "redirect:/socios";
            } catch (ReglaNegocioException e) {
                resultado.rejectValue("dni", "dni.duplicado", e.getMessage());
            }
        }
        return formulario(model, socio, "/socios/" + id + "/editar", "Editar socio");
    }

    private String formulario(Model model, Socio socio, String accion, String titulo) {
        model.addAttribute("socio", socio);
        model.addAttribute("accion", accion);
        model.addAttribute("titulo", titulo);
        model.addAttribute("estadosCuota", EstadoCuota.values());
        return "socios/formulario";
    }
}
