package br.com.vinicius.oficina3d.controller;

import br.com.vinicius.oficina3d.service.OficinaService;
import br.com.vinicius.oficina3d.service.MaquinaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final OficinaService oficinaService;
    private final MaquinaService maquinaService;

    public HomeController(OficinaService oficinaService, MaquinaService maquinaService) {
        this.oficinaService = oficinaService;
        this.maquinaService = maquinaService;
    }

    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("oficinas", oficinaService.findAll());
        model.addAttribute("maquinas", maquinaService.findAll());
        return "home";
    }
}