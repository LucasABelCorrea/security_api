# Security API

API REST desenvolvida em **Spring Boot** para gerenciamento de **Firewalls** e **Vulnerabilidades** de segurança da informação. Projeto desenvolvido para fins de estudo na FIAP.

---

## 📋 Sumário

- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Pré-requisitos](#-pré-requisitos)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Como rodar a aplicação](#-como-rodar-a-aplicação)
  - [1. Subindo o Banco de Dados com Docker](#1-subindo-o-banco-de-dados-com-docker)
  - [2. Rodando a API Spring Boot](#2-rodando-a-api-spring-boot)
- [Documentação da API (Swagger)](#-documentação-da-api-swagger)
- [Endpoints Disponíveis](#-endpoints-disponíveis)
- [Exemplos de Requisições](#-exemplos-de-requisições)
- [Encerrando o ambiente](#-encerrando-o-ambiente)

---

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 4.0.3**
  - Spring Web MVC
  - Spring Data JPA
  - Spring Boot DevTools
- **MySQL 8** (via Docker)
- **Maven** (gerenciador de dependências — wrapper `mvnw` incluído)
- **Lombok** (redução de boilerplate)
- **SpringDoc OpenAPI / Swagger UI** (documentação interativa)

---

## ✅ Pré-requisitos

Antes de começar, você precisa ter instalado na sua máquina:

- [Java JDK 17+](https://adoptium.net/)
- [Docker](https://www.docker.com/products/docker-desktop/) e [Docker Compose](https://docs.docker.com/compose/install/)
- [Git](https://git-scm.com/) (para clonar o repositório)

> 💡 **Não é necessário ter o Maven instalado!** O projeto inclui o Maven Wrapper (`mvnw` / `mvnw.cmd`).

---

## 📂 Estrutura do Projeto

```
security_api/
├── src/
│   └── main/
│       ├── java/br/com/fiap/security_api/
│       │   ├── Application.java
│       │   ├── controller/
│       │   │   ├── FirewallController.java
│       │   │   └── VulnerabilidadeController.java
│       │   ├── model/
│       │   │   ├── Firewall.java
│       │   │   └── Vulnerabilidade.java
│       │   └── repository/
│       │       ├── FirewallRepository.java
│       │       └── VulnerabilidadeRepository.java
│       └── resources/
│           └── application.properties
├── docker-compose.yml
├── pom.xml
├── mvnw
└── README.md
```

---

## ▶️ Como rodar a aplicação

Siga os passos abaixo na ordem para subir o ambiente do zero.

### 1. Subindo o Banco de Dados com Docker

A aplicação espera um banco **MySQL** rodando em `localhost:3306` com as seguintes credenciais (definidas em `src/main/resources/application.properties`):

| Configuração | Valor |
|--------------|-------|
| Host         | `localhost` |
| Porta        | `3306` |
| Database     | `api` (criada automaticamente pela aplicação) |
| Usuário      | `root` |
| Senha        | `root_pwd` |

#### 📄 Crie o arquivo `docker-compose.yml` na raiz do projeto

> ⚠️ **Obrigatório:** se este arquivo ainda não existir no projeto, crie-o com o conteúdo abaixo. Ele é o que garante que qualquer pessoa consiga subir o banco do zero.

```yaml
version: "3.8"

services:
  mysql:
    image: mysql:8.0
    container_name: security_api_mysql
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: root_pwd
      MYSQL_DATABASE: api
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-uroot", "-proot_pwd"]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  mysql_data:
```

#### 🐳 Suba o container

Na raiz do projeto (onde está o `docker-compose.yml`), execute:

```bash
docker compose up -d
```

> Em versões mais antigas do Docker, use `docker-compose up -d` (com hífen).

Verifique se o container está rodando corretamente:

```bash
docker ps
```

Você deve ver o container `security_api_mysql` com status `Up` e `healthy`.

#### 🔧 Alternativa: subindo apenas com `docker run`

Se preferir não usar Docker Compose, pode subir o MySQL diretamente com o comando:

```bash
docker run -d \
  --name security_api_mysql \
  -e MYSQL_ROOT_PASSWORD=root_pwd \
  -e MYSQL_DATABASE=api \
  -p 3306:3306 \
  mysql:8.0
```

---

### 2. Rodando a API Spring Boot

Com o banco de dados rodando, abra um terminal na raiz do projeto e execute:

```bash
./mvn spring-boot:run
```

> Na primeira execução, o Maven irá baixar todas as dependências — pode levar alguns minutos.

A aplicação subirá em: **http://localhost:8080**

As tabelas `firewalls` e `vulnerabilidades` serão criadas automaticamente pelo Hibernate (`spring.jpa.hibernate.ddl-auto=update`).

---

## 📖 Documentação da API (Swagger)

Após subir a aplicação, acesse a documentação interativa em:

🔗 **http://localhost:8080/**

A interface do Swagger UI permite testar todos os endpoints diretamente pelo navegador.

A especificação OpenAPI (JSON) está disponível em:

🔗 **http://localhost:8080/v3/api-docs**

---

## 🔌 Endpoints Disponíveis

Todos os endpoints estão sob o prefixo `/api/v2`.

### Firewalls — `/api/v2/firewalls`

| Método | Endpoint                  | Descrição                          |
|--------|---------------------------|------------------------------------|
| POST   | `/api/v2/firewalls`       | Cria um novo firewall              |
| GET    | `/api/v2/firewalls`       | Lista todos os firewalls           |
| GET    | `/api/v2/firewalls/{id}`  | Busca um firewall pelo ID          |
| PUT    | `/api/v2/firewalls/{id}`  | Atualiza um firewall existente     |
| DELETE | `/api/v2/firewalls/{id}`  | Remove um firewall                 |

### Vulnerabilidades — `/api/v2/vulnerabilidades`

| Método | Endpoint                              | Descrição                              |
|--------|---------------------------------------|----------------------------------------|
| POST   | `/api/v2/vulnerabilidades`            | Cria uma nova vulnerabilidade          |
| GET    | `/api/v2/vulnerabilidades`            | Lista todas as vulnerabilidades        |
| GET    | `/api/v2/vulnerabilidades/{cve}`      | Busca uma vulnerabilidade pelo CVE     |
| PUT    | `/api/v2/vulnerabilidades/{cve}`      | Atualiza uma vulnerabilidade existente |
| DELETE | `/api/v2/vulnerabilidades/{cve}`      | Remove uma vulnerabilidade             |

---

## 🧪 Exemplos de Requisições

### Criar um Firewall

```bash
curl -X POST http://localhost:8080/api/v2/firewalls \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "nome": "FW-Core-01",
    "cluster": "DC-SP-01",
    "numBlades": 4,
    "vendor": "Check Point"
  }'
```

### Listar Firewalls

```bash
curl http://localhost:8080/api/v2/firewalls
```

### Criar uma Vulnerabilidade

```bash
curl -X POST http://localhost:8080/api/v2/vulnerabilidades \
  -H "Content-Type: application/json" \
  -d '{
    "cve": 202412345,
    "titulo": "Remote Code Execution",
    "severidade": 9.8,
    "versao": 3.1,
    "qtdAtivosAfetados": 12
  }'
```

### Buscar Vulnerabilidade por CVE

```bash
curl http://localhost:8080/api/v2/vulnerabilidades/202412345
```

---

## 🛑 Encerrando o ambiente

Para parar a aplicação Spring Boot, basta pressionar `Ctrl + C` no terminal onde ela está rodando.

Para parar e remover o container do MySQL:

```bash
# Parar o container (mantendo os dados)
docker compose stop

# Parar e remover o container (mantendo os dados no volume)
docker compose down

# Parar, remover o container E apagar os dados do volume
docker compose down -v
```

Se você usou `docker run` em vez de `docker compose`:

```bash
docker stop security_api_mysql
docker rm security_api_mysql
```

---

## 👨‍💻 Autor

Lucas Almeida Bel Correa - RM: 558539

Projeto desenvolvido para a disciplina de Microsservices — **FIAP - 3°SIR**.

---
