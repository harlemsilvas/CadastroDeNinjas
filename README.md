# CadastroDeNinjas

Uma API para cadastro de ninjas usando Spring Boot.

## Visão geral

Este projeto fornece endpoints REST para cadastrar, listar, consultar e remover ninjas (CRUD). Ideal para estudos de Java + Spring Boot e como base para evoluções (Swagger, validação, testes, deploy etc.).

## Tecnologias
- Java 21
- Spring Boot 4.0.1
- Spring Web MVC
- Spring Data JPA
- Lombok
- Maven (Spring Boot Maven Plugin)
- Banco: H2 (em desenvolvimento)

## Status atual
### Endpoints implementados
- GET /ninjas/boasvindas → retorna uma mensagem de boas-vindas (implementado em `NinjaController`).

### Modelagem (JPA)
- NinjaModel (tb_cadastro): id, nome, email, idade. Relaciona-se com Missoes via coluna `missoes_id`.
- MissoesModel (tb_missoes): id, nome, dificuldade.

## Estrutura de pacotes
- `dev.java10x.CadastroDeNinjas.Ninjas` — NinjaModel, NinjaController, NinjaService, NinjaRepository
- `dev.java10x.CadastroDeNinjas.Missoes` — MissoesModel, MissoesController, MissoesService, MissoesRepository

## Objetivo do projeto
Projeto de estudo e portfólio para praticar:
- arquitetura em camadas (Controller / Service / Repository)
- CRUD com JPA
- boas práticas de API REST
- testes de endpoints

## Funcionalidades (MVP)
- [ ] Cadastrar ninja
- [ ] Listar ninjas
- [ ] Buscar ninja por ID
- [ ] Atualizar ninja
- [ ] Remover ninja

## Como rodar o projeto

### Pré-requisitos
- Java 21 instalado (`java -version`)
- Git

### Rodando localmente
No Windows (PowerShell):

```powershell
git clone https://github.com/harlemsilvas/CadastroDeNinjas.git
cd CadastroDeNinjas
.mvnw.cmd spring-boot:run
```

No Linux / macOS:

```bash
./mvnw spring-boot:run
```

A API normalmente estará em: http://localhost:8080

## Testar a rota de boas-vindas
PowerShell:

```powershell
Invoke-RestMethod -Uri 'http://localhost:8080/ninjas/boasvindas' -Method GET
```

Ou com curl:

```bash
curl http://localhost:8080/ninjas/boasvindas
```

## Executar testes
PowerShell:

```powershell
.mvnw.cmd test
```

Linux / macOS:

```bash
./mvnw test
```

## Documentação
- `docs/API.md` (presente)
- `docs/ARCHITECTURE.md` (presente)
- `docs/RUNBOOK.md` (presente)

Consulte também:
- `CONTRIBUTING.md` — orientações para contribuir com o projeto.
- `LICENSE` — licença do projeto (MIT), arquivo na raiz do repositório.

## API - Endpoints principais
Base URL: http://localhost:8080

- GET /ninjas/boasvindas
- GET /ninjas
- POST /ninjas
- GET /ninjas/{id}
- PUT /ninjas/{id}
- DELETE /ninjas/{id}

Observação: o controller atual implementa PUT para atualização; PATCH não está implementado.

---

Atualizado: revisão de sintaxe, marcadores e links; rotas ajustadas conforme código fonte.
