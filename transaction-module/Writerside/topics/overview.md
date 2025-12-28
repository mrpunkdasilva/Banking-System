# Visão Geral do Módulo de Transações

O Módulo de Transações é um componente central do nosso sistema bancário, responsável por gerenciar todas as transações bancárias. Esta documentação fornece informações abrangentes sobre a arquitetura, configuração e uso do módulo.

## Funcionalidades Principais

- Transferências entre contas
- Depósitos e saques
- Histórico de transações
- Extrato bancário
- Gerenciamento de beneficiários

## Stack Tecnológica

- Java 17
- JavaFX (Interface gráfica)
- JPA (Persistência)
- MySQL (Banco de dados)
- Maven (Gerenciamento de dependências)
- Docker (Containerização)
- Checkstyle (Análise de qualidade de código)

## Estrutura do Módulo

O projeto segue os princípios da Clean Architecture com as seguintes camadas:

```plaintext
src/
├── main/
│   ├── java/
│   │   └── org/jala/university/
│   │       ├── application/     # Casos de uso e regras de aplicação
│   │       ├── domain/         # Regras de negócio e entidades
│   │       ├── infrastructure/ # Implementações técnicas
│   │       └── presentation/   # Interface do usuário e controladores
```

## Começando

Consulte o guia [Primeiros Passos](getting-started.md) para instruções de configuração.