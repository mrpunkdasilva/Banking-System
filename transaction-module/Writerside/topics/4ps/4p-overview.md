# Os 4P's do Módulo de Transações

Este documento apresenta uma visão geral do módulo de transações organizada segundo o framework dos 4P's: Product, Process, People e Project.

## Diagrama Conceitual

```mermaid
mindmap
  root((4P's do Módulo))
    Product
      Especificações
        Descrição do problema
          Módulo específico para garantir segurança, consistência e confiabilidade em transações
      Diagrama de casos de uso
        Realizar transação via QR Code
        Realizar transação via NFC
        Consultar histórico de transações
        Gerenciar beneficiários
        Agendar transferência
        Validar dados do beneficiário
        Realizar transação entre contas
        Autenticar com senha
        MFA para alto valor
      Tecnologias
        Java
        JFX
        Spring Boot
        MySQL
        Docker
      Alcance
        Experiência segura e prática com QR Code e NFC
        Funcionalidades de autenticação, gestão e histórico
      Funcionalidades Principais
        Transações via QR Code e NFC
        Gestão de Beneficiários
        Agendamento de Transferências
        Histórico de Transações
        Segurança Avançada
          Senha para transferências
          MFA para alto valor
          Validação de beneficiários
        Operações em Contas
      Limitações
        Sem suporte a investimento ou moedas estrangeiras
        MFA apenas acima de valor definido
    Process
      Boas práticas
        Revisão de código
        Integração contínua
        Modelagem de branchs
        Testes unitários
        Documentação
      Metodologia
        Ágil
        Colaboração
        Transparência
        Retroalimentação
        Melhoria contínua
        Adaptabilidade
    People
      Stakeholders
        Professor: José Paulo Rodrigues
      PO / Arquiteto / Líder
        Tutor: Karem Huacota Saavedra
      Equipe de Desenvolvimento (Grupo 4)
        Rinaldo Lira
        Gustavo Jesus
        Matheus Henrique
        David Souza
        Gabriel Ramos
    Project
      Ferramentas
        IDE
        SCM
          Git
        Wiki
```

## Visão Integrada

O módulo de transações é um componente crítico do sistema bancário, desenvolvido com foco em segurança e usabilidade. A integração dos 4P's garante uma abordagem holística:

- **Product**: Define o que será construído, com foco nas necessidades do usuário
- **Process**: Estabelece como o desenvolvimento será conduzido
- **People**: Identifica quem está envolvido e suas responsabilidades
- **Project**: Organiza os recursos e ferramentas necessários

Para mais detalhes sobre cada aspecto, consulte as seções específicas:

- [Visão Geral do Produto](product-overview.md)
- [Processo de Desenvolvimento](process-overview.md)
- [Equipe e Stakeholders](people-overview.md)
- [Visão Geral do Projeto](project-overview.md)