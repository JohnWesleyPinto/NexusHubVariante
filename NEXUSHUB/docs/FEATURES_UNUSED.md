# Recursos Não Utilizados da Branch AgoraVai

Durante o processo de unificação do frontend e backend da branch `AgoraVai` com a branch `main`, identificamos alguns diretórios, arquivos e pacotes que não foram incorporados à versão final na `main`. Este documento explica cada um deles e por que não estão em uso.

---

## 1. Diretório `NEXUSHUB/view-next` (Front-End Next.js)

* **O que é**: Um protótipo de frontend desenvolvido em Next.js (React). Ele contém apenas uma página inicial básica que lista projetos em destaque integrando com a API.
* **Motivo de não ser utilizado**: O ecossistema completo de produção do NexusHub foi construído utilizando Angular 21 (no diretório `NEXUSHUB/view`). O Angular implementa todas as páginas do portal, incluindo autenticação (login, cadastro, recuperação de senha), painéis administrativos, controle de acessos (LGPD), modais de criação de projetos, curtida de projetos/grupos e área de loja (Sandbox e Stripe). Manter o protótipo `view-next` ativo traria complexidade de manutenção desnecessária, já que ele não cobre a totalidade do sistema.

---

## 2. Diretório `NEXUSHUB/view-angular` (Estrutura Angular antiga)

* **O que é**: O diretório de desenvolvimento original do frontend Angular na branch `AgoraVai`. Ele continha componentes de página com estruturas planas (ex: `src/app/pages/cadastro.page.ts`, `cadastro.page.html`).
* **Motivo de não ser utilizado**: Durante o avanço da branch `main`, foi realizada uma grande refatoração estrutural (mesclada da branch `B_REFACT_ESTRUTURA`). Todo o código do frontend foi migrado para o diretório `NEXUSHUB/view` e organizado em uma arquitetura modular por features (ex: `features/auth`, `features/projects`, `features/groups`, `features/store`).
* **O que foi feito**: Em vez de utilizar a pasta `view-angular`, as funcionalidades exclusivas criadas nela (como upload de fotos de perfil, validações extras e favoritação/curtidas de grupos) foram extraídas, adaptadas e integradas diretamente nos módulos do diretório `NEXUSHUB/view`. Portanto, a pasta `view-angular` tornou-se obsoleta e duplicada.

---

## 3. Pacotes Obsoletos de Backend (model/entity, model/repository, model/service)

* **O que são**: Pastas de código Java que surgiram na branch `AgoraVai` (ex: `br.ufpb.dsc.nexushub.model.entity.*`, `br.ufpb.dsc.nexushub.model.repository.*`).
* **Motivo de não ser utilizado**: A branch `main` adotou um modelo de pacotes altamente modular baseado em domínios de negócio no submódulo `model` (como `model.projects`, `model.groups`, `model.identity`, `model.opportunities`). 
* **O que foi feito**: As entidades e serviços da `AgoraVai` foram portados para o novo modelo de classes em seus respectivos domínios (por exemplo, a antiga classe `Projeto.java` foi reescrita e adaptada como a entidade `Project.java` dentro do domínio de projetos). As tabelas e colunas foram compatibilizadas via migrações de banco e scripts, tornando as classes sob o pacote `br.ufpb.dsc.nexushub.model.entity` obsoletas e redundantes.
