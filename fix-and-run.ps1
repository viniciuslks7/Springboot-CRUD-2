# fix-and-run.ps1
# Execute no diretório raiz do projeto (ex: C:\Users\LAB\Desktop\Spring MVC)

$root = Get-Location
Write-Host "Executando em: $root" -ForegroundColor Cyan

# --- Paths ---
$srcResources = "src\main\resources"
$templatesDir = Join-Path $srcResources "templates"
$controllerDir = "src\main\java\br\com\vinicius\oficina3d\controller"
$configDir = "src\main\java\br\com\vinicius\oficina3d\config"
$appProps = Join-Path $srcResources "application.properties"

# --- Ensure directories exist ---
New-Item -ItemType Directory -Force -Path $templatesDir | Out-Null
New-Item -ItemType Directory -Force -Path $controllerDir | Out-Null
New-Item -ItemType Directory -Force -Path $configDir | Out-Null

function Backup-IfExists {
    param($path)
    if (Test-Path $path) {
        $bak = "$path.bak"
        Write-Host "Backup: $path -> $bak"
        Copy-Item -Force -Path $path -Destination $bak
    }
}

# --- Backup common files ---
Backup-IfExists -path $appProps
Backup-IfExists -path (Join-Path $templatesDir "login.html")
Backup-IfExists -path (Join-Path $templatesDir "register.html")
Backup-IfExists -path (Join-Path $controllerDir "LoginController.java")
Backup-IfExists -path (Join-Path $controllerDir "RegistrationController.java")
Backup-IfExists -path (Join-Path $configDir "SecurityConfig.java")

# --- application.properties (logging debug) ---
$appPropsContent = @"
# App properties (added by fix-and-run.ps1)
spring.thymeleaf.cache=false

# Debug logs for diagnosing redirect loops
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.springframework.boot.autoconfigure.web.servlet.error=DEBUG
"@

Set-Content -Path $appProps -Value $appPropsContent -Encoding UTF8
Write-Host "Wrote: $appProps" -ForegroundColor Green

# --- SecurityConfig.java (safe default + logging) ---
$securityConfigPath = Join-Path $configDir "SecurityConfig.java"
$securityConfigContent = @'
package br.com.vinicius.oficina3d.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("🔐 SecurityConfig carregado — iniciando configuração da chain");

        http
            .authorizeHttpRequests(auth -> {
                System.out.println("🔐 Configurando rotas permitidas...");
                auth
                    .requestMatchers(
                        "/", "/index",
                        "/login", "/register",
                        "/css/**", "/js/**", "/img/**",
                        "/webjars/**", "/h2-console/**"
                    ).permitAll()
                    .anyRequest().authenticated();
            })

            .formLogin(form -> {
                System.out.println("🔐 Configuração do login form ativada");
                form.loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/", true)
                    .permitAll();
            })

            .logout(logout -> {
                System.out.println("🔐 Configuração do logout ativada");
                logout.logoutUrl("/logout")
                      .logoutSuccessUrl("/login?logout")
                      .permitAll();
            });

        // H2 console
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        System.out.println("🔐 SecurityConfig finalizado");
        return http.build();
    }
}
'@

Set-Content -Path $securityConfigPath -Value $securityConfigContent -Encoding UTF8
Write-Host "Wrote: $securityConfigPath" -ForegroundColor Green

# --- LoginController.java ---
$loginControllerPath = Join-Path $controllerDir "LoginController.java"
$loginControllerContent = @'
package br.com.vinicius.oficina3d.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @GetMapping({"/login"})
    public String loginPage() {
        log.info("➡️  Entrou no GET /login");
        return "login";
    }
}
'@

Set-Content -Path $loginControllerPath -Value $loginControllerContent -Encoding UTF8
Write-Host "Wrote: $loginControllerPath" -ForegroundColor Green

# --- RegistrationController.java (simple scaffold) ---
$registrationControllerPath = Join-Path $controllerDir "RegistrationController.java"
$registrationControllerContent = @'
package br.com.vinicius.oficina3d.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

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

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "As senhas não coincidem.");
            return "register";
        }

        // TODO: registrar usuário (service + bcrypt)
        // por ora redireciona ao login com parâmetro
        return "redirect:/login?registered";
    }
}
'@

Set-Content -Path $registrationControllerPath -Value $registrationControllerContent -Encoding UTF8
Write-Host "Wrote: $registrationControllerPath" -ForegroundColor Green

# --- login.html (add CSRF hidden input) ---
$loginHtmlPath = Join-Path $templatesDir "login.html"
$loginHtmlContent = @'
<!DOCTYPE html>
<html lang="pt-BR" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Login - Oficina3D</title>

    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://unpkg.com/flowbite@1.7.0/dist/flowbite.min.css" />
    <script src="https://unpkg.com/flowbite@1.7.0/dist/flowbite.js"></script>
