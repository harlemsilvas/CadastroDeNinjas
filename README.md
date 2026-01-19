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
- PATCH /ninjas/{id} → atualizações parciais (nome, email, idade, missoes). Veja seção "PATCH /ninjas/{id}" para exemplos.

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
Você pode rodar a suíte de testes localmente com o wrapper Maven.

PowerShell:

```powershell
# rodar todos os testes
.mvnw.cmd test

# rodar apenas testes de unidade
.mvnw.cmd -Dtest=NinjaServiceTest test

# rodar apenas testes de integração do controller que iniciam a aplicação
.mvnw.cmd -Dtest=NinjaControllerIntegrationTest test
```

Linux / macOS:

```bash
./mvnw test
```

Observações:
- Os testes de integração usam H2 em memória e iniciam o servidor em uma porta aleatória (RANDOM_PORT). Os testes criam/limpam dados automaticamente.
- Se quiser acelerar um build local para desenvolvimento iterativo, pode usar `-DskipTests` no pacote: `./mvnw -DskipTests package`.

## Inicialização do banco (data.sql)
O projeto inclui um script de inicialização que é executado automaticamente ao arrancar a aplicação quando a propriedade padrão de inicialização de dados do Spring Boot está habilitada.

- Arquivo: `src/main/resources/data.sql`
- Comportamento: o script usa comandos H2 (`MERGE INTO` e `ALTER TABLE ... RESTART WITH`) para ser idempotente e evitar falhas de inserção duplicada em ambientes de teste. Ele insere/atualiza três missões iniciais e 10 ninjas de exemplo.
- O script atual (resumo):
  - Insere/atualiza 3 missões (ids 1..3).
  - Insere/atualiza 10 ninjas famosos (Naruto, Sasuke, Sakura, etc.) apontando para as missões ciclicamente.
  - Ajusta as sequências/identities para evitar colisões (próximo id após os itens definidos).

Se quiser alterar os dados iniciais, edite `src/main/resources/data.sql` e reinicie a aplicação. Em ambiente de produção, remova ou substitua este script conforme apropriado.

## API - Endpoints principais
Base URL: http://localhost:8080

- GET /ninjas/boasvindas
- GET /ninjas
- POST /ninjas
- GET /ninjas/{id}
- PUT /ninjas/{id}
- PATCH /ninjas/{id}  ← atualizações parciais
- DELETE /ninjas/{id}

PATCH /ninjas/{id}
- Aceita um JSON parcial com os campos que você deseja atualizar: `nome`, `email`, `idade`, `missoes`.
- Formatos aceitos para `missoes` (por causa do modelo ManyToOne no `Ninja`):
  - um id numérico: `{ "missoes": 2 }`
  - um objeto com id: `{ "missoes": { "id": 2 } }`
- Comportamento:
  - Campos enviados são validados e aplicados; campos omitidos são mantidos.
  - Campos desconhecidos causam 400 Bad Request (modo estrito).
  - Se a missão informada não existir, retorna 400 com mensagem de erro explicando o id inválido.

Exemplos:
- Atualizar nome e idade:

```json
{ "nome": "Kakashi Hatake", "idade": 35 }
```

- Atualizar missão por id:

```json
{ "missoes": 2 }
```

## Documentação
- `docs/API.md` (presente)
- `docs/ARCHITECTURE.md` (presente)
- `docs/RUNBOOK.md` (presente)

Consulte também:
- `CONTRIBUTING.md` — orientações para contribuir com o projeto.
- `LICENSE` — licença do projeto (MIT), arquivo na raiz do repositório.

---

Atualizado: inclusão de documentação de testes locais, inicialização do banco e operação PATCH.
