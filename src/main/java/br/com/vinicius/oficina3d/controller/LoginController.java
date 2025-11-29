package br.com.vinicius.oficina3d.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    // Página inicial (index)
    @GetMapping("/")
    public String index() {
        log.info("➡️  Acessando página inicial (index)");
        return "index";
    }

    @GetMapping("/index")
    public String indexAlternative() {
        log.info("➡️  Acessando /index");
        return "index";
    }

    // Página de login
    @GetMapping("/login")
    public String loginPage() {
        log.info("➡️  Acessando página de login");
        return "login";
    }
}