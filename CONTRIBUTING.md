# CONTRIBUTING

Obrigado por contribuir com o CadastroDeNinjas! Abaixo estão as orientações rápidas para abrir issues, preparar pull requests e garantir que as contribuições sejam aceitas com mais rapidez.

## Como abrir uma issue
- Verifique primeiro se já existe uma issue similar no repositório.
- Use um título claro e descritivo.
- Forneça passos para reproduzir (se for um bug), o comportamento esperado e o comportamento observado.
- Inclua informações de ambiente quando relevante (Java version, SO, comandos executados).

## Como contribuir (Pull Request)
1. Faça fork do repositório e clone localmente.
2. Crie uma branch com nome descritivo: `feat/<descricao>`, `fix/<descricao>`, `chore/<descricao>`.
3. Escreva código claro e com pequenos commits atômicos.
4. Execute os testes localmente antes de enviar o PR:

```powershell
# Na raiz do projeto
.\mvnw.cmd test
```

5. Atualize a documentação (`docs/API.md`, `README.md` ou `docs/ARCHITECTURE.md`) quando houver mudanças visíveis na API ou no comportamento.
6. Abra o Pull Request contra a branch `main` do repositório original e descreva o que foi alterado e por quê.

## Padrões de commits
- Utilize mensagens curtas e no estilo "Conventional Commits":
  - `feat: descrever nova funcionalidade`
  - `fix: corrigir bug X`
  - `docs: atualizar documentação`
  - `chore: tarefas de manutenção`

## Checklist de PR (o que avaliar antes de submeter)
- [ ] Código compilando sem erros
- [ ] Testes automatizados relevantes adicionados/atualizados
- [ ] Documentação atualizada quando aplicável
- [ ] Mudanças são pequenas e focadas (um objetivo por PR)

## Estilo de código
- Siga as convenções Java do projeto (formatação, nomenclatura). Se o projeto usa um formatador (ex: Google Java Format), aplique-o antes de commitar.
- Prefira código legível, com métodos pequenos e responsabilidades claras.

## Testes
- Inclua testes automatizados para bugs e novas funcionalidades quando possível.
- Execute `./mvnw test` (ou `.\mvnw.cmd test` no Windows) localmente e assegure que tudo passe.

## Revisão de código
- PRs serão revisados por mantenedores; responda a comentários e ajuste o PR conforme solicitado.
- Mantenha PRs pequenos — facilita revisão e acelera merge.

## Comunicação
- Para discussões maiores, abra uma issue primeiro para alinhar a solução.

## Licença
- Ao contribuir, você concorda que suas alterações serão licenciadas sob a licença do projeto (MIT), conforme indicado no repositório.

---

Agradecemos sua contribuição! Se quiser, posso criar templates de ISSUE/PR e um CODE_OF_CONDUCT básico — diga se deseja isso.
