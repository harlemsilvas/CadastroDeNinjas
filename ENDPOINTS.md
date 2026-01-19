# Endpoints - CadastroDeNinjas

Este é um resumo prático dos endpoints da API e exemplos rápidos de uso (curl, HTTPie e instruções para Postman).

Base URL (local): http://localhost:8080

Observação: a aplicação usa H2 em memória por padrão em dev/test; o script `src/main/resources/data.sql` popula dados iniciais (3 missões e 10 ninjas).

---

## Endpoints principais

- GET /ninjas/boasvindas
- GET /ninjas
- POST /ninjas
- GET /ninjas/{id}
- PUT /ninjas/{id}
- PATCH /ninjas/{id}  (atualizações parciais)
- DELETE /ninjas/{id}

---

## Cabeçalhos comuns

- Content-Type: application/json

---

## Exemplos (curl)

1) Listar todos os ninjas

```bash
curl -sS http://localhost:8080/ninjas
```

2) Buscar um ninja por id

```bash
curl -sS http://localhost:8080/ninjas/1
```

3) Criar um ninja (POST)

```bash
curl -sS -X POST http://localhost:8080/ninjas \
  -H "Content-Type: application/json" \
  -d '{"nome":"Nuevo Ninja","email":"ninja@example.test","idade":20}'
```

4) Substituir um ninja inteiro (PUT)

```bash
curl -sS -X PUT http://localhost:8080/ninjas/1 \
  -H "Content-Type: application/json" \
  -d '{"id":1,"nome":"Nome Completo","email":"novo@example.test","idade":30}'
```

5) Atualização parcial (PATCH) — atualizar nome e idade

```bash
curl -sS -X PATCH http://localhost:8080/ninjas/1 \
  -H "Content-Type: application/json" \
  -d '{"nome":"Kakashi Hatake","idade":35}'
```

6) PATCH atualizar missão por id (aceita id numérico)

```bash
curl -sS -X PATCH http://localhost:8080/ninjas/1 \
  -H "Content-Type: application/json" \
  -d '{"missoes": 2}'
```

7) PATCH atualizar missão via objeto com id

```bash
curl -sS -X PATCH http://localhost:8080/ninjas/1 \
  -H "Content-Type: application/json" \
  -d '{"missoes": {"id": 2}}'
```

8) Deletar

```bash
curl -sS -X DELETE http://localhost:8080/ninjas/1
```

---

## Exemplos (HTTPie)

HTTPie tem uma sintaxe mais legível:

- Listar:

```bash
http GET :8080/ninjas
```

- PATCH (nome + idade):

```bash
http PATCH :8080/ninjas/1 nome='Kakashi Hatake' idade:=35
```

- PATCH missão por objeto:

```bash
http PATCH :8080/ninjas/1 missoes:='{"id":2}'
```

---

## Instruções rápidas para Postman

1. Abra o Postman e crie uma nova Collection (ex.: `CadastroDeNinjas`).
2. Crie requests com os métodos apropriados e a URL: `http://localhost:8080/ninjas` ou `http://localhost:8080/ninjas/{id}`.
3. Em `Body` selecione `raw` → `JSON` e cole o JSON de exemplo (ver seções curl acima).
4. Adicione header `Content-Type: application/json` quando enviar bodies JSON.

Dica: você pode exportar os requests que usar com os exemplos acima e versionar como coleções do Postman.

---

## Observações sobre o PATCH

- Campos suportados: `nome` (string), `email` (string), `idade` (int >= 0), `missoes` (id numérico ou objeto com `{ "id": <num> }`).
- Modo estrito: campos desconhecidos retornam 400 para evitar erros silenciosos.
- Se a missão informada não existir, o endpoint retorna 400 com uma mensagem indicando qual id não foi encontrado.

---
 
