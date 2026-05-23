# Arquitetura

## Modelo geral

O NEXUS HUB segue MVC com separacao explicita entre `model`, `view` e `controller`.

```text
NEXUSHUB
├── model
├── controller
├── view
└── docs
```

## Model

Modulo Java responsavel pelo dominio da aplicacao.

Contem:

- Entidades JPA.
- DTOs.
- Repositorios.
- Interfaces e implementacoes de servico.
- Regras de negocio.

## Controller

Modulo Spring Boot responsavel pela API.

Contem:

- Classe principal da aplicacao.
- Controllers REST.
- Configuracoes.
- Seeds de desenvolvimento.

O controller depende do modulo `model`.

## View

Aplicacao Next.js responsavel pela interface.

Contem:

- Rotas e paginas em `app`.
- Estilos globais.
- Camada simples de acesso a API em `lib`.

## Comunicacao

A view consome a API REST exposta pelo controller.

Fluxo esperado:

```text
Usuario -> View Next.js -> Controller Spring REST -> Model -> Banco de dados
```

## Banco inicial

O projeto usa H2 em memoria na fase inicial para facilitar desenvolvimento local. Em ambiente real, a recomendacao e migrar para PostgreSQL.
