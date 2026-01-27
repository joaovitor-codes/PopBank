# PopBank API

Bem-vindo ao **PopBank**, uma API RESTful robusta para simulação de operações bancárias digitais. Este projeto foi desenvolvido para gerenciar contas, transações financeiras e carteiras digitais com segurança e eficiência, utilizando as melhores práticas do ecossistema Spring.

## 📋 Índice

- [Funcionalidades Principais](#-funcionalidades-principais)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Configuração](#-instalação-e-configuração)
- [Como Usar](#-como-usar)
- [Estrutura do Projeto](#-estrutura-do-projeto)

## 🚀 Funcionalidades Principais

O PopBank oferece um conjunto completo de recursos para administração bancária:

*   **Gestão de Usuários e Autenticação:**
    *   Registro e Login seguros com JWT (JSON Web Tokens).
    *   Controle de Acesso Baseado em Funções (RBAC) para Administradores (`ADMIN`) e Usuários comuns.
    *   Fluxo unificado de cadastro que cria automaticamente Usuário, Conta e Carteira.
*   **Gestão de Contas:**
    *   Administração completa de contas bancárias.
    *   Associação de dados pessoais (CPF, Data de Nascimento, Filiação).
*   **Carteira Digital (Wallet):**
    *   Gestão de saldos.
    *   Suporte a múltiplas contas poupança por usuário.
    *   Movimentação de fundos entre saldo principal e poupança.
*   **Transações Financeiras:**
    *   Transferências entre contas (P2P).
    *   Validação rigorosa de saldo e regras de negócio.
    *   Histórico detalhado de transações.
    *   Funcionalidade de **Estorno (Undo)** para administradores.
    *   Possibilidade de deletar transações (Admin).
*   **Documentação Interativa:**
    *   Integração nativa com Swagger UI (OpenAPI).

## 🛠 Tecnologias Utilizadas

Este projeto foi construído com uma stack moderna e performática:

*   **Linguagem:** [Java 17](https://www.oracle.com/java/)
*   **Framework:** [Spring Boot 3.5.7](https://spring.io/projects/spring-boot)
*   **Banco de Dados:** [PostgreSQL](https://www.postgresql.org/) (Produção/Dev) e H2 (Testes)
*   **Segurança:** Spring Security + Java JWT (Auth0 v4.5.0)
*   **Persistência:** Spring Data JPA / Hibernate
*   **Documentação:** SpringDoc OpenAPI (Swagger UI)
*   **Ferramentas de Build:** Maven
*   **Utilitários:** Lombok, Validation API

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado em seu ambiente:

*   **Java JDK 17** ou superior.
*   **PostgreSQL** (Configurado para rodar na porta `5433` ou ajustável).
*   **Maven** (Opcional, pois o wrapper `./mvnw` está incluído no projeto).
*   **Git**.

## 🔧 Instalação e Configuração

Siga os passos abaixo para configurar o ambiente de desenvolvimento:

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/seu-usuario/popBank.git
    cd popBank
    ```

2.  **Configuração do Banco de Dados:**
    Certifique-se de que o serviço do PostgreSQL esteja rodando e crie um banco de dados chamado `db_popbank`.
    ```sql
    CREATE DATABASE db_popbank;
    ```

3.  **Configuração da Aplicação:**
    Abra o arquivo `src/main/resources/application.properties`. As configurações padrão são:
    
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5433/db_popbank
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    
    # IMPORTANTE: Para a primeira execução, altere para 'update' para criar as tabelas automaticamente
    spring.jpa.hibernate.ddl-auto=update
    ```
    > **Atenção:** Ajuste o `username` e `password` conforme sua instalação local do PostgreSQL.

4.  **Variáveis de Ambiente (Opcional):**
    Para maior segurança, você pode definir a chave secreta do token JWT via variável de ambiente:
    *   `JWT_SECRET`: Sua string secreta para assinatura de tokens.

5.  **Instale as dependências:**
    ```bash
    ./mvnw clean install
    ```

## ▶️ Como Usar

### Executando a Aplicação

Para iniciar o servidor Spring Boot, execute o comando na raiz do projeto:

```bash
./mvnw spring-boot:run
```

A aplicação iniciará e estará disponível em `http://localhost:8080`.

### Documentação da API (Swagger)

A maneira mais fácil de explorar e testar os endpoints é através do Swagger UI. Com a aplicação rodando, acesse em seu navegador:

👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

### Exemplos de Uso (cURL)

**1. Registrar uma nova conta (Público):**
Este endpoint cria o usuário, a conta e a carteira automaticamente.

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "login": "cliente@popbank.com",
    "password": "senhaSegura123",
    "nome": "João da Silva",
    "cpf": "123.456.789-00",
    "dataNascimento": "1990-05-20",
    "nomeDaMae": "Maria da Silva"
  }'
```

**2. Realizar Login (Obter Token):**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "login": "cliente@popbank.com",
    "password": "senhaSegura123"
  }'
```

**3. Criar uma Transação (Requer Token):**

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "senderId": "uuid-do-remetente",
    "receiverId": "uuid-do-destinatario",
    "amount": 100.50,
    "transactionType": "TRANSFER"
  }'
```

## 📂 Estrutura do Projeto

A organização de pastas segue a arquitetura em camadas padrão do Spring Boot:

```
popBank/
├── src/main/java/com/dev/popbank/
│   ├── config/          # Configurações (Security, Swagger, Beans)
│   ├── controller/      # Camada de API (REST Controllers)
│   ├── service/         # Camada de Regra de Negócio
│   ├── repository/      # Camada de Acesso a Dados (JPA)
│   ├── model/           # Entidades JPA
│   ├── dtos/            # Data Transfer Objects (Requests/Responses)
│   ├── mapper/          # Conversores (MapStruct/Manual)
│   └── PopBankApplication.java # Classe Main
└── src/main/resources/
    └── application.properties # Configurações da aplicação e DB
```

---
