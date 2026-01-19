# Runbook / Troubleshooting

## Erro de datasource ao iniciar
Como o projeto usa Spring Data JPA, é necessário configurar um banco (H2/PostgreSQL/etc.) no `application.properties`/`application.yml`.
Se não houver config, a aplicação pode falhar no start.

## Porta 8080 em uso
Defina outra porta:
`server.port=8081`

## Build / Package
./mvnw -U clean package

## Rodar testes
./mvnw test
