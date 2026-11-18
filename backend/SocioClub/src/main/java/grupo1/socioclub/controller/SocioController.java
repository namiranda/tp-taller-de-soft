package grupo1.socioclub.controller;

import grupo1.socioclub.service.SocioService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
