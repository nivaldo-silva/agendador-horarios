# 📅 Agendador de Horários

API REST para gerenciamento de agendamentos, desenvolvida com **Spring Boot**. Permite criar, buscar, atualizar e cancelar agendamentos com validação de conflitos de horário.

---

## 🚀 Tecnologias

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- Lombok
- SpringDoc OpenAPI (Swagger)
- Bean Validation

---

## 📁 Estrutura do Projeto

```
src/
├── controller/        # Camada de entrada HTTP
├── service/           # Regras de negócio
├── repository/        # Acesso ao banco de dados
├── entity/            # Entidades JPA
│   └── enums/         # Enum de status
├── dto/               # Objetos de transferência de dados
├── mapper/            # Conversão entre entidade e DTO
├── exception/         # Exceções customizadas e handler global
└── docs/              # Interfaces de documentação Swagger
```

---

## ⚙️ Como executar

**Pré-requisitos:** Java 21+ e Maven instalados.

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/agendador-horarios.git
cd agendador-horarios

# Execute a aplicação
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

A documentação interativa (Swagger UI) pode ser acessada em:
```
http://localhost:8080/swagger-ui.html
```

---

## 📌 Endpoints

### Criar agendamento
```http
POST /agendamentos
```
```json
{
  "nome_cliente": "João Silva",
  "telefone_cliente": "81999998888",
  "email_cliente": "joao@email.com",
  "profissional": "Dr. Carlos",
  "servico": "Consulta",
  "duracao_minutos": 30,
  "observacao": "Primeira consulta",
  "data_hora_agendada": "15/06/2025 10:00:00"
}
```

---

### Buscar agendamentos
```http
GET /agendamentos
```

Filtros disponíveis via query params (opcionais):

| Parâmetro | Tipo     | Descrição                        |
|-----------|----------|----------------------------------|
| `codigo`  | `String` | Código único do agendamento      |
| `nome`    | `String` | Busca parcial pelo nome do cliente |
| `data`    | `String` | Data no formato `dd/MM/yyyy`     |

Prioridade dos filtros: `codigo` > `data` > `nome` > todos.

---

### Atualizar agendamento
```http
PATCH /agendamentos/{codigo}
```
Atualiza parcialmente um agendamento. Apenas os campos enviados serão alterados.

---

### Cancelar agendamento
```http
PATCH /agendamentos/{codigo}/cancelar
```

---

### Atualizar status
```http
PATCH /agendamentos/{codigo}/status?novoStatus=CONCLUIDO
```

Status disponíveis: `AGENDADO`, `CONCLUIDO`, `CANCELADO`, `NAO_COMPARECEU`

---

## 🔄 Status do Agendamento

| Status          | Descrição                        |
|-----------------|----------------------------------|
| `AGENDADO`      | Agendamento ativo (padrão)       |
| `CONCLUIDO`     | Serviço realizado                |
| `CANCELADO`     | Agendamento cancelado            |
| `NAO_COMPARECEU`| Cliente não compareceu           |

---

## 🛡️ Tratamento de Erros

A API retorna erros no formato **Problem Detail** (RFC 9457):

```json
{
  "type": "https://api.agendador-horarios.io/problems/horario-indisponivel",
  "title": "Horário Indisponível",
  "status": 409,
  "detail": "O horário solicitado já está ocupado.",
  "timestamp": "2025-06-15T10:00:00Z"
}
```

| Status HTTP | Situação                                  |
|-------------|-------------------------------------------|
| `400`       | Dados inválidos ou formato incorreto      |
| `404`       | Agendamento não encontrado                |
| `409`       | Conflito de horário                       |
| `422`       | Agendamento já cancelado                  |
| `500`       | Erro interno inesperado                   |

---

## 📝 Formato de Data

Todas as datas devem seguir o padrão:

```
dd/MM/yyyy HH:mm:ss
Exemplo: 15/06/2025 10:00:00
```

---

## 🧠 Decisões de Projeto

- **Código de agendamento:** gerado automaticamente via UUID (8 caracteres, maiúsculos), dispensando o uso do ID interno.
- **Validação de conflito:** ao criar ou reagendar, a API verifica se já existe um agendamento no mesmo horário para o mesmo serviço.
- **PATCH parcial:** o endpoint de atualização só altera os campos enviados, ignorando os nulos.
- **Separação de responsabilidades:** a documentação Swagger fica em interfaces separadas (`AgendamentoDocs`), mantendo o controller limpo.

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Abra uma issue ou envie um pull request.

---

