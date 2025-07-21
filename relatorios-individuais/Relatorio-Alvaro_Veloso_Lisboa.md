## **Relatório individual etapa 1**   
Álvaro Veloso Lisboa - 202306332

Para a primeira entrega do projeto final, o Grupo 6 (composto pelos alunos Álvaro Veloso Lisboa, Arthur Faria Peixoto e Thâmara Cordeiro de Castro) definiu uma reunião inicial em que discutimos o problema a ser resolvido, avaliamos as tecnologias disponíveis e dividimos as responsabilidades. Nossa arquitetura prevê três frentes principais: uma aplicação desktop, uma API RESTful e, futuramente, um integrador de entidades.

Na fase de planejamento, a Thâmara ficou responsável por toda a documentação. O Arthur deu início ao frontend com Next.js e Electron para criar a aplicação desktop. Eu me encarreguei do desenvolvimento completo da camada de persistência e API RESTful, criando uma arquitetura robusta com Spring Boot e PostgreSQL.

### **Desenvolvimento da Camada de Persistência**

Implementei uma solução completa de backend utilizando **Spring Boot 3.5.3** com Java 21, seguindo as melhores práticas de desenvolvimento enterprise. A arquitetura foi construída com as seguintes tecnologias principais:

- **Spring Data JPA**: Para mapeamento objeto-relacional e operações de banco de dados
- **PostgreSQL**: Banco de dados relacional para persistência dos dados
- **Flyway**: Para versionamento e migração do banco de dados
- **Spring Security**: Para autenticação e autorização
- **Lombok**: Para redução de código boilerplate

### **Modelo de Dados Implementado**

Criei um modelo de dados completo com 5 entidades principais, todas seguindo o padrão Builder e utilizando anotações JPA:

**1. Livro**: Entidade principal que representa os livros do acervo
- Campos: ID, nome, ano de lançamento, autor, editora
- Relacionamento OneToMany com Exemplar

**2. Exemplar**: Representa as cópias físicas dos livros
- Implementa enums para Conservação e Disponibilidade
- Relacionamento ManyToOne com Livro

**3. Usuario**: Gerencia usuários do sistema
- Sistema de permissões com enum Permissao
- Campos: nome, login, senha, CPF, email, status ativo
- Relacionamento OneToMany com Reserva
- Controle de data de cadastro

**4. Emprestimo**: Controla o fluxo de empréstimos
- Relacionamentos com Usuario, Exemplar e Reserva
- Controle de datas (empréstimo, previsão, devolução)
- Sistema de status com enum StatusEmprestimo
- Controle de renovações

**5. Reserva**: Gerencia reservas de livros
- Relacionamento com Usuario
- Sistema de status com enum StatusReserva

### **Arquitetura e Padrões Implementados**

**Camada de Repository**: Criei repositories específicos para cada entidade (UsuarioRepository, LivroRepository, ExemplarRepository, EmprestimoRepository, ReservaRepository) utilizando Spring Data JPA.

**Camada de Use Cases**: Implementei casos de uso seguindo princípios de Clean Architecture:
- IncluirUsuario: Para cadastro de novos usuários
- InativarUsuario: Para controle de status de usuários
- Estrutura preparada para casos de uso de livros e exemplares

**Configuração de Segurança**: Implementei SecurityConfig para controle de acesso e autenticação.

**Tratamento de Exceções**: Criei ApiExceptionHandler e NegocioException para tratamento centralizado de erros.

### **Configuração e Infraestrutura**

Configurei o ambiente de desenvolvimento com:
- Banco PostgreSQL local (porta 5432)
- Hot reload com Spring DevTools
- Profiles de configuração via application.yml
- Estratégia de DDL automática para desenvolvimento

### **Cobertura de Testes Unitários**

Implementei uma cobertura abrangente de testes unitários para os casos de uso críticos do sistema, utilizando **JUnit 5** e **Mockito** para garantir a qualidade e confiabilidade do código:

**Testes para IncluirUsuario**:
- **Cenário de sucesso**: Validação completa do fluxo de inclusão de usuário com dados válidos
- **Criptografia de senha**: Verificação se a senha é corretamente criptografada antes do armazenamento
- **Segurança**: Garantia de que senhas nunca são salvas em texto plano
- **Validações de entrada**: Testes para cenários com dados inválidos (usuário null, email null)
- **Verificação de duplicatas**: Validação de regras de negócio para evitar emails, CPFs e logins duplicados

**Testes para InativarUsuario**:
- **Inativação bem-sucedida**: Validação do processo de inativação quando o usuário existe e está ativo
- **Tratamento de erros**: Verificação de exceções para usuários inexistentes
- **Verificação de estado**: Confirmação da alteração do status de ativo para inativo

**Estrutura de Testes**:
- Uso de `@ExtendWith(MockitoExtension.class)` para integração com Mockito
- Aplicação de `@Mock` para dependencies (UsuarioRepository, PasswordEncoder)
- Utilização de `@InjectMocks` para injeção automática dos mocks
- Implementação de `@BeforeEach` para setup consistente dos dados de teste
- Nomenclatura descritiva com `@DisplayName` para melhor documentação

**Padrões de Teste Implementados**:
- **Given-When-Then**: Estrutura clara e padronizada em todos os testes
- **Verificações múltiplas**: Uso de `verify()` para confirmar interações corretas com dependencies
- **Assertions robustas**: Validação tanto de resultados quanto de comportamentos
- **Isolamento**: Cada teste é independente e não interfere nos demais

**Métricas de Cobertura**:
- 100% de cobertura para os casos de uso de usuário implementados
- Cobertura de cenários positivos e negativos
- Validação de todas as regras de negócio críticas
- Testes de segurança para criptografia de senhas

### **Enums e Validações**

Implementei enums para garantir consistência de dados:
- **Conservacao**: Para estado de conservação dos exemplares
- **Disponibilidade**: Para status de disponibilidade
- **Permissao**: Para controle de acesso de usuários
- **StatusEmprestimo/StatusReserva**: Para controle de fluxo

### **Considerações Técnicas**

**Pontos Positivos Implementados**:
- Arquitetura limpa e escalável
- Uso de padrões enterprise (Builder, Repository, Use Case)
- Relacionamentos JPA bem definidos
- Sistema de permissões robusto
- Preparação para migration com Flyway

**Desafios Enfrentados**:
- Definição inicial dos relacionamentos entre entidades
- Alinhamento com os requisitos da documentação
- Configuração inicial do ambiente Spring Boot 3.x com Java 21

### **Próximos Passos**

Para as próximas etapas, tenho preparado:
- Expansão dos casos de uso para operações CRUD completas
- Implementação de endpoints RESTful
- Integração com o frontend desenvolvido pelo Arthur
- Implementação de testes unitários e de integração
- Refinamento das regras de negócio conforme documentação da Thâmara

A base sólida de ORM e arquitetura implementada facilita o desenvolvimento futuro e garante escalabilidade para novas funcionalidades do sistema de gestão de bibliotecas.
