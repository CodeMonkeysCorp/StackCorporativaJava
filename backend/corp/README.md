# Corporate Activity Manager - Backend (Spring Boot)

Um gerenciador de atividades corporativas construído com **Spring Boot 3.5.7**, **MariaDB**, e **Docker Compose** para gerenciamento simples de tasks, projetos e times.

## Tecnologias

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Data JPA + Hibernate**
- **Spring Security + JWT (jjwt)**
- **MariaDB 10.11**
- **Maven 3.9.8**
- **Docker & Docker Compose**

## Estrutura do Projeto

```
backend/corp/
├── src/
│   ├── main/
│   │   ├── java/com/project/corp/
│   │   │   ├── CorpApplication.java          # App principal
│   │   │   ├── model/                        # Entidades JPA
│   │   │   │   ├── Task.java
│   │   │   │   ├── Project.java
│   │   │   │   ├── Team.java
│   │   │   │   └── User.java
│   │   │   ├── repository/                   # JPA Repositories
│   │   │   │   ├── TaskRepository.java
│   │   │   │   ├── ProjectRepository.java
│   │   │   │   └── TeamRepository.java
│   │   │   ├── service/                      # Lógica de negócio
│   │   │   │   ├── TaskService.java
│   │   │   │   ├── ProjectService.java
│   │   │   │   └── TeamService.java
│   │   │   ├── controller/                   # REST API
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── TaskController.java
│   │   │   │   ├── ProjectController.java
│   │   │   │   └── TeamController.java
│   │   │   ├── security/                     # JWT e autenticação
│   │   │   │   ├── JwtProvider.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   └── config/
│   │   │       └── DataInitializer.java
│   │   └── resources/
│   │       └── application.properties        # Configuração Spring
│   └── test/
├── pom.xml
├── docker-compose.yml
├── Dockerfile
├── mvnw / mvnw.cmd
└── README.md
```

## Quick Start

### Opção 1: Docker Compose (Recomendado)

Suba a aplicação e o MariaDB com um único comando:

```bash
cd backend/corp
docker-compose up
```

A aplicação estará em: **http://localhost:8080**  
MariaDB em: **localhost:3306** (usuário: `root`, senha: `admin`)

Para parar:

```bash
docker-compose down
```

### Opção 2: Execução Local

Certifique-se de ter MariaDB rodando (ou use Docker apenas para o BD):

```bash
# Inicie apenas o MariaDB
cd backend/corp
docker-compose up mariadb -d

# Em outro terminal, rode a aplicação
cd backend/corp
./mvnw spring-boot:run
```

**Nota**: Para Windows, use `mvnw.cmd` em vez de `mvnw`.

## Configuração do Banco de Dados

As variáveis de ambiente são:

```properties
DB_URL=jdbc:mariadb://localhost:3306/corporativa
DB_USER=root
DB_PASS=admin
```

Você pode sobrescrevê-las via variáveis de ambiente ou editando `src/main/resources/application.properties`.

## Autenticação JWT

A API utiliza **Spring Security + JWT (jjwt)** para autenticação. Todos os endpoints (exceto `/api/auth/**`) requerem um token Bearer válido.

### Credenciais Padrão (criadas na inicialização)

- **Admin**
  - Username: `admin`
  - Password: `admin123`
- **User**
  - Username: `user`
  - Password: `user123`

### Como Autenticar

1. **Registrar novo usuário**:

   ```bash
   curl -X POST http://localhost:8080/api/auth/register \
     -H "Content-Type: application/json" \
     -d '{
       "name": "John Doe",
       "username": "john",
       "email": "john@example.com",
       "password": "senha123"
     }'
   ```

