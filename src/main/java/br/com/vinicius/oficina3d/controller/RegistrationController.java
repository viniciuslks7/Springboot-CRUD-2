package br.com.vinicius.oficina3d.controller;

import br.com.vinicius.oficina3d.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {
    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        log.info("➡️  Entrou no GET /register");
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@RequestParam String fullName,
                                      @RequestParam String username,
                                      @RequestParam String email,
                                      @RequestParam String password,
                                      @RequestParam String confirmPassword,
                                      Model model) {
        log.info("➡️  POST /register: username={}, email={}", username, email);

        // Validation
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "As senhas não coincidem.");
            return "register";
        }

        if (password.length() < 6) {
            model.addAttribute("error", "A senha deve ter pelo menos 6 caracteres.");
            return "register";
        }

        try {
            // Register user
            userService.registerUser(username, password, fullName, email);
            log.info("✅ Usuário registrado com sucesso: {}", username);
            return "redirect:/login?registered";
        } catch (RuntimeException e) {
            log.error("❌ Erro ao registrar usuário: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
