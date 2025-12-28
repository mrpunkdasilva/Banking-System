# Visão Geral do Projeto

## Ferramentas

### Ambiente de Desenvolvimento
- **IDE**: IntelliJ IDEA / Eclipse
- **Controle de Versão**: Git
- **Repositório**: GitLab
- **Documentação**: Writerside

### Infraestrutura
- **Containerização**: Docker
- **Banco de Dados**: MySQL
- **CI/CD**: GitLab CI

## Organização do Projeto

### Estrutura de Diretórios
```plaintext
src/
├── main/
│   ├── java/
│   │   └── org/jala/university/
│   │       ├── application/     # Casos de uso
│   │       ├── domain/         # Entidades e regras
│   │       ├── infrastructure/ # Implementações
│   │       └── presentation/   # UI e controllers
│   └── resources/
│       ├── META-INF/
│       │   └── persistence.xml
│       └── fxml/               # Layouts JavaFX
└── test/
    └── java/                  # Testes unitários
```

## Cronograma

O projeto segue um cronograma ágil com sprints de duas semanas, incluindo:

- Sprint Planning
- Daily Standups
- Sprint Review
- Sprint Retrospective

## Métricas de Acompanhamento

- Velocidade da equipe
- Cobertura de testes
- Bugs por funcionalidade
- Tempo médio de resolução de problemas