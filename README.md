# Springboot-CRUD-2

Este projeto é uma aplicação web de gerenciamento de oficinas e máquinas, construída com Spring Boot, Thymeleaf, Tailwind CSS/DaisyUI, PostgreSQL e autenticação via Spring Security.

## Funcionalidades
- Cadastro, edição e exclusão de oficinas
- Cadastro, edição e exclusão de máquinas
- Cada usuário só visualiza e gerencia suas próprias oficinas e máquinas
- Autenticação e associação de dados ao usuário logado
- Dashboard com métricas personalizadas
- Layout responsivo e moderno com DaisyUI

## Como rodar
1. Configure o banco de dados em `src/main/resources/application.properties`.
2. Execute os scripts SQL em `src/main/resources/db/migration` para criar as tabelas.
3. Instale as dependências do frontend com `npm install` (se necessário).
4. Execute o projeto com `mvnw spring-boot:run` ou `mvn spring-boot:run`.

## Estrutura do Projeto
- `src/main/java` - código Java (controllers, services, models)
- `src/main/resources/templates` - templates Thymeleaf
- `src/main/resources/static/css` - CSS Tailwind
- `src/main/resources/db/migration` - scripts de migração do banco

## Autor
Vinicius

---

Para dúvidas ou sugestões, abra uma issue no repositório.
