# Riverfy Backend

> Sistema de gestão e engajamento para a comunidade cristã Rio da Vida.

Este é a API REST do Projeto Rio da Vida, desenvolvido como parte da disciplina de Análise e Projeto de Sistemas (APS). O projeto visa digitalizar processos como cadastro de membros, avisos comunitários e planos de leitura bíblica.

---

## 🚀 Tecnologias e Ferramentas

O projeto utiliza uma stack moderna e robusta, focada em escalabilidade e segurança:

*   **Linguagem:** Java 21
*   **Framework:** Spring Boot 3+
*   **Segurança:** Spring Security & Auth0 (OAuth2/JWT)
*   **Banco de Dados:** PostgreSQL
*   **Migrações:** Flyway (Versionamento de banco de dados)
*   **Documentação:** Swagger UI (OpenAPI 3)
*   **Containerização:** Docker & Docker Compose
*   **Persistência:** Spring Data JPA / Hibernate

---

## 🏗️ Arquitetura e Boas Práticas

O desenvolvimento é guiado por princípios de engenharia de software para garantir um código limpo e de fácil manutenção:

*   **Padrões:** SOLID, DRY e Clean Code.
*   **Arquitetura:** Camadas bem definidas (Controller, Service, Repository, DTO).
*   **Versionamento de Código:** Estratégia de Branchs e Code Review.
*   **Processo:** Metodologia Ágil (Sprints).

---

## ⚙️ Como Executar o Projeto

### Pré-requisitos
*   Java 21 instalado.
*   Docker e Docker Compose instalados.
*   Maven 3.9+.

### Passo a Passo

1. **Clonar o repositório:**
   ```bash
   git clone https://github.com/Projeto-Rio-da-Vida/riverfy-backend.git
   cd riverfy-backend
3. **Configurar Variáveis de Ambiente:**
   Crie um arquivo `.env` na raiz ou configure no seu sistema as credenciais do banco e do Auth0 (conforme o `application.yml`).

4. **Subir o Banco de Dados (Docker):**
  ```bash
  docker-compose up -d
```
4. **Executar a Aplicação:**
  ```bash
  ./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

A documentação Swagger poderá ser acessada em `http://localhost:8080/swagger-ui.html`.

## 👥 Equipe

*   Victor Vale - Backend Developer
*   Manoel Dionisio - Backend Developer
*   Alyne Matos - Frontend Developer
*   Leticya Oliveira - Requirements & UI/UX Designer (Figma)

## 📄 Licença
Este projeto é para fins acadêmicos e uso comunitário da Igreja Rio da Vida.
