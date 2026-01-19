# CadastroDeNinjas 🥷

Uma API para cadastro de ninjas usando Spring Boot. :contentReference[oaicite:1]{index=1}

## ✨ Visão geral

Este projeto fornece endpoints REST para cadastrar, listar, consultar e remover ninjas (CRUD).
Ideal para estudos de Java + Spring Boot e como base para evoluções (Swagger, validação, testes, deploy, etc.).

## 🧰 Tecnologias
- **Java 21**
- **Spring Boot 4.0.1**
- **Spring Web MVC**
- **Spring Data JPA**
- **Lombok**
- **Maven (Spring Boot Maven Plugin)**
- **Banco: H2 (em devenvolvimento)**

## ✅ Status atual
### Endpoints implementados
- `GET /boasvindas` → retorna uma mensagem de boas-vindas.

### Modelagem (JPA)
- **NinjaModel** (`tb_cadastro`)
    - `id`, `nome`, `email`, `idade`
    - relacionamento com missão via FK `missoes_id`
- **MissoesModel** (`tb_missoes`)
    - `id`, `nome`, `dificuldade`

## 🗂️ Estrutura de pacotes
- `dev.java10x.CadastroDeNinjas.Ninjas`
    - NinjaModel (entidade) e controllers/serviços de ninjas
- `dev.java10x.CadastroDeNinjas.Missoes`
    - MissoesModel (entidade) e controllers/serviços de missões

## ✅ Objetivo do projeto
Projeto de estudo e portfólio para praticar:
- arquitetura em camadas (Controller / Service / Repository)
- CRUD com JPA
- boas práticas de API REST
- testes de endpoints

## ✅ Funcionalidades (MVP)

- [ ] Cadastrar ninja
- [ ] Listar ninjas
- [ ] Buscar ninja por ID
- [ ] Atualizar ninja
- [ ] Remover ninja

## 🚀 Como rodar o projeto

### Pré-requisitos
- Java **21** instalado (`java -version`)
- Git

### Rodando localmente

```bash
# 1) Clone
git clone https://github.com/harlemsilvas/CadastroDeNinjas.git
cd CadastroDeNinjas

# 2) Rode (Maven Wrapper)
./mvnw spring-boot:run
# no Windows:
# mvnw.cmd spring-boot:run
```
A API normalmente sobe em:

- http://localhost:8080
- 
## 🔎 Testar a rota atual
```bash
  curl http://localhost:8080/boasvindas
```  

## 🧪 Como executar testes
```bash
./mvnw test
```
🗂️ Estrutura recomendada (camadas)

- controller → endpoints REST
- service → regras de negócio
- repository → acesso ao banco (JPA)
- model/entity → entidades persistidas

## 📚 Documentação

- API: veja docs/API.md
- Arquitetura: veja docs/ARCHITECTURE.md
- Runbook (erros comuns / troubleshooting): veja docs/RUNBOOK.md

---
🗺️ Roadmap 
📌 Próximos upgrades (sugestão)

- Adicionar banco em runtime (H2 ou PostgreSQL) + application.properties
- DTOs + validação (spring-boot-starter-validation)
- Swagger/OpenAPI (springdoc-openapi)
- Tratamento global de erros (@ControllerAdvice)
- Dockerfile + docker-compose
---
🤝 Contribuindo

Veja CONTRIBUTING.md.

📝 Licença

Este projeto está sob a licença MIT. Veja LICENSE.
---

## 3) docs/API.md (modelo pronto)

# API - CadastroDeNinjas

Base URL (local): `http://localhost:8080`

## Healthcheck
- `GET /actuator/health` (se o Actuator estiver habilitado)

## Endpoints (CRUD)

### Criar Ninja
- `POST /ninjas`
**Body (exemplo):**
```json
{
  "nome": "Naruto Uzumaki",
  "aldeia": "Konoha",
  "rank": "Genin"
}
```
Listar Ninjas
- GET /ninjas

Buscar Ninja por ID
- GET /ninjas/{id}

Atualizar Ninja
- PUT /ninjas/{id}

ou
- PATCH /ninjas/{id}

Remover Ninja
- DELETE /ninjas/{id}

Códigos de resposta (sugestão)
- 200 OK (consulta / update)
- 201 Created (criação)
- 204 No Content (delete)
- 400 Bad Request (payload inválido)
- 404 Not Found (id inexistente)
- 500 Internal Server Error (erro não tratado)

Erro padrão (sugestão).
```json
{
"timestamp": "2026-01-19T12:34:56",
"status": 400,
"error": "Bad Request",
"message": "campo 'nome' é obrigatório",
"path": "/ninjas"
}
```

---

# 4) Arquitetura

## Visão geral
Aplicação Spring Boot organizada em camadas:

- **Controller (Web/API)**: recebe requests, valida entrada e retorna responses.
- **Service (Regras de negócio)**: orquestra casos de uso.
- **Repository (Persistência)**: acesso a dados (JPA).
- **Model/Entity**: entidades do domínio.
- **DTOs** (opcional): entrada/saída para não expor entidades diretamente.

## Padrões e decisões
- Separação por camadas para manter responsabilidades claras.
- DTOs recomendados para evitar acoplamento e facilitar evolução.
- Tratamento de exceções centralizado via `@ControllerAdvice` (recomendado).
---
# Runbook / Troubleshooting

## Port 8080 já está em uso
- Troque a porta no `application.properties`:
  `server.port=8081`

## Erros de build
```bash
./mvnw -U clean package
```

## Dicas de log

Aumentar nível de log:
- logging.level.org.springframework=INFO
- logging.level.seu.pacote=DEBUG


---

## 6) Contribuindo

## Padrões
- Use commits curtos e objetivos (ex: `feat: criar endpoint de cadastro`)
- Abra PR com descrição do que mudou

## Rodar local
1. `./mvnw spring-boot:run`
2. `./mvnw test`

## Checklist do PR
- [ ] Build passando
- [ ] Testes passando
- [ ] Endpoints documentados em `docs/API.md`

## 7) LICENSE (MIT)
- MIT License 
- Copyright (c) 2026 Harlem Silva
- Permission is hereby granted, free of charge, to any person obtaining a copy...


