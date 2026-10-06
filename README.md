# Security API

API REST desenvolvida em **Java com Spring Boot** para gerenciamento de **Firewalls** e **Vulnerabilidades**, utilizando **Microsoft SQL Server** como banco de dados.

Projeto desenvolvido para a disciplina de **Microservices and Web Engineering** da FIAP.

---

## Tecnologias

- Java 17
- Spring Boot 4.0.3
- Spring Web MVC
- Spring Data JPA
- Microsoft SQL Server
- Microsoft JDBC Driver for SQL Server
- Docker
- Maven
- Swagger / OpenAPI
- Lombok
- ModelMapper

---

## Arquitetura

A aplicação segue uma arquitetura em camadas:

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
SQL Server
```

### Responsabilidades

| Camada | Responsabilidade |
|---|---|
| `controller` | Disponibiliza os endpoints REST |
| `dto` | Define objetos de entrada, saída e mapeamento |
| `service` | Centraliza a lógica da aplicação |
| `repository` | Realiza a persistência utilizando Spring Data JPA |
| `model` | Representa as entidades persistidas no banco |

---

## Estrutura do Projeto

```text
security_api/
├── src/
│   ├── main/
│   │   ├── java/br/com/fiap/security_api/
│   │   │   ├── Application.java
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── FirewallController.java
│   │   │   │   └── VulnerabilidadeController.java
│   │   │   │
│   │   │   ├── dto/
│   │   │   │   ├── FirewallCreateRequest.java
│   │   │   │   ├── FirewallUpdateRequest.java
│   │   │   │   ├── FirewallResponse.java
│   │   │   │   ├── FirewallMapper.java
│   │   │   │   ├── VulnerabilidadeCreateRequest.java
│   │   │   │   ├── VulnerabilidadeUpdateRequest.java
│   │   │   │   ├── VulnerabilidadeResponse.java
│   │   │   │   └── VulnerabilidadeMapper.java
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── Firewall.java
│   │   │   │   └── Vulnerabilidade.java
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── FirewallRepository.java
│   │   │   │   └── VulnerabilidadeRepository.java
│   │   │   │
│   │   │   └── service/
│   │   │       ├── FirewallService.java
│   │   │       └── VulnerabilidadeService.java
│   │   │
│   │   └── resources/
│   │       ├── application-dev.properties
│   │       ├── application-prd.properties
│   │       │
│   │       └── mysql/
│   │           ├── application.properties
│   │           ├── application-dev.properties
│   │           └── application-prd.properties
│   │
│   └── test/
│       └── java/
│
├── Dockerfile
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

As configurações utilizadas atualmente pela aplicação estão em:

```text
application-dev.properties
application-prd.properties
```

Os arquivos presentes na pasta:

```text
resources/mysql/
```

contêm as configurações antigas utilizadas com **MySQL** e foram mantidos apenas como referência.

Por estarem em uma subpasta, esses arquivos não são carregados automaticamente durante a execução normal do Spring Boot.

Para os testes locais deste projeto é utilizado o profile:

```text
dev
```

---

# Executando o projeto localmente

## 1. Subir o SQL Server com Docker

Execute o seguinte comando:

```bash
docker run -d \
  --name sqlserver \
  --rm \
  -e MSSQL_SA_PASSWORD=1q2w3e4R@ \
  -e "ACCEPT_EULA=Y" \
  -p 1433:1433 \
  mcr.microsoft.com/mssql/server:latest
```

O SQL Server será disponibilizado localmente com as seguintes configurações:

```text
Host: localhost
Porta: 1433
Usuário: sa
Senha: 1q2w3e4R@
```

> As credenciais acima são utilizadas exclusivamente no ambiente local de desenvolvimento.

Para verificar se o container está em execução:

```bash
docker ps
```

O container deverá aparecer como:

```text
sqlserver
```

com o mapeamento:

```text
1433:1433
```

---

## 2. Configurar o DBeaver

Crie uma nova conexão utilizando o driver:

```text
SQL Server
```

Configure inicialmente:

```text
Host: localhost
Porta: 1433
Database: master
Authentication: SQL Server Authentication
Usuário: sa
Senha: 1q2w3e4R@
```

Teste a conexão.

Após conectar, crie o banco utilizado pela aplicação:

```sql
CREATE DATABASE api;
```

Depois, altere a conexão do DBeaver para utilizar:

```text
Database: api
```

Configuração final:

```text
Host: localhost
Porta: 1433
Database: api
Usuário: sa
Senha: 1q2w3e4R@
```

---

## 3. Configuração do SQL Server no Spring Boot

O profile `dev` utiliza o arquivo:

```text
src/main/resources/application-dev.properties
```

A conexão é configurada através das seguintes propriedades:

```properties
spring.datasource.url=jdbc:sqlserver://${DB_SERVER_URL}:${DB_SERVER_PORT};databaseName=${DB_SCHEMA};encrypt=false;trustServerCertificate=true
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PWD}
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.SQLServerDialect
```

O Hibernate utiliza:

```properties
spring.jpa.hibernate.ddl-auto=update
```

permitindo criar e atualizar automaticamente as tabelas correspondentes às entidades da aplicação.

---

## 4. Configurar as variáveis de ambiente

No **PowerShell**, execute:

```powershell
$env:DB_SERVER_URL="localhost"
$env:DB_SERVER_PORT="1433"
$env:DB_SCHEMA="api"
$env:DB_USER="sa"
$env:DB_PWD="1q2w3e4R@"
$env:SPRING_PROFILES_ACTIVE="dev"
```

