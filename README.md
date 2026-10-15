# AutoEscola 3ESPH

API ReST desenvolvida com Spring Boot para o gerenciamento de alunos, instrutores e agendamento de instruções de uma autoescola.

## Integrantes

| Nome | RM |
| --- | --- |
| Pedro Gaspar Fernandes Ferrari | 554887 |
| Enrico Ricarte Rodrigues | 558571 |
| Victor Freire | 556191 |

## Tecnologias

- Java 25
- Spring Boot 4.0.5
- Spring Web MVC, Data JPA, Security, Validation
- Flyway (migrations)
- MySQL
- JWT (com.auth0 java-jwt)
- Springdoc OpenAPI (Swagger)
- Lombok

## Arquitetura

O projeto segue a arquitetura hexagonal (ports & adapters):

- `application/core/domain` — entidades de domínio puras
- `application/port/in` e `application/port/out` — portas de entrada e saída
- `application/service` — regras de negócio / casos de uso
- `adapter/in` — controllers e DTOs de entrada/saída
- `adapter/out` — persistência (JPA) e integração com APIs externas
- `config` — segurança, documentação e configuração de integrações

## Integração com API externa

O endereço dos alunos e instrutores é enriquecido automaticamente a partir do CEP informado, consultando o WebService público **ViaCEP** (`https://viacep.com.br/ws/{cep}/json/`). Quando o CEP não é encontrado, os campos de endereço podem ser informados manualmente. Também há um endpoint dedicado de consulta de CEP: `GET /cep/{cep}`.

## Como executar

Pré-requisitos:

- Java 17 ou superior (projeto configurado para Java 25)
- MySQL rodando em `localhost:3306`
- Banco de dados `autoescola3esph` criado

Credenciais de conexão (padrão no `application.properties`):

```
spring.datasource.url=jdbc:mysql://localhost:3306/autoescola3esph
spring.datasource.username=root
spring.datasource.password=fiap
```

Subir a aplicação (usa o Maven Wrapper):

```
./mvnw spring-boot:run
```

Ou no Windows:

```
mvnw.cmd spring-boot:run
```

A aplicação sobe na porta `8085`. O Flyway cria automaticamente as tabelas e o usuário administrador inicial.

## Documentação (Swagger)

Com a aplicação em execução, a documentação interativa está disponível em:

- Swagger UI: `http://localhost:8085/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8085/v3/api-docs`

O arquivo `SwaggerOffLine.yaml` na raiz contém a especificação OpenAPI exportada.

## Como testar

Usuário administrador inicial:

```
login: admin
senha: admin
```

1. `POST /login` com `{"login": "admin", "senha": "admin"}` retorna um `tokenJWT`.
2. Use o header `Authorization: Bearer <tokenJWT>` em todas as demais requisições.
3. Usuários `ADMIN` podem gerenciar usuários, instrutores e alunos.
4. Usuários `USER` podem listar instrutores e alunos.

### Endpoints

| Método | Rota | Permissão |
| --- | --- | --- |
| POST | `/login` | público |
| PUT | `/login/senha` | ADMIN / USER |
| GET/POST/PUT/DELETE | `/usuarios` e `/usuarios/{id}` | ADMIN |
| GET/POST/PUT/DELETE | `/instrutores` e `/instrutores/{id}` | GET: ADMIN/USER, demais: ADMIN |
| GET/POST/PUT/DELETE | `/alunos` e `/alunos/{id}` | GET: ADMIN/USER, demais: ADMIN |
| GET | `/cep/{cep}` | autenticado |
| POST | `/instrucoes` | autenticado |
| POST | `/instrucoes/cancelar` | autenticado |
| GET | `/health-check` | autenticado |

### Regras de negócio

- Horário de funcionamento: segunda a sábado, das 06:00 às 21:00.
- Instruções têm duração fixa de 1 hora (agendadas em horas inteiras).
- Antecedência mínima de 30 minutos para agendar.
- No máximo 2 instruções por dia para o mesmo aluno.
- Instrutores/alunos inativos não podem ser usados em agendamentos.
- Não permitido agendar instrução com instrutor já ocupado na mesma data/hora.
- A escolha do instrutor é opcional; quando omitida, o sistema escolhe um instrutor disponível de forma aleatória.
- Cancelamento exige motivo (`ALUNO_DESISTIU`, `INSTRUTOR_CANCELOU`, `OUTROS`) e antecedência mínima de 24 horas.

## Testes automatizados

Os testes de cada camada podem ser executados com:

```
./mvnw test
```

Ou no Windows:

```
mvnw.cmd test
```

Os testes de persistência utilizam o perfil `test` e o banco `autoescola3esph_test` (o Flyway cria o schema automaticamente).

## Estrutura do projeto

```
src/main/java/br/com/fiap3esph/autoescola3esph
├── adapter
│   ├── in/controller
│   └── out
│       ├── integration        # ViaCEP
│       └── repository         # JPA + mappers
├── application
│   ├── core/domain
│   ├── core/specification     # validadores de agendamento
│   ├── port/in
│   ├── port/out
│   └── service
├── config
│   ├── documentation          # Swagger/OpenAPI
│   ├── integration            # RestClient
│   └── security               # JWT / CORS
├── exception
└── shared
```
