package br.com.vinicius.oficina3d.config;

import br.com.vinicius.oficina3d.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            .authorizeHttpRequests(auth -> auth
                // Rotas públicas (sem autenticação necessária)
                .requestMatchers(
                    "/",                    // Página inicial
                    "/index",               // Página inicial alternativa
                    "/login",               // Página de login
                    "/register",            // Página de cadastro
                    "/css/**",              // Arquivos CSS
                    "/js/**",               // Arquivos JavaScript
                    "/img/**",              // Imagens
                    "/images/**",           // Imagens alternativa
                    "/webjars/**",          // Bibliotecas web (se usar)
                    "/error"                // Página de erro
                ).permitAll()
                // Todas as outras rotas precisam autenticação
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")                    // Página customizada de login
                .loginProcessingUrl("/login")           // URL que processa o login
                .defaultSuccessUrl("/home", true)       // Redireciona para /home após login
                .failureUrl("/login?error=true")        // Redireciona para login com erro
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")                   // URL de logout
                .logoutSuccessUrl("/login?logout")      // Redireciona após logout
                .invalidateHttpSession(true)            // Invalida a sessão
                .deleteCookies("JSESSIONID")            // Remove cookies
                .permitAll()
            )
            .userDetailsService(userDetailsService);
            
        return http.build();
    }
}