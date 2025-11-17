# Docker Compose - StackCorporativaJava

Orquestração completa de todos os 3 serviços (Frontend, Backend, MariaDB) em containers Docker.

## 📋 Pré-requisitos

- Docker 20.10+
- Docker Compose 2.0+
- Git

## 🚀 Quick Start

### 1. Clone e entre no diretório

```bash
git clone https://github.com/CodeMonkeysCorp/StackCorporativaJava.git
cd StackCorporativaJava
```

### 2. Configure variáveis de ambiente (opcional)

```bash
# Copie o arquivo de exemplo
cp .env.example .env

# Edite conforme necessário (se desejar alterar portas, senhas, etc)
# vim .env
```

### 3. Inicie todos os serviços

```bash
docker-compose up
```

Ou em background:

```bash
docker-compose up -d
```

### 4. Aguarde a inicialização

```
✅ mariadb      - Banco de dados (porta 3306)
✅ backend      - API Spring Boot (porta 8080)
✅ frontend     - Angular Nginx (porta 3000)
```

## 🌐 Acessar Serviços

| Serviço      | URL                   | Descrição            |
| ------------ | --------------------- | -------------------- |
| **Frontend** | http://localhost:3000 | Aplicação Angular    |
| **Backend**  | http://localhost:8080 | API REST Spring Boot |
| **MariaDB**  | localhost:3306        | Banco de dados       |

## 🔐 Credenciais Padrão

### Banco de Dados (MariaDB)

```
Host: mariadb
Port: 3306
User: root
Password: admin
Database: corporativa
```

### API (Backend)

```bash
# Login
POST http://localhost:8080/api/auth/login
Body: {
  "username": "admin",
  "password": "admin123"
}

# Response
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "username": "admin",
  "role": "ROLE_ADMIN"
}
```

## 📊 Estrutura dos Serviços

```
docker-compose.yml
├── mariadb (MariaDB 10.11)
│   └── Volume: mariadb_data
├── backend (Spring Boot)
│   ├── Build: ./backend/corp/Dockerfile
│   ├── Depends on: mariadb (healthcheck)
│   └── Port: 8080
└── frontend (Angular + Nginx)
    ├── Build: ./frontend/Dockerfile
    ├── Depends on: backend
    └── Port: 3000
```

## 🛠️ Comandos Úteis

### Iniciar serviços

```bash
docker-compose up              # Modo interativo
docker-compose up -d           # Background
docker-compose up --build      # Rebuild imagens
```

### Parar serviços

```bash
docker-compose down            # Parar e remover containers
docker-compose down -v         # Parar e remover volumes também
docker-compose stop            # Apenas parar (sem remover)
```

### Logs

```bash
docker-compose logs            # Todos os serviços
docker-compose logs -f         # Follow mode
docker-compose logs backend    # Apenas backend
docker-compose logs -f backend # Backend com follow
```

### Executar comandos

```bash
# Acessar bash do backend
docker-compose exec backend bash

# Executar comando no MariaDB
docker-compose exec mariadb mysql -u root -padmin -e "SHOW DATABASES;"

# Acessar bash do frontend
docker-compose exec frontend bash
```

### Reiniciar

```bash
docker-compose restart         # Todos
docker-compose restart backend # Apenas backend
```

## 📁 Variáveis de Ambiente

Arquivo: `.env.example` ou `.env`

```env
# Database
DB_ROOT_PASSWORD=admin
DB_NAME=corporativa
DB_USER=root
DB_PASS=admin
DB_PORT=3306

# Backend
JWT_SECRET=my-super-secret-key-change-in-production
JWT_EXPIRATION=86400000
BACKEND_PORT=8080

# Frontend
API_BASE_URL=http://localhost:8080
FRONTEND_PORT=3000
```

## 🔍 Verificar Saúde dos Serviços

```bash
# Status dos containers
docker-compose ps

# Listar imagens
docker-compose images

# Inspecionar rede
docker network ls
docker network inspect stackcorporativajava_corp-network
```

## 🐛 Troubleshooting

### Porta já em uso

```bash
# Encontrar processo usando porta
lsof -i :8080  # Backend
lsof -i :3000  # Frontend
lsof -i :3306  # MariaDB

# Matar processo (Windows)
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Container não inicia

```bash
# Ver logs detalhados
docker-compose logs backend

# Rebuild
docker-compose build --no-cache backend
docker-compose up backend
```

### Conexão com banco de dados falha

```bash
# Verificar saúde do MariaDB
docker-compose exec mariadb mariadb-admin ping -h localhost

# Reconectar
docker-compose restart backend
```

### Limpar tudo

```bash
docker-compose down -v        # Remove tudo
docker system prune -a        # Limpa images não usadas
docker volume prune           # Remove volumes órfãos
```

## 📚 Documentação Completa

- [PROJECT_DESCRIPTION.md](../PROJECT_DESCRIPTION.md) — Visão geral do projeto
- [ARCHITECTURE.md](../ARCHITECTURE.md) — Arquitetura detalhada
- [backend/corp/README.md](../backend/corp/README.md) — Setup do backend
- [frontend/README.md](../frontend/README.md) — Setup do frontend

## 🚀 Próximos Passos

1. **Desenvolvimento Local**: Use `docker-compose up` para ambiente local completo
2. **CI/CD**: Workflows GitHub Actions automatizam build e testes
3. **Staging**: Deploy em ambiente containerizado para testes
4. **Produção**: Configure segredos (JWT_SECRET), volumes persistentes, etc

## 📝 Notas

- **Desenvolvimento**: Use `docker-compose up` para ambiente local
- **Produção**: Configure `.env` com valores seguros antes de deploy
- **Secrets**: Altere `JWT_SECRET` em produção
- **Volumes**: MariaDB usa volume persistente (`mariadb_data`)
- **Network**: Todos os serviços na rede `corp-network`
- **Health Checks**: Mariadb aguarda health check antes do backend iniciar

## 🆘 Suporte

Para reportar problemas:

1. Verifique logs: `docker-compose logs`
2. Consulte [CONTRIBUTING.md](../CONTRIBUTING.md)
3. Abra uma Issue no GitHub

---

**Data**: 17 de novembro de 2025  
**Versão**: 1.0  
**Mantido por**: CodeMonkeysCorp
