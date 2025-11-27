package br.com.vinicius.oficina3d.controller;

import br.com.vinicius.oficina3d.model.Maquina;
import br.com.vinicius.oficina3d.model.Oficina;
import br.com.vinicius.oficina3d.service.MaquinaService;
import br.com.vinicius.oficina3d.service.OficinaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    private final OficinaService oficinaService;
    private final MaquinaService maquinaService;

    public HomeController(OficinaService oficinaService, MaquinaService maquinaService) {
        this.oficinaService = oficinaService;
        this.maquinaService = maquinaService;
    }

    @GetMapping({"/home"})
    public String home(Model model) {
        List<Oficina> oficinas = oficinaService.findAll();
        List<Maquina> maquinas = maquinaService.findAll();

        if (oficinas == null) oficinas = Collections.emptyList();
        if (maquinas == null) maquinas = Collections.emptyList();

        model.addAttribute("title", "Dashboard — Oficina3D"); // usado pelo fragments/head
        model.addAttribute("oficinas", oficinas);
        model.addAttribute("maquinas", maquinas);

        // se usa base.html com content dinâmico, poderia ser:
        // model.addAttribute("content", "home :: content");
        // return "base";
        // Mas como seu home.html é standalone, só retorne "home"
        return "home";
    }
}
