# Security API

API REST desenvolvida em **Spring Boot** para gerenciamento de **Firewalls** e **Vulnerabilidades** de segurança da informação. Projeto desenvolvido para fins de estudo na FIAP.

A aplicação foi refatorada para seguir uma separação de responsabilidades mais clara, utilizando **Controllers**, **Services**, **Repositories**, **Models** e **DTOs**.

---

## 📋 Sumário

- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Pré-requisitos](#-pré-requisitos)
- [Novidades da versão atual](#-novidades-da-versão-atual)
- [Arquitetura da Aplicação](#-arquitetura-da-aplicação)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Como rodar a aplicação](#-como-rodar-a-aplicação)
  - [1. Subindo o Banco de Dados com Docker](#1-subindo-o-banco-de-dados-com-docker)
  - [2. Rodando a API Spring Boot](#2-rodando-a-api-spring-boot)
- [Documentação da API (Swagger)](#-documentação-da-api-swagger)
- [Endpoints Disponíveis](#-endpoints-disponíveis)
- [Modelos, DTOs e campos esperados](#-modelos-dtos-e-campos-esperados)
- [Exemplos de Requisições](#-exemplos-de-requisições)
- [Encerrando o ambiente](#-encerrando-o-ambiente)
- [Autor](#-autor)

---

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 4.0.3**
  - Spring Web MVC
  - Spring Data JPA
  - Spring Boot DevTools
  - Spring Validation
- **MySQL 8**
- **Maven** (gerenciador de dependências — wrapper `mvnw` incluído)
- **Lombok** (redução de boilerplate)
- **ModelMapper** (mapeamento entre DTOs e Models)
- **SpringDoc OpenAPI / Swagger UI** (documentação interativa)

---

## ✅ Pré-requisitos

Antes de começar, você precisa ter instalado na sua máquina:

- [Java JDK 17+](https://adoptium.net/)
- [Docker](https://www.docker.com/products/docker-desktop/) e [Docker Compose](https://docs.docker.com/compose/install/)
- [Git](https://git-scm.com/) (para clonar o repositório)

> 💡 **Não é necessário ter o Maven instalado!** O projeto inclui o Maven Wrapper (`mvnw` / `mvnw.cmd`).

---

## 🆕 Novidades da versão atual

Esta versão inclui uma refatoração estrutural da API, com foco em organização, manutenção e padronização das entradas e saídas dos endpoints.

### Camada de Service

Foi criada uma camada de serviço para centralizar as regras de acesso e manipulação dos dados:

- `FirewallService`
- `VulnerabilidadeService`

Os Controllers não acessam mais diretamente os Repositories. Agora eles chamam os Services, que por sua vez utilizam os Repositories para persistência e consulta no banco de dados.

### DTOs de entrada e saída

Foram adicionados DTOs para separar os dados recebidos e retornados pela API dos Models persistidos no banco.

Para **Firewalls**:

- `FirewallCreateRequest`
- `FirewallUpdateRequest`
- `FirewallResponse`
- `FirewallMapper`

Para **Vulnerabilidades**:

- `VulnerabilidadeCreateRequest`
- `VulnerabilidadeUpdateRequest`
- `VulnerabilidadeResponse`
- `VulnerabilidadeMapper`

Essa separação evita expor diretamente as entidades JPA nos endpoints e permite controlar melhor quais campos entram no cadastro, quais campos entram na atualização e quais campos são devolvidos na resposta.

### Geração automática de ID no Model

Os identificadores agora são gerados automaticamente pelo banco/JPA com `@GeneratedValue(strategy = GenerationType.AUTO)`:

- `Firewall.id`
- `Vulnerabilidade.cve`

Com isso, os campos `id` e `cve` **não devem ser enviados no corpo das requisições POST**. Eles são retornados pela API depois que o registro é criado.

### Controllers refatorados

Os Controllers foram ajustados para:

- Receber DTOs de request com `@RequestBody`.
- Validar entradas com `@Valid`.
- Usar os Mappers para converter DTOs em Models e Models em DTOs de resposta.
- Delegar operações de criação, consulta, atualização e remoção para a camada de Service.
- Retornar `ResponseEntity` com status HTTP adequado, como `201 Created`, `200 OK`, `204 No Content` e `404 Not Found`.

---

## 🧱 Arquitetura da Aplicação

A aplicação está organizada em camadas:

| Camada | Responsabilidade |
|--------|------------------|
| `controller` | Expõe os endpoints REST e recebe as requisições HTTP. |
| `dto` | Define objetos de entrada, saída e mapeamento entre DTOs e Models. |
| `service` | Centraliza a lógica de aplicação e intermedia Controller e Repository. |
| `repository` | Realiza a comunicação com o banco usando Spring Data JPA. |
| `model` | Representa as entidades JPA persistidas no banco de dados. |

Fluxo principal da API:

```text
Requisição HTTP
      ↓
Controller
      ↓
DTO / Mapper
      ↓
Service
      ↓
Repository
      ↓
Banco de Dados
```

---

## 📂 Estrutura do Projeto

```text
security_api/
├── src/
│   └── main/
│       ├── java/br/com/fiap/security_api/
│       │   ├── Application.java
│       │   ├── controller/
│       │   │   ├── FirewallController.java
│       │   │   └── VulnerabilidadeController.java
│       │   ├── dto/
│       │   │   ├── FirewallCreateRequest.java
│       │   │   ├── FirewallMapper.java
│       │   │   ├── FirewallResponse.java
│       │   │   ├── FirewallUpdateRequest.java
│       │   │   ├── VulnerabilidadeCreateRequest.java
│       │   │   ├── VulnerabilidadeMapper.java
│       │   │   ├── VulnerabilidadeResponse.java
│       │   │   └── VulnerabilidadeUpdateRequest.java
│       │   ├── model/
│       │   │   ├── Firewall.java
│       │   │   └── Vulnerabilidade.java
│       │   ├── repository/
│       │   │   ├── FirewallRepository.java
│       │   │   └── VulnerabilidadeRepository.java
│       │   └── service/
│       │       ├── FirewallService.java
│       │       └── VulnerabilidadeService.java
│       └── resources/
│           └── application.properties
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

## ▶️ Como rodar a aplicação

Siga os passos abaixo na ordem para subir o ambiente do zero.

### 1. Subindo o Banco de Dados com Docker

A aplicação espera um banco **MySQL** rodando em `localhost:3306` com as seguintes credenciais, definidas em `src/main/resources/application.properties`:

| Configuração | Valor |
|--------------|-------|
| Host | `localhost` |
| Porta | `3306` |
| Database | `api` |
| Usuário | `root` |
| Senha | `root_pwd` |

A URL configurada cria o banco automaticamente se ele ainda não existir:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/api?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
```


#### Opção com Docker Run

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
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

A aplicação subirá em:

```text
http://localhost:8080
```

As tabelas `firewalls` e `vulnerabilidades` serão criadas/atualizadas automaticamente pelo Hibernate, conforme a configuração:

```properties
spring.jpa.hibernate.ddl-auto=update
```

---

## 📖 Documentação da API (Swagger)

Após subir a aplicação, acesse a documentação interativa em:

```text
http://localhost:8080/
```

A interface do Swagger UI permite testar todos os endpoints diretamente pelo navegador.

A especificação OpenAPI em JSON está disponível em:

```text
http://localhost:8080/v3/api-docs
```

---

## 🔌 Endpoints Disponíveis

Todos os endpoints utilizam o prefixo configurado em `application.properties`:

```properties
api.version=v2
```

Portanto, o prefixo atual é:

```text
/api/v2
```

### Firewalls — `/api/v2/firewalls`

| Método | Endpoint | Descrição | Body |
|--------|----------|-----------|------|
| POST | `/api/v2/firewalls` | Cria um novo firewall | `FirewallCreateRequest` |
| GET | `/api/v2/firewalls` | Lista todos os firewalls | Não possui |
| GET | `/api/v2/firewalls/{id}` | Busca um firewall pelo ID | Não possui |
| PUT | `/api/v2/firewalls/{id}` | Atualiza um firewall existente | `FirewallUpdateRequest` |
| DELETE | `/api/v2/firewalls/{id}` | Remove um firewall | Não possui |

### Vulnerabilidades — `/api/v2/vulnerabilidades`

| Método | Endpoint | Descrição | Body |
|--------|----------|-----------|------|
| POST | `/api/v2/vulnerabilidades` | Cria uma nova vulnerabilidade | `VulnerabilidadeCreateRequest` |
| GET | `/api/v2/vulnerabilidades` | Lista todas as vulnerabilidades | Não possui |
| GET | `/api/v2/vulnerabilidades/{cve}` | Busca uma vulnerabilidade pelo identificador CVE gerado | Não possui |
| PUT | `/api/v2/vulnerabilidades/{cve}` | Atualiza uma vulnerabilidade existente | `VulnerabilidadeUpdateRequest` |
| DELETE | `/api/v2/vulnerabilidades/{cve}` | Remove uma vulnerabilidade | Não possui |

---

## 📦 Modelos, DTOs e campos esperados

### Firewall

Entidade persistida: `Firewall`

| Campo | Tipo | Observação |
|-------|------|------------|
| `id` | `Long` | Gerado automaticamente. |
| `nome` | `String` | Obrigatório. |
| `cluster` | `String` | Opcional. |
| `numBlades` | `BigDecimal` | Obrigatório. |
| `vendor` | `String` | Obrigatório. |

#### `FirewallCreateRequest`

Usado no `POST /api/v2/firewalls`.

```json
{
  "nome": "FW-Core-01",
  "cluster": "DC-SP-01",
  "numBlades": 4,
  "vendor": "Check Point"
}
```

#### `FirewallUpdateRequest`

Usado no `PUT /api/v2/firewalls/{id}`.

```json
{
  "nome": "FW-Core-01-Atualizado",
  "cluster": "DC-SP-02",
  "numBlades": 6,
  "vendor": "Check Point"
}
```

#### `FirewallResponse`

Resposta retornada pela API.

```json
{
  "id": 1,
  "nome": "FW-Core-01",
  "cluster": "DC-SP-01",
  "numBlades": 4,
  "vendor": "Check Point"
}
```

---

### Vulnerabilidade

Entidade persistida: `Vulnerabilidade`

| Campo | Tipo | Observação |
|-------|------|------------|
| `cve` | `Long` | Gerado automaticamente. |
| `titulo` | `String` | Obrigatório. |
| `severidade` | `BigDecimal` | Obrigatório. |
| `versao` | `BigDecimal` | Obrigatório. |
| `qtdAtivosAfetados` | `Integer` | Obrigatório. |

#### `VulnerabilidadeCreateRequest`

Usado no `POST /api/v2/vulnerabilidades`.

```json
{
  "titulo": "Remote Code Execution",
  "severidade": 9.8,
  "versao": 3.1,
  "qtdAtivosAfetados": 12
}
```

#### `VulnerabilidadeUpdateRequest`

Usado no `PUT /api/v2/vulnerabilidades/{cve}`.

```json
{
  "titulo": "Remote Code Execution - Atualizada",
  "severidade": 9.9,
  "versao": 3.1,
  "qtdAtivosAfetados": 18
}
```

#### `VulnerabilidadeResponse`

Resposta retornada pela API.

```json
{
  "cve": 1,
  "titulo": "Remote Code Execution",
  "severidade": 9.8,
  "versao": 3.1,
  "qtdAtivosAfetados": 12
}
```

---

## 🧪 Exemplos de Requisições

### Criar um Firewall

> O campo `id` não deve ser enviado. Ele é gerado automaticamente.

```bash
curl -X POST http://localhost:8080/api/v2/firewalls \
  -H "Content-Type: application/json" \
  -d '{
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

### Buscar Firewall por ID

```bash
curl http://localhost:8080/api/v2/firewalls/1
```

### Atualizar um Firewall

```bash
curl -X PUT http://localhost:8080/api/v2/firewalls/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "FW-Core-01-Atualizado",
    "cluster": "DC-SP-02",
    "numBlades": 6,
    "vendor": "Check Point"
  }'
```

### Remover um Firewall

```bash
curl -X DELETE http://localhost:8080/api/v2/firewalls/1
```

---

### Criar uma Vulnerabilidade

> O campo `cve` não deve ser enviado. Ele é gerado automaticamente.

```bash
curl -X POST http://localhost:8080/api/v2/vulnerabilidades \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Remote Code Execution",
    "severidade": 9.8,
    "versao": 3.1,
    "qtdAtivosAfetados": 12
  }'
```

### Listar Vulnerabilidades

```bash
curl http://localhost:8080/api/v2/vulnerabilidades
```

### Buscar Vulnerabilidade por CVE

```bash
curl http://localhost:8080/api/v2/vulnerabilidades/1
```

### Atualizar uma Vulnerabilidade

```bash
curl -X PUT http://localhost:8080/api/v2/vulnerabilidades/1 \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Remote Code Execution - Atualizada",
    "severidade": 9.9,
    "versao": 3.1,
    "qtdAtivosAfetados": 18
  }'
```

### Remover uma Vulnerabilidade

```bash
curl -X DELETE http://localhost:8080/api/v2/vulnerabilidades/1
```

---

## 🛑 Encerrando o ambiente

Para parar a aplicação Spring Boot, pressione `Ctrl + C` no terminal onde ela está rodando.

Para parar e remover o container do MySQL com Docker Compose:

```bash
# Parar o container mantendo os dados
docker compose stop

# Parar e remover o container mantendo os dados no volume
docker compose down

# Parar, remover o container e apagar os dados do volume
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