</head>
<body class="bg-gray-100 flex items-center justify-center min-h-screen">

    <div class="w-full max-w-md p-8 bg-white rounded-xl shadow-lg">
        <h1 class="text-3xl font-bold text-center text-sky-600 mb-6">Oficina3D — Login</h1>

        <div th:if="${param.error}" class="mb-4 px-4 py-3 rounded bg-red-100 text-red-700 text-sm">Usuário ou senha incorretos.</div>
        <div th:if="${param.logout}" class="mb-4 px-4 py-3 rounded bg-green-100 text-green-700 text-sm">Logout realizado com sucesso.</div>
        <div th:if="${param.registered}" class="mb-4 px-4 py-3 rounded bg-green-50 text-green-800 text-sm">Conta criada com sucesso. Faça login.</div>

        <form th:action="@{/login}" method="post" class="space-y-4">
            <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}" />
            <div>
                <label class="block text-sm font-medium mb-1">Usuário</label>
                <input type="text" name="username" class="w-full px-3 py-2 border rounded shadow-sm focus:outline-none" placeholder="Digite seu usuário" required />
            </div>

            <div>
                <label class="block text-sm font-medium mb-1">Senha</label>
                <input type="password" name="password" class="w-full px-3 py-2 border rounded shadow-sm focus:outline-none" placeholder="Digite sua senha" required />
            </div>

            <button type="submit" class="w-full py-2 mt-2 bg-sky-600 text-white rounded hover:bg-sky-700 transition">Entrar</button>
        </form>

        <div class="text-center mt-4 text-sm text-gray-600">Não tem conta? <a th:href="@{/register}" class="text-sky-600 font-medium">Crie uma aqui</a></div>

        <p class="text-center text-sm text-gray-600 mt-6">© <span th:text="${#dates.format(#dates.createNow(),'yyyy')}"></span> Oficina3D — Vinicius</p>
    </div>

</body>
</html>
'@

Set-Content -Path $loginHtmlPath -Value $loginHtmlContent -Encoding UTF8
Write-Host "Wrote: $loginHtmlPath" -ForegroundColor Green

# --- register.html (ensure CSRF hidden input exists) ---
$registerHtmlPath = Join-Path $templatesDir "register.html"
$registerHtmlContent = @'
<!DOCTYPE html>
<html lang="pt-BR" xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <title>Cadastro - Oficina3D</title>

  <script src="https://cdn.tailwindcss.com"></script>
  <link rel="stylesheet" href="https://unpkg.com/flowbite@1.7.0/dist/flowbite.min.css" />
  <script src="https://unpkg.com/flowbite@1.7.0/dist/flowbite.js"></script>
</head>
<body class="min-h-screen bg-gray-100 flex items-center justify-center">
  <div class="w-full max-w-lg bg-white rounded-xl shadow-lg p-8">
    <h2 class="text-2xl font-bold text-sky-600 mb-6 text-center">Criar Conta — Oficina3D</h2>

    <form th:action="@{/register}" method="post" class="space-y-4">
      <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}" />

      <div>
        <label class="block text-sm font-medium mb-1">Nome completo</label>
        <input name="fullName" required class="w-full px-3 py-2 border rounded focus:outline-none" placeholder="Seu nome completo" />
      </div>

      <div>
        <label class="block text-sm font-medium mb-1">Usuário</label>
        <input name="username" required class="w-full px-3 py-2 border rounded focus:outline-none" placeholder="nome_de_usuario" />
      </div>

      <div>
        <label class="block text-sm font-medium mb-1">Email</label>
        <input name="email" type="email" required class="w-full px-3 py-2 border rounded focus:outline-none" placeholder="seu@exemplo.com" />
      </div>

      <div class="grid grid-cols-2 gap-4">
        <div>
          <label class="block text-sm font-medium mb-1">Senha</label>
          <input name="password" type="password" required minlength="6" class="w-full px-3 py-2 border rounded focus:outline-none" placeholder="Senha" />
        </div>
        <div>
          <label class="block text-sm font-medium mb-1">Confirmar senha</label>
          <input name="confirmPassword" type="password" required minlength="6" class="w-full px-3 py-2 border rounded focus:outline-none" placeholder="Repita a senha" />
        </div>
      </div>

      <button type="submit" class="w-full py-2 bg-sky-600 text-white rounded hover:bg-sky-700 transition">Criar conta</button>
    </form>

    <p class="text-center text-sm text-gray-600 mt-6">Já tem conta? <a th:href="@{/login}" class="text-sky-600 font-medium">Entrar</a></p>
  </div>
</body>
</html>
'@

Set-Content -Path $registerHtmlPath -Value $registerHtmlContent -Encoding UTF8
Write-Host "Wrote: $registerHtmlPath" -ForegroundColor Green

# --- Done writing files ---
Write-Host ""
Write-Host "Todos os arquivos foram atualizados. (backups *.bak gerados quando existentes)" -ForegroundColor Yellow

# --- Maven build + run (optional) ---
Write-Host ""
Write-Host "Rodando: mvn -DskipTests clean package" -ForegroundColor Cyan
& mvn -DskipTests clean package

if ($LASTEXITCODE -ne 0) {
    Write-Host "Build falhou. Verifique erros acima. Não vou tentar rodar o app." -ForegroundColor Red
    exit 1
}

Write-Host "Build OK. Agora executando a aplicação: mvn spring-boot:run" -ForegroundColor Cyan
# Run Spring Boot (will block the script; se quiser apenas iniciar sem bloquear, use Start-Process)
& mvn spring-boot:run
