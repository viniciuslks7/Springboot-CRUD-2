package br.com.vinicius.oficina3d.controller;

import br.com.vinicius.oficina3d.model.Maquina;
import br.com.vinicius.oficina3d.model.Oficina;
import br.com.vinicius.oficina3d.service.MaquinaService;
import br.com.vinicius.oficina3d.service.OficinaService;
import br.com.vinicius.oficina3d.service.UserService;
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
    private final UserService userService;

    public HomeController(OficinaService oficinaService, MaquinaService maquinaService, UserService userService) {
        this.oficinaService = oficinaService;
        this.maquinaService = maquinaService;
        this.userService = userService;
    }

    @GetMapping({"/home"})
    public String home(Model model) {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        br.com.vinicius.oficina3d.model.User user = userService.findByUsername(username).orElse(null);
        List<Oficina> oficinas = (user != null) ? oficinaService.findByUserId(user.getId()) : Collections.emptyList();
        List<Maquina> maquinas = (user != null) ? maquinaService.findByUserId(user.getId()) : Collections.emptyList();

        model.addAttribute("title", "Dashboard — Oficina3D");
        model.addAttribute("oficinas", oficinas);
        model.addAttribute("maquinas", maquinas);
        return "home";
    }
}
