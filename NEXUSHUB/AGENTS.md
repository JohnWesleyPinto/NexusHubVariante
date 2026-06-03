# AGENTS

Este arquivo orienta agentes e assistentes que forem trabalhar no projeto NEXUS HUB.

## Objetivo do projeto

O NEXUS HUB e uma plataforma academica gamificada para centralizar projetos, grupos, eventos e oportunidades universitarias. A proposta e aproximar estudantes, professores e iniciativas academicas em um ambiente unico, com pontos, conquistas, rankings e perfis academicos.

## Arquitetura

O projeto segue MVC de forma explicita:

- `model`: camada de dominio, entidades, DTOs, repositorios e servicos.
- `controller`: aplicacao Spring Boot, controllers REST e configuracoes.
- `view`: frontend em Next.js.
- `docs`: documentacao tecnica, produto e arquitetura.

## Regras de trabalho

- Preservar a separacao MVC.
- Nao mover arquivos entre `model`, `controller` e `view` sem motivo claro.
- Backend deve continuar em Spring Boot.
- Frontend deve continuar em Next.js.
- Usar nomes em portugues para entidades de negocio quando fizer sentido.
- Evitar adicionar dependencias sem necessidade.
- Criar documentacao em `docs` quando uma decisao tecnica ou funcional importante for tomada.
- Antes de apagar arquivos, confirmar que eles nao fazem parte do escopo atual.

## Padroes iniciais

No backend:

- Entidades ficam em `model/src/main/java/.../model/entity`.
- DTOs ficam em `model/src/main/java/.../model/dto`.
- Repositorios ficam em `model/src/main/java/.../model/repository`.
- Servicos ficam em `model/src/main/java/.../model/service`.
- Controllers REST ficam em `controller/src/main/java/.../controller`.

No frontend:

- Telas ficam em `view/app`.
- Chamadas para API ficam em `view/lib`.
- Estilos globais ficam em `view/app/globals.css`.

## Proximas prioridades

- Autenticacao de usuarios.
- Perfil academico.
- Catalogo de projetos.
- Grupos academicos.
- Mural de oportunidades.
- Sistema de pontos.
- Conquistas e rankings.
- Eventos academicos.
