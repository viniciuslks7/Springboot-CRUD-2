package br.com.vinicius.oficina3d.controller;

import br.com.vinicius.oficina3d.model.Oficina;
import br.com.vinicius.oficina3d.service.OficinaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/oficinas")
public class OficinaController {

    private static final Logger log = LoggerFactory.getLogger(OficinaController.class);

    private final OficinaService service;

    public OficinaController(OficinaService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        log.debug("Entrando no método list de OficinaController");
        List<Oficina> oficinas = service.findAll();
        if (oficinas == null) {
            oficinas = Collections.emptyList();
            log.warn("Oficinas retornou null do service; usando lista vazia como fallback.");
        }
        log.debug("Encontradas {} oficinas", oficinas.size());
        model.addAttribute("oficinas", oficinas);
        // atributo content sempre definido para base.html
        model.addAttribute("content", "oficinas/list :: content");
        log.debug("Renderizando base com content={}", "oficinas/list.html :: content");
        return "base";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("oficina", new Oficina());
        model.addAttribute("content", "oficinas/form :: content");
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
        model.addAttribute("content", "oficinas/form :: content");
        return "base";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/oficinas";
    }
}
