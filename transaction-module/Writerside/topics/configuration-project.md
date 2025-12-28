# Configuração do Projeto

Este guia detalha os passos necessários para configurar o ambiente de desenvolvimento do módulo de transações.

## Requisitos

- Java Development Kit (JDK) 17 ou superior
- Maven 3.8+
- Docker e Docker Compose
- IDE compatível com JavaFX (recomendado: IntelliJ IDEA)

## Estrutura do Projeto

```plaintext
./
├── docker/
│   └── docker-compose.yml
├── src/
│   └── main/
│       ├── java/
│       └── resources/
├── pom.xml
└── .env
```

## Primeiros Passos

1. Clone o repositório
2. Configure as variáveis de ambiente
3. Inicie o banco de dados com Docker
4. Execute o build com Maven