Essas variáveis correspondem às mesmas credenciais utilizadas ao criar o container SQL Server.

Para conferir:

```powershell
echo $env:DB_SERVER_URL
echo $env:DB_SERVER_PORT
echo $env:DB_SCHEMA
echo $env:DB_USER
echo $env:SPRING_PROFILES_ACTIVE
```

Resultado esperado:

```text
localhost
1433
api
sa
dev
```

---

## 5. Executar a aplicação

Na raiz do projeto, execute:

```powershell
.\mvnw.cmd spring-boot:run
```

O Spring Boot deverá utilizar o profile:

```text
dev
```

Durante a inicialização será possível visualizar:

```text
The following 1 profile is active: "dev"
```

e, após conectar corretamente ao SQL Server:

```text
HikariPool-1 - Start completed
```

A aplicação estará disponível na porta:

```text
8080
```

---

# Swagger

A documentação interativa da API pode ser acessada em:

```text
http://localhost:8080/
```

O profile `dev` utiliza:

```properties
api.version=v1
```

Portanto, os endpoints estão disponíveis em:

```text
/api/v1
```

---

# Endpoints

## Firewalls

| Método | Endpoint | Operação |
|---|---|---|
| POST | `/api/v1/firewalls` | Criar firewall |
| GET | `/api/v1/firewalls` | Listar todos |
| GET | `/api/v1/firewalls/{id}` | Buscar por ID |
| PUT | `/api/v1/firewalls/{id}` | Atualizar |
| DELETE | `/api/v1/firewalls/{id}` | Excluir |

### Exemplo de criação

```json
{
  "nome": "FW-Core-01",
  "cluster": "Cluster-SP",
  "numBlades": 4,
  "vendor": "Check Point"
}
```

O campo `id` é gerado automaticamente.

---

## Vulnerabilidades

| Método | Endpoint | Operação |
|---|---|---|
| POST | `/api/v1/vulnerabilidades` | Criar vulnerabilidade |
| GET | `/api/v1/vulnerabilidades` | Listar todas |
| GET | `/api/v1/vulnerabilidades/{cve}` | Buscar por CVE |
| PUT | `/api/v1/vulnerabilidades/{cve}` | Atualizar |
| DELETE | `/api/v1/vulnerabilidades/{cve}` | Excluir |

### Exemplo de criação

```json
{
  "titulo": "Remote Code Execution",
  "severidade": 9.8,
  "versao": 3.1,
  "qtdAtivosAfetados": 12
}
```

O campo `cve` é gerado automaticamente.

---

# Validação do CRUD

O CRUD pode ser validado utilizando o **Swagger** para realizar as requisições e o **DBeaver** para confirmar a persistência dos dados no SQL Server.

## CREATE

Realize um POST pelo Swagger:

```text
POST /api/v1/firewalls
```

ou:

```text
POST /api/v1/vulnerabilidades
```

Depois consulte o banco pelo DBeaver:

```sql
SELECT * FROM firewalls;
```

```sql
SELECT * FROM vulnerabilidades;
```

O registro criado pela API deverá estar presente no SQL Server.

---

## READ

Para listar os registros:

```text
GET /api/v1/firewalls
GET /api/v1/vulnerabilidades
```

Para consultar um registro específico:

```text
GET /api/v1/firewalls/{id}
GET /api/v1/vulnerabilidades/{cve}
```

---

## UPDATE

Realize uma atualização:

```text
PUT /api/v1/firewalls/{id}
```

ou:

```text
PUT /api/v1/vulnerabilidades/{cve}
```

Depois valide pelo DBeaver:

```sql
SELECT * FROM firewalls;
```

```sql
SELECT * FROM vulnerabilidades;
```

O registro deverá apresentar os valores atualizados.

---

## DELETE

Realize uma exclusão:

```text
DELETE /api/v1/firewalls/{id}
```

ou:

```text
DELETE /api/v1/vulnerabilidades/{cve}
```

Depois consulte novamente:

```sql
SELECT * FROM firewalls;
```

```sql
SELECT * FROM vulnerabilidades;
```

O registro excluído não deverá mais estar presente.

---

# Fluxo da aplicação

```text
Swagger
   ↓
API REST - Spring Boot
   ↓
Controller
   ↓
DTO / Mapper
   ↓
Service
   ↓
Repository
   ↓
Spring Data JPA
   ↓
Microsoft JDBC Driver
   ↓
SQL Server
localhost:1433
   ↓
Database api
   ↓
DBeaver
```

As operações realizadas através da API são persistidas diretamente no banco **SQL Server**, podendo ser verificadas pelo DBeaver.

---

# Persistência

A aplicação utiliza **Spring Data JPA** através dos seguintes repositories:

```text
FirewallRepository
VulnerabilidadeRepository
```

As entidades correspondentes às tabelas utilizadas são:

```text
Firewall
Vulnerabilidade
```

Mapeadas respectivamente para:

```text
firewalls
vulnerabilidades
```

Os identificadores são gerados automaticamente utilizando:

```java
@GeneratedValue(strategy = GenerationType.AUTO)
```

---

# Encerrando o ambiente

Para encerrar o SQL Server:

```bash
docker stop sqlserver
```

Como o container foi criado utilizando:

```text
--rm
```

ele será removido automaticamente após ser encerrado.

Os dados desse container também não são persistidos após sua remoção, pois não foi configurado um volume Docker para o SQL Server.

---

# Autores

**Lucas Almeida Bel Correa**  
RM 558539

**Guilherme Tusita**  
RM 554511

FIAP — Sistemas de Informação  
Microservices and Web Engineering
