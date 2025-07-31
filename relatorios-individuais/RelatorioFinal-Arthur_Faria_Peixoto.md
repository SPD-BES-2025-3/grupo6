# Sistema de Gestão de Biblioteca – Projeto Final
Arthur Faria Peixoto \- 201910873

**Integrantes:** Álvaro Veloso Lisboa, Arthur Faria Peixoto, Thâmara Cordeiro de Castro

---

## Resumo

O presente relatório consolida o desenvolvimento do **Sistema de Gestão de Biblioteca** concebido pelo Grupo 6. Nosso objetivo foi criar uma solução desktop híbrida (Next.js + Electron) integrada a uma API RESTful robusta (Spring Boot + PostgreSQL), com vistas a oferecer funcionalidades completas de cadastro, empréstimo, devolução, reserva e visualização pública de acervo.

Nessa entrega, além de concluir todas as telas com seus fluxos específicos, implementei a autenticação completa usando JWT, garantindo que cada requisição ao back-end só fosse permitida mediante token válido. Para persistir o token e manter o usuário logado entre sessões e para manter os dados do usuário salvo entre sessões, utilizei o LocalStorage, e para o uso rápido dos dados de usuário foi construido um store via Zustand, um state-manager leve e reativo. Nos formulários, adotei react-hook-form e yup para validações dinâmicas e de alto desempenho. Toda a interface foi construída sobre Material UI com um esquema de theming personalizado, garantindo consistência visual e permitindo reaproveitar componentes estilizados. Para os ícones vetoriais, recorri a react-icons, o que deixou a aplicação mais leve e de fácil manutenção.

Além disso, criei as validações client-side, exibindo modais de erro, confirmação e sucesso claras e globais através de um componente de alertas integrado ao tema, e fiz testes manuais de todas as regras de permissão (administrador, usuário e visitante) assegurando que cada ação só estivesse disponível ao perfil correto.

Como pode ser visto no commit `195b8a3 - feat: emprestimo`, desenvolvi toda a lógica relacionada ao módulo de empréstimos no back‑end. Isso incluiu:

- Criação de DTOs específicos para as operações de cadastro, renovação e devolução, com validações de integridade e segurança.
- Implementação completa do controller REST, respeitando boas práticas RESTful e segregando responsabilidades com o uso de services e repositórios.
- Validação das regras de negócio, como impedimento de empréstimos duplicados, renovação apenas com exemplar em posse do usuário e checagem de pendências.
- Criação de testes unitários com JUnit e Mockito cobrindo diversos cenários como renovação com prazo expirado, devolução de exemplar não emprestado e tentativas de duplicidade assegurando robustez e previsibilidade das respostas da API.

Essas funcionalidades foram projetadas respeitando princípios de coesão, injeção de dependência, controle transacional e resposta adequada de status HTTP em cada operação. A API final foi documentada no Swagger para facilitar testes e integração com o front‑end.

**Principais vantagens:**

- **Usabilidade Desktop:** interface moderna, responsiva e padronizada via Material UI;
- **Escalabilidade Backend:** arquitetura limpa em camadas, versionamento de banco com Flyway e segurança via Spring Security;
- **Integração de Dados Públicas:** sincronização de PostgreSQL para MongoDB via Redis, garantindo performance e isolamento de dados sensíveis;
- **Qualidade de Software:** cobertura de testes unitários de 100% para casos de uso críticos e automação de CI/CD.

---

## 1. Introdução

- **Justificativa e Motivação:**
  A crescente necessidade de bibliotecas acadêmicas por sistemas que unam facilidade de uso a segurança e confiabilidade motivou este projeto. Nosso foco foi proporcionar aos administradores total controle do acervo e aos visitantes acesso público rápido a informações não-sensíveis.

- **Descrição do Problema:**
  Sistemas legados muitas vezes exigem navegadores e carecem de integrações em tempo real entre repositórios de dados. Buscamos resolver esse gap com uma aplicação desktop leve e uma API eficiente.

---

## 2. Metodologia e Planejamento

| Item                          | Detalhes                                                                                                                                                                                                                                                                                                                        |
| ----------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Objetivo Geral**            | Desenvolver um sistema desktop integrado a uma API RESTful para gestão de biblioteca, englobando cadastro, empréstimo, reserva e visualização pública.                                                                                                                                                                          |
| **Objetivos Específicos**     | 1. Criar interface desktop (Next.js + Electron) com navegação por perfil;<br>2. Implementar API RESTful (Spring Boot + PostgreSQL) com segurança JWT;<br>3. Sincronizar dados públicos via Redis/MongoDB;<br>4. Garantir qualidade com testes e documentação completa.                                                          |
| **Tecnologias e Ferramentas** | - **Front‑end:** Next.js, Electron, react‑hook‑form, Material UI, react‑icons, Zustand, LocalStorage, JWT;<br>- **Back‑end:** Spring Boot 3.5.3, Java 21, Spring Data JPA, Flyway, Spring Security, Lombok;<br>- **Banco:** PostgreSQL, Redis, MongoDB;<br>- **Documentação:** Markdown, UML (classes, componentes, sequência). |

