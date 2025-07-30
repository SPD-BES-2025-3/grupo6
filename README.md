# Sistema de Gestão de Biblioteca - Grupo 6

Repositório monolítico com front‑end desktop em Next.js + Electron e back‑end RESTful em Spring Boot.

---

## 🔍 Visão Geral

O **Sistema de Gestão de Biblioteca** permite:

* **Administradores**: cadastrar/editar/apagar usuários, livros, exemplares e gerenciar empréstimos (cadastro, devolução, renovação).
* **Usuários autenticados**: visualizar exemplares, criar e cancelar reservas.
* **Visitantes**: navegação pública pela listagem de livros e exemplares, sem necessidade de login, via API pública isolada.

A arquitetura é composta por três frentes:

1. **Front‑end Desktop** (Next.js + Electron)
2. **API RESTful** (Spring Boot + PostgreSQL)
3. **Integrador Público** (Redis + MongoDB)

---

## 🚀 Tecnologias

* **Front‑end:** Next.js, Electron, React, Material UI, react‑hook‑form, react‑icons, Zustand
* **Back‑end:** Spring Boot 3.5.3, Java 21, Spring Data JPA, Flyway, Spring Security, Lombok
* **Banco de Dados:** PostgreSQL, Redis, MongoDB
* **Autenticação:** JWT (armazenamento em LocalStorage)
* **Testes:** JUnit 5, Mockito

---

## ⚙️ Pré-requisitos

* **Node.js** v18+ e **npm**
* **Java** 21
* **Maven** 3.8+
* **Docker**

---

## 🏁 Como Rodar a Aplicação

### 1. Banco de Dados e Build com Docker


```bash
docker-compose build
docker-compose up -d
```

> O `docker-compose.yml` já define os serviços:
>
> * `postgres-db`
> * `redis-cache`
> * `mongodb-db`
> * `api-gateway`
> * `spring-orm-api`
> * `spring-odm-api`
> * `front-odm`

* Documentação Swagger: `http://localhost:8080/api/swagger-ui.html`
* Documentação Swagger Api Pública: `http://localhost:8080/public/swagger-ui.html`

### 4. Executar o Front‑end Desktop

1. Instalar dependências:

   ```bash
   cd front
   npm i --legacy-peer-deps
   ```

2. Rodar em modo de desenvolvimento:

   ```bash
   npm run dev
   ```
   * A aplicação web Next.js estará em `http://localhost:3000`.

3. Build para produção:
   O Docker já builda o front então só é necessário rodar o electron
   ```bash
   npm run electron
   ```
