package br.com.vinicius.oficina3d.controller;

import br.com.vinicius.oficina3d.model.Maquina;
import br.com.vinicius.oficina3d.model.User;
import br.com.vinicius.oficina3d.service.MaquinaService;
import br.com.vinicius.oficina3d.service.OficinaService;
import br.com.vinicius.oficina3d.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/maquinas")
public class MaquinaController {

    private final MaquinaService maquinaService;
    private final OficinaService oficinaService;
    private final UserService userService;

    public MaquinaController(MaquinaService maquinaService, OficinaService oficinaService, UserService userService) {
        this.maquinaService = maquinaService;
        this.oficinaService = oficinaService;
        this.userService = userService;
    }

    // Lista todas as máquinas
    @GetMapping
    public String list(Model model, @RequestParam(required=false) Long oficinaId) {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username).orElse(null);
        if (oficinaId != null) {
            model.addAttribute("maquinas", maquinaService.findByOficinaId(oficinaId));
        } else {
            model.addAttribute("maquinas", (user != null) ? maquinaService.findByUserId(user.getId()) : java.util.Collections.emptyList());
        }
        model.addAttribute("oficinas", oficinaService.findAll());
        return "maquinas/list";
    }

    // Exibe formulário para nova máquina
    @GetMapping("/new")
    public String newForm(Model model) {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username).orElse(null);
        model.addAttribute("maquina", new Maquina());
        model.addAttribute("oficinas", (user != null) ? oficinaService.findByUserId(user.getId()) : java.util.Collections.emptyList());
        return "maquinas/form";
    }

    // Salva máquina (nova ou editada)
    @PostMapping
    public String save(@Valid @ModelAttribute Maquina maquina, 
                      BindingResult result, 
                      Model model,
                      RedirectAttributes redirectAttributes) {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username).orElse(null);
        if (result.hasErrors()) {
            model.addAttribute("oficinas", (user != null) ? oficinaService.findByUserId(user.getId()) : java.util.Collections.emptyList());
            return "maquinas/form";
        }
        try {
            if (user != null) {
                maquina.setUser(user);
            }
            maquinaService.save(maquina);
            redirectAttributes.addFlashAttribute("message", "Máquina salva com sucesso!");
            return "redirect:/maquinas";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao salvar máquina: " + e.getMessage());
            model.addAttribute("oficinas", (user != null) ? oficinaService.findByUserId(user.getId()) : java.util.Collections.emptyList());
            return "maquinas/form";
        }
    }

    // Exibe formulário para editar máquina
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username).orElse(null);
        try {
            maquinaService.findById(id).ifPresent(m -> model.addAttribute("maquina", m));
            model.addAttribute("oficinas", (user != null) ? oficinaService.findByUserId(user.getId()) : java.util.Collections.emptyList());
            return "maquinas/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Máquina não encontrada!");
            return "redirect:/maquinas";
        }
    }

    // Deleta máquina
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            maquinaService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Máquina excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao excluir máquina: " + e.getMessage());
        }
        return "redirect:/maquinas";
    }
}