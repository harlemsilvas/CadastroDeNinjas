# Arquitetura - CadastroDeNinjas

## Stack
- Spring Boot 4 (Web MVC)
- Spring Data JPA
- Lombok

## Entidades e tabelas

### NinjaModel → `tb_cadastro`
Campos:
- `id` (PK, auto incremento)
- `nome` (String)
- `email` (String)
- `idade` (int)

Relacionamento:
- `@ManyToOne` com `MissoesModel`
- Coluna FK: `missoes_id`

Interpretação:
- Vários ninjas podem estar associados à mesma missão.

### MissoesModel → `tb_missoes`
Campos:
- `id` (PK, auto incremento)
- `nome` (String)
- `dificuldade` (String)

Relacionamento (estado atual do código):
- existe um atributo `ninja` mapeado com `@OneToOne(mappedBy = "missoes")`

⚠️ Atenção:
Esse `@OneToOne` não representa “uma missão possui vários ninjas”.
Se a regra for “uma missão pode ter vários ninjas”, o mapeamento recomendado é:

- Em `MissoesModel`:
    - `@OneToMany(mappedBy = "missoes") private List<NinjaModel> ninjas;`

- Em `NinjaModel` (já está ok):
    - `@ManyToOne @JoinColumn(name="missoes_id") private MissoesModel missoes;`

## Camadas recomendadas (para evoluir o projeto)
- Controller → endpoints REST
- Service → regras de negócio
- Repository → persistência (JPA)
- DTOs → entrada/saída (evitar expor entity diretamente)