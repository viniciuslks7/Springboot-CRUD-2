package br.com.vinicius.oficina3d.controller;

import br.com.vinicius.oficina3d.model.Maquina;
import br.com.vinicius.oficina3d.service.MaquinaService;
import br.com.vinicius.oficina3d.service.OficinaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/maquinas")
public class MaquinaController {

    private final MaquinaService maquinaService;
    private final OficinaService oficinaService;

    public MaquinaController(MaquinaService maquinaService, OficinaService oficinaService) {
        this.maquinaService = maquinaService;
        this.oficinaService = oficinaService;
    }

    @GetMapping
    public String list(Model model, @RequestParam(required=false) Long oficinaId) {
            System.out.println("Renderizando máquinas!");
        if (oficinaId != null) {
            model.addAttribute("maquinas", maquinaService.findByOficinaId(oficinaId));
        } else {
            model.addAttribute("maquinas", maquinaService.findAll());
        }
        model.addAttribute("oficinas", oficinaService.findAll());
        model.addAttribute("content", "maquinas/list.html :: content");
        return "base";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("maquina", new Maquina());
        model.addAttribute("oficinas", oficinaService.findAll());
        model.addAttribute("content", "maquinas/form.html :: content");
        return "base";
    }

    @PostMapping
    public String save(@ModelAttribute Maquina maquina) {
        maquinaService.save(maquina);
        return "redirect:/maquinas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        maquinaService.findById(id).ifPresent(m -> model.addAttribute("maquina", m));
        model.addAttribute("oficinas", oficinaService.findAll());
        model.addAttribute("content", "maquinas/form.html :: content");
        return "base";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        maquinaService.deleteById(id);
        return "redirect:/maquinas";
    }
}