2. **Fazer login**:

   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{
       "username": "admin",
       "password": "admin123"
     }'
   ```

   Resposta:

   ```json
   {
     "token": "eyJhbGciOiJIUzUxMiJ9...",
     "username": "admin",
     "role": "ROLE_USER"
   }
   ```

3. **Usar token em requisições autenticadas**:
   ```bash
   curl -X GET http://localhost:8080/api/tasks \
     -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
   ```

### Configuração JWT (environment)

```properties
JWT_SECRET=your-secret-key-min-256-bits
JWT_EXPIRATION=86400000  # 24 horas em ms
```

## API Endpoints

### Auth (sem autenticação)

- **POST** `/api/auth/register` — Registrar novo usuário
- **POST** `/api/auth/login` — Fazer login e obter token JWT

### Tasks (requer autenticação)

- **GET** `/api/tasks` — Listar todas as tarefas
- **GET** `/api/tasks/completed` — Listar tarefas concluídas
- **GET** `/api/tasks/pending` — Listar tarefas pendentes
- **GET** `/api/tasks/{id}` — Obter tarefa por ID
- **POST** `/api/tasks` — Criar tarefa
- **PUT** `/api/tasks/{id}` — Atualizar tarefa
- **PATCH** `/api/tasks/{id}/complete` — Marcar como concluída
- **PATCH** `/api/tasks/{id}/pending` — Marcar como pendente
- **DELETE** `/api/tasks/{id}` — Deletar tarefa

### Projects

- **GET** `/api/projects` — Listar projetos
- **GET** `/api/projects/{id}` — Obter projeto
- **POST** `/api/projects` — Criar projeto
- **PUT** `/api/projects/{id}` — Atualizar projeto
- **DELETE** `/api/projects/{id}` — Deletar projeto

### Teams

- **GET** `/api/teams` — Listar times
- **GET** `/api/teams/{id}` — Obter time
- **POST** `/api/teams` — Criar time
- **PUT** `/api/teams/{id}` — Atualizar time
- **DELETE** `/api/teams/{id}` — Deletar time

## Exemplos de Requisições

### Criar Tarefa

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Revisar código",
    "description": "Revisar PRs pendentes",
    "completed": false
  }'
```

### Criar Projeto

```bash
curl -X POST http://localhost:8080/api/projects \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Portal Corporativo",
    "description": "Portal de acesso para colaboradores"
  }'
```

### Criar Time

```bash
curl -X POST http://localhost:8080/api/teams \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Backend Team",
    "description": "Equipe de desenvolvimento backend"
  }'
```

### Marcar Tarefa como Concluída

```bash
curl -X PATCH http://localhost:8080/api/tasks/1/complete
```

### Listar Tarefas Pendentes

```bash
curl http://localhost:8080/api/tasks/pending
```

## Build & Test

### Build

```bash
cd backend/corp
./mvnw clean package
```

### Rodar Testes

```bash
cd backend/corp
./mvnw test
```

### Executar Localmente (sem Docker)

```bash
cd backend/corp
./mvnw spring-boot:run
```

## Variáveis de Ambiente (Docker)

No `docker-compose.yml`, a app recebe automaticamente:

```yaml
environment:
  DB_URL: jdbc:mariadb://mariadb:3306/corporativa
  DB_USER: root
  DB_PASS: admin
```

Para ambiente de produção, substitua pelos valores reais.

## Logs

Os logs são exibidos no console com informações de:

- Inicialização da aplicação
- Criação/atualização/deleção de registros
- Queries SQL (ativado via `spring.jpa.show-sql=true`)

## Próximos Passos

- [x] Implementar **Spring Security + JWT** para autenticação
- [ ] Adicionar **Flyway** para migrações versionadas
- [ ] Adicionar **validação** com `@Valid` e DTOs
- [ ] Tratamento de erros global com `@ControllerAdvice`
- [ ] Documentação OpenAPI (Swagger) com `springdoc`
- [ ] Testes automatizados (JUnit 5 + Testcontainers)
- [ ] Integração contínua com GitHub Actions

## Contribuindo

1. Crie uma branch para sua feature: `git checkout -b feature/xyz`
2. Commit suas mudanças: `git commit -am 'Add feature xyz'`
3. Push para a branch: `git push origin feature/xyz`
4. Abra um Pull Request

## Licença

MIT

---

**Dúvidas?** Consulte a documentação de Spring Boot: https://spring.io/projects/spring-boot
