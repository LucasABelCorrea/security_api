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
  - [1. Baixando a imagem do Docker Hub](#1-baixando-a-imagem-do-docker-hub)
  - [2. Subindo o banco de dados MySQL](#2-subindo-o-banco-de-dados-mysql)
  - [3. Variáveis de ambiente necessárias](#3-variáveis-de-ambiente-necessárias)
  - [4. Executando a aplicação com docker run](#4-executando-a-aplicação-com-docker-run)
  - [5. Acessando o Swagger / OpenAPI](#5-acessando-o-swagger--openapi)
  - [6. Encerrando os containers](#6-encerrando-os-containers)
- [Rodando a partir do código-fonte (desenvolvimento local)](#️-rodando-a-partir-do-código-fonte-desenvolvimento-local)
- [Endpoints Disponíveis](#-endpoints-disponíveis)
- [Modelos, DTOs e campos esperados](#-modelos-dtos-e-campos-esperados)
- [Exemplos de Requisições](#-exemplos-de-requisições)
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

A forma recomendada de executar a aplicação é a partir da imagem publicada no **Docker Hub**. Não é necessário clonar o repositório, instalar o Java ou compilar o projeto — basta ter o **Docker** instalado.

Imagem oficial: [`lucasbel/security_api:1.0.0`](https://hub.docker.com/r/lucasbel/security_api)

Siga os passos abaixo na ordem.

---

### 1. Baixando a imagem do Docker Hub

Faça o download da imagem da aplicação:

```bash
docker pull lucasbel/security_api:1.0.0
```

Para confirmar que a imagem foi baixada com sucesso:

```bash
docker images
```

---

### 2. Subindo o banco de dados MySQL

A aplicação depende de um banco **MySQL**. Suba um container antes de iniciar a API:

```bash
docker run -d \
  --name mysql \
  --rm \
  -e MYSQL_ROOT_PASSWORD=root_pwd \
  -e MYSQL_USER=new_user \
  -e MYSQL_PASSWORD=my_pwd \
  -p 3306:3306 \
  mysql
```

> ⏳ Aguarde alguns segundos até o MySQL inicializar completamente antes de seguir para o próximo passo.

---

### 3. Variáveis de ambiente necessárias

A imagem recebe toda a configuração por variáveis de ambiente, passadas com a flag `-e` no `docker run`:

| Variável | Descrição | Valor de exemplo |
|----------|-----------|------------------|
| `DB_SERVER_URL` | Host do servidor MySQL | `host.docker.internal` |
| `DB_SERVER_PORT` | Porta do servidor MySQL | `3306` |
| `DB_SCHEMA` | Nome do schema/banco de dados | `security` |
| `DB_USER` | Usuário do banco de dados | `root` |
| `DB_PWD` | Senha do banco de dados | `root_pwd` |
| `SPRING_PROFILES_ACTIVE` | Profile ativo da aplicação (`default` ou `prd`) | `default` |

> 💡 **Sobre o `host.docker.internal`:** esse hostname permite que o container da API acesse o MySQL rodando na máquina host. No **Linux**, adicione também `--add-host=host.docker.internal:host-gateway` ao comando `docker run`.

---

### 4. Executando a aplicação com `docker run`

O comando mapeia a porta **8080**, define o **profile** e passa as **variáveis de ambiente** de conexão com o banco.

#### Profile `default` — recomendado para testar a imagem

Cria o banco e as tabelas automaticamente. É o profile ideal para o primeiro teste, pois não exige nenhuma preparação prévia do banco:

```bash
docker run -p 8080:8080 \
  -e DB_SERVER_URL=host.docker.internal \
  -e DB_SERVER_PORT=3306 \
  -e DB_SCHEMA=security \
  -e DB_USER=root \
  -e DB_PWD=root_pwd \
  -e SPRING_PROFILES_ACTIVE=default \
  lucasbel/security_api:1.0.0
```

#### Profile `prd` — execução em produção

Não altera o schema do banco. Exige que o banco **e as tabelas já existam** previamente:

```bash
docker run -p 8080:8080 \
  -e DB_SERVER_URL=host.docker.internal \
  -e DB_SERVER_PORT=3306 \
  -e DB_SCHEMA=security \
  -e DB_USER=root \
  -e DB_PWD=root_pwd \
  -e SPRING_PROFILES_ACTIVE=prd \
  lucasbel/security_api:1.0.0
```

#### Diferença entre os profiles

| Comportamento | `default` | `prd` |
|---------------|-----------|-------|
| Cria o banco se não existir (`createDatabaseIfNotExist`) | ✅ Sim | ❌ Não |
| Cria/atualiza tabelas (`spring.jpa.hibernate.ddl-auto`) | `update` | `none` |
| Exibe as queries SQL no log (`spring.jpa.show-sql`) | `true` | `false` |
| Exige banco e tabelas pré-existentes | Não | Sim |
| Uso indicado | Desenvolvimento e testes | Produção |

> ⚠️ Ao usar o profile `prd` sem que o banco e as tabelas existam, a aplicação iniciará mas as requisições aos endpoints falharão. Para o primeiro teste da imagem, use o profile `default`.

A aplicação estará pronta quando o log exibir:

```text
Started Application in X.XXX seconds
```

---

### 5. Acessando o Swagger / OpenAPI

Com a aplicação rodando, acesse a documentação interativa pelo navegador em:

```text
http://localhost:8080/
```

| Recurso | URL | Descrição |
|---------|-----|-----------|
| **Swagger UI** | [http://localhost:8080/](http://localhost:8080/) | Interface gráfica para testar os endpoints |
| **OpenAPI JSON** | [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs) | Especificação OpenAPI em formato JSON |

Pelo Swagger UI é possível testar todos os endpoints diretamente do navegador: basta expandir a operação desejada, clicar em **Try it out**, preencher os dados e clicar em **Execute**.

Para validar rapidamente pelo terminal:

```bash
curl http://localhost:8080/api/v2/firewalls
```

---

### 6. Encerrando os containers

Para parar a aplicação, pressione `Ctrl + C` no terminal onde o `docker run` está sendo executado. Em seguida, remova o container do banco:

```bash
docker stop security_api_mysql
docker rm security_api_mysql
```

---

## 🛠️ Rodando a partir do código-fonte (desenvolvimento local)

Alternativa à imagem do Docker Hub, para quem deseja alterar o código da aplicação.

### 1. Subindo o Banco de Dados

```bash
docker run -d \
  --name security_api_mysql \
  -e MYSQL_ROOT_PASSWORD=root_pwd \
  -e MYSQL_DATABASE=security \
  -p 3306:3306 \
  mysql:8.0
```

### 2. Rodando a API Spring Boot

Com o banco de dados rodando, abra um terminal na raiz do projeto e execute:

```bash
DB_SERVER_URL=localhost ./mvnw spring-boot:run
```

No Windows (PowerShell):

```powershell
$env:DB_SERVER_URL="localhost"; .\mvnw.cmd spring-boot:run
```

> A variável `DB_SERVER_URL` é necessária porque o valor padrão (`host.docker.internal`) só é resolvido de dentro de um container.

A aplicação subirá em `http://localhost:8080` e as tabelas `firewalls` e `vulnerabilidades` serão criadas automaticamente pelo Hibernate (`spring.jpa.hibernate.ddl-auto=update`).

### 3. Construindo a imagem Docker localmente

O projeto inclui um `Dockerfile` com build multi-estágio (Maven + Eclipse Temurin 17):

```bash
docker build -t security_api:local .
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

## 👨‍💻 Autor

Lucas Almeida Bel Correa - RM: 558539

Projeto desenvolvido para a disciplina de Microsservices — **FIAP - 3°SIR**.

---
