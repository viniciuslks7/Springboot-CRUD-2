package br.com.vinicius.oficina3d.controller;

import br.com.vinicius.oficina3d.model.Oficina;
import br.com.vinicius.oficina3d.service.OficinaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/oficinas")
public class OficinaController {

    private final OficinaService service;

    public OficinaController(OficinaService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("oficinas", service.findAll());
        model.addAttribute("content", "oficinas/list.html :: content");
        return "base";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("oficina", new Oficina());
        model.addAttribute("content", "oficinas/form.html :: content");
        return "base";
    }

    @PostMapping
    public String save(@ModelAttribute Oficina oficina) {
        service.save(oficina);
        return "redirect:/oficinas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        service.findById(id).ifPresent(o -> model.addAttribute("oficina", o));
        model.addAttribute("content", "oficinas/form.html :: content");
        return "base";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/oficinas";
    }
}