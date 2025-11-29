package br.com.vinicius.oficina3d.controller;

import br.com.vinicius.oficina3d.model.Oficina;
import br.com.vinicius.oficina3d.model.User;
import br.com.vinicius.oficina3d.service.OficinaService;
import br.com.vinicius.oficina3d.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/oficinas")
public class OficinaController {

    private static final Logger log = LoggerFactory.getLogger(OficinaController.class);

    private final OficinaService service;
    private final UserService userService;

    public OficinaController(OficinaService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    // Lista todas as oficinas
    @GetMapping
    public String list(Model model) {
        log.debug("Entrando no método list de OficinaController");
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username).orElse(null);
        List<Oficina> oficinas = (user != null) ? service.findByUserId(user.getId()) : Collections.emptyList();
        log.debug("Encontradas {} oficinas para usuário {}", oficinas.size(), username);
        model.addAttribute("oficinas", oficinas);
        return "oficinas/list";
    }

    // Exibe formulário para nova oficina
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("oficina", new Oficina());
        return "oficinas/form";
    }

    // Salva oficina (nova ou editada)
    @PostMapping
    public String save(@Valid @ModelAttribute("oficina") Oficina oficina, 
                      BindingResult result, 
                      Model model,
                      RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "oficinas/form";
        }
        try {
            String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username).orElse(null);
            if (user != null) {
                oficina.setUser(user);
            }
            service.save(oficina);
            redirectAttributes.addFlashAttribute("message", "Oficina salva com sucesso!");
            return "redirect:/oficinas";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao salvar oficina: " + e.getMessage());
            return "oficinas/form";
        }
    }

    // Exibe formulário para editar oficina
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Oficina oficina = service.findById(id)
                .orElseThrow(() -> new RuntimeException("Oficina não encontrada"));
            model.addAttribute("oficina", oficina);
            return "oficinas/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Oficina não encontrada!");
            return "redirect:/oficinas";
        }
    }

    // Deleta oficina
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            service.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Oficina excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao excluir oficina: " + e.getMessage());
        }
        return "redirect:/oficinas";
    }
}