### Divisão de Tarefas, Prazos e Cronograma

- **Planejamento & Kick‑off:** Reunião inicial (todos)
- **Documentação:** Thâmara
- **Modelagem UML:** Álvaro & Thâmara
- **Persistência & API:** Álvaro & Arthur
- **Interface & Autenticação:** Arthur
- **Integração Redis/MongoDB:** Álvaro & Arthur
- **Testes Unitários:** Álvaro
- **Ajustes Finais:** Todos

**Critérios de Avaliação Aplicados:**

- **Volume total:** 76 commits totais
- **Volume individual:** 39 commits de interface + 1 commit principal de back-end; 21.186 linhas adicionadas, 7.849 removidas
- **Profundidade Técnica:** adoção de Clean Architecture, theming, integrações Redis/MongoDB
- **Precisão:** 100% de cobertura de testes JUnit/Mockito em casos de uso de usuário
- **Criatividade:** uso de Electron para desktop híbrido e arquitetura de sincronização pública

---

## 3. Arquitetura Geral

**Componentes Principais:**

1. **Aplicação Desktop:** Next.js + Electron (renderização híbrida, theming consistente)
2. **API RESTful:** Spring Boot, endpoints CRUD para Livro, Exemplar, Usuário, Empréstimo, Reserva
3. **Integrador Público:** serviço que extrai dados de PostgreSQL e replica no MongoDB via Redis, servindo uma API pública

---

## 4. Desenvolvimento

### 4.1 Front‑end Desktop (Arthur F. Peixoto)

- **Perfis de Usuário:**

  - **Administrador:** CRUD completo em Usuário, Livro, Exemplar, Empréstimo (cadastro, devolução, renovação)
  - **Usuário:** visualiza exemplares, faz e cancela reservas
  - **Visitante:** acesso público a listagem de livros/exemplares via modal, sem autenticação

- **Bibliotecas e Padrões:** react‑hook‑form, Material UI com tema personalizado, react‑icons, Zustand (estado) e LocalStorage (persistência de token JWT).

### 4.2 Back‑end e Persistência (Álvaro V. Lisboa & Arthur)

- **Entidades JPA:** Livro, Exemplar, Usuário, Empréstimo, Reserva
- **Repositorios & Use Cases:** camadas separadas, Clean Architecture
- **Segurança:** JWT, Spring Security
- **Migrações de Banco:** Flyway

### 4.3 Public API & Integração (Álvaro & Arthur)

- **Fluxo:** Spring Boot publica em Redis → Worker replica em MongoDB → API pública lê MongoDB
- **Isolamento de Dados:** somente informações não-sensíveis são sincronizadas

### 4.4 Testes e Qualidade (Álvaro)

- **JUnit 5 + Mockito:** 100% de cobertura
- **Negócio:** validações de duplicidade, criptografia de senha, tratamento centralizado de exceções

---

## 5. Infraestrutura e Deploy

- **Ambiente de Desenvolvimento:** PostgreSQL local (porta 5432), Spring DevTools (hot reload), profiles via `docker-compose.yml`
- **Build e Empacotamento:** `docker-compose build` + `cd front` + `npm i --legacy-peer-deps` + `npm run electron` para buildar tudo e rodar a aplicação desktop a partir dos builds

---

## 6. Resultados e Lições Aprendidas

- **Pontos Fortes:**

  - Integração híbrida desktop/web
  - Arquitetura modular e limpa
  - Sincronização segura de dados públicos
  - Testes automatizados de alta cobertura

- **Desafios:**

  - Definição inicial dos relacionamentos JPA
  - Configuração do pipeline Redis → MongoDB
  - Theming consistente entre Material UI e Electron
  - Build via Docker (era a primeira vez utilizando docker)

- **Aprendizados:**

  - Importância de dividir claramente responsabilidades
  - Ferramentas de migrations (Flyway) agilizam onboarding
  - Testes unitários prévios facilitam refatorações futuras

---

_Relatório preparado por Arthur Faria Peixoto - Grupo 6, conforme critérios de volume, profundidade técnica, precisão e criatividade._
