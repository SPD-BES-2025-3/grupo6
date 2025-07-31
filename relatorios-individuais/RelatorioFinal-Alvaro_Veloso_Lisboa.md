## **Relatório Final - Etapa 2**   
Álvaro Veloso Lisboa - 202306332

### **Resumo das Implementações**

Este relatório documenta o progresso completo do desenvolvimento desde a primeira etapa até a conclusão do projeto final. Após estabelecer a base sólida da camada de persistência e API RESTful com Spring Boot e PostgreSQL na etapa 1, expandiu-se significativamente a arquitetura para incluir múltiplas APIs, sincronização de dados, autenticação robusta e containerização completa.

### **Evolução da Etapa 1 para Etapa 2**

**Continuação do Projeto ORM (PostgreSQL + Spring Boot)**

Na etapa 1, havia sido implementada a estrutura básica do projeto ORM com as 5 entidades principais (Livro, Exemplar, Usuario, Emprestimo, Reserva). Na etapa 2, completei a implementação com os seguintes avanços:

#### **1. Conclusão dos Use Cases**
- Implementação completa de todos os casos de uso restantes para gerenciamento do sistema de biblioteca
- Operações CRUD completas para todas as entidades
- Lógica de negócio para controle de empréstimos, renovações e reservas
- Validações de regras de negócio (disponibilidade, prazos, permissões)

#### **2. API REST Completa**
- Endpoints RESTful para todas as operações do sistema
- Padronização de respostas HTTP com códigos de status apropriados
- Tratamento centralizado de exceções

#### **3. Autenticação com Spring Security e JWT**
- Implementação de autenticação baseada em tokens JWT
- Sistema de autorização baseado em roles/permissões
- Endpoints protegidos conforme perfil do usuário (EmprestimoController, somente usuário administrador ou bibliotecário pode acessar)
- Endpoints acessíveis somente para usuários autenticados no projeto ORM, e endpoints públicos para o projeto ODM
- Configuração de segurança robusta com criptografia de senhas

#### **4. Documentação com Swagger**
- Integração completa com OpenAPI 3.0
- Documentação de todos os endpoints através das anotações Swagger
- Esquemas de request/response documentados
- Interface interativa para testes da API

#### **5. Configuração de CORS**
- Políticas de CORS configuradas para permitir acesso do frontend
- Configuração específica para desenvolvimento e produção
- Headers de segurança apropriados

### **Implementação do Projeto ODM (MongoDB + Spring Boot)**

Desenvolveu-se uma segunda API completamente independente utilizando MongoDB como banco de dados NoSQL:

#### **Arquitetura ODM**
- **Spring Boot** com Spring Data MongoDB
- **MongoDB** como banco de dados principal
- **API REST** pública com endpoints de consulta dos dados sincronizados com a API ORM
- **Swagger** para documentação completa da API
- **Estrutura de dados** otimizada para NoSQL, mantendo compatibilidade conceitual com o ORM

#### **Funcionalidades Implementadas**
- Queries otimizadas para MongoDB
- Validações específicas para ambiente NoSQL
- API pública totalmente documentada

### **Implementação do API Gateway**

Criou-se um gateway centralizado para gerenciar o fluxo entre clientes e as APIs:

#### **Responsabilidades do Gateway**
- Roteamento inteligente entre APIs ORM e ODM
- Ponto único de entrada para o sistema
- Abstração da complexidade de múltiplas APIs para o cliente

#### **Tecnologias Utilizadas**
- Spring Cloud Gateway
- Configuração declarativa de rotas

### **Sincronização de Dados com Redis Pub/Sub**

Implementou-se um sistema robusto de sincronização entre as APIs ORM e ODM:

#### **Arquitetura de Sincronização**
- **DataSyncPublisher** no projeto ORM: publica eventos de mudança de dados
- **DataSyncSubscriber** no projeto ODM: escuta e processa eventos de sincronização
- **Redis** como message broker para comunicação assíncrona
- **Canal Pub/Sub** dedicado para eventos de sincronização

#### **Mecanismo de Fallback**
- **Fila de operações** persistida no Redis para garantir consistência
- **Sistema de retry** automático para operações que falharam
- **Bloqueio sequencial**: operações subsequentes aguardam resolução de falhas
- **Garantia de consistência** eventual entre as duas bases de dados

#### **Benefícios da Implementação**
- Tolerância a falhas na sincronização
- Manutenção da ordem das operações
- Recuperação automática de operações perdidas
- Monitoramento do status de sincronização

### **Containerização com Docker**

Finalizou-se o projeto com uma solução completa de containerização:

#### **Docker Compose Completo**
- **PostgreSQL**: Banco principal do projeto ORM
- **MongoDB**: Banco do projeto ODM  
- **Redis**: Message broker e cache para sincronização
- **API ORM**: Containerizada com todas as dependências
- **API ODM**: Containerizada independentemente
- **API Gateway**: Container separado para roteamento
- **Frontend**: Aplicação Next.js/Electron containerizada

#### **Benefícios da Containerização**
- Ambiente de desenvolvimento consistente
- Deploy simplificado com um único comando
- Isolamento completo de dependências
- Escalabilidade horizontal facilitada
- Configuração de rede entre containers otimizada

### **Arquitetura Final do Sistema**

A arquitetura final representa um sistema distribuído robusto e escalável:

```
Frontend (Next.js/Electron)
    ↓
API Gateway (Spring Cloud Gateway)
    ↓                    ↓
┌─────────────────┬─────────────────┐
│   API ORM       │   API ODM       │
│ (PostgreSQL)    │  (MongoDB)      │
└─────────────────┴─────────────────┘
    ↓                     ↑
Redis Pub/Sub + Queue System
```

### **Tecnologias Utilizadas na Solução Completa**

**Backend:**
- Java 21, Spring Boot 3.5.3
- Spring Data JPA, Spring Data MongoDB
- Spring Security + JWT
- Spring Cloud Gateway
- PostgreSQL, MongoDB, Redis
- Flyway para migrações
- Swagger/OpenAPI 3.0

**Containerização:**
- Docker, Docker Compose
- Multi-stage builds otimizados
- Network isolation e volume management

**Arquitetura:**
- Microserviços independentes
- Event-driven architecture
- API Gateway pattern
- Message queue com fallback

### **Contribuições para o Projeto**

Esta implementação representa uma evolução significativa da arquitetura inicial, transformando um sistema monolítico em uma solução distribuída moderna. As principais conquistas incluem:

1. **Flexibilidade**: Duas APIs independentes permitem escolher a melhor abordagem para cada caso de uso
2. **Consistência**: Sistema de sincronização garante integridade dos dados
3. **Robustez**: Mecanismos de fallback e retry garantem alta disponibilidade
4. **Escalabilidade**: Arquitetura de microserviços permite crescimento independente
5. **Manutenibilidade**: Separação clara de responsabilidades e documentação completa

### **Conclusão**

O projeto evoluiu de uma API simples para um ecossistema completo de microserviços, demonstrando domínio de tecnologias modernas de desenvolvimento. A implementação contempla desde conceitos básicos de persistência até arquiteturas distribuídas complexas, resultando em uma solução robusta e escalável para o sistema de gestão de bibliotecas.

A experiência adquirida abrange desenvolvimento full-stack, integração de sistemas, mensageria, containerização e arquiteturas distribuídas - competências fundamentais para o desenvolvimento de software moderno.
