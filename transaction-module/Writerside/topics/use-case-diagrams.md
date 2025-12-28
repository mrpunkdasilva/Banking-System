# Diagramas de Casos de Uso

## Visão Geral

Os diagramas de casos de uso representam as interações entre os usuários (atores) e o sistema. Eles são utilizados para capturar os requisitos funcionais do sistema e ilustrar como os diferentes atores interagem com as funcionalidades disponíveis.

## Atores Principais

- **Cliente**: Usuário final que utiliza o sistema para realizar transações financeiras
- **Autenticador Externo**: Sistema externo responsável pela autenticação multifatorial
- **Sistema Bancário**: Sistema externo que processa as transações financeiras

## Diagrama Geral

O diagrama abaixo apresenta uma visão geral de todos os casos de uso do sistema e suas relações:

```mermaid
graph TD
    %% Atores
    Cliente((Cliente))
    Autenticador((Autenticador<br>Externo))
    SistemaBancario((Sistema<br>Bancário))
    
    %% Casos de Uso
    UC1[Realizar Transação<br>com QRCode]
    UC2[Realizar Transação<br>com NFC]
    UC3[Visualizar Histórico<br>de Transações]
    UC4[Gerenciar<br>Beneficiários]
    UC5[Realizar Transações<br>entre Contas]
    UC6[Agendar<br>Transações]
    UC7[Realizar Transação<br>com MFA]
    
    %% Relacionamentos
    Cliente --- UC1
    Cliente --- UC2
    Cliente --- UC3
    Cliente --- UC4
    Cliente --- UC5
    Cliente --- UC6
    Cliente --- UC7
    Autenticador --- UC7
    SistemaBancario --- UC1
    SistemaBancario --- UC2
    SistemaBancario --- UC5
    SistemaBancario --- UC7
    
    %% Inclusões e extensões
    UC5 -.-> |<<include>>| UC4
    UC6 -.-> |<<include>>| UC5
    UC7 -.-> |<<extend>>| UC5
    
    %% Estilo
    classDef actor fill:#a2d2ff,stroke:#333,stroke-width:2px
    classDef usecase fill:#caffbf,stroke:#333,stroke-width:1px
    
    class Cliente,Autenticador,SistemaBancario actor
    class UC1,UC2,UC3,UC4,UC5,UC6,UC7 usecase
```

## Casos de Uso Detalhados

### 1. Realizar Transação com QRCode (RN-01)

**Ator Principal**: Cliente

**Fluxo Principal**:
- Seleciona a opção de transação com QRCode
- Digitaliza o QRCode
- Valida o QRCode
- Exige senha
- Insere senha
- Valida a senha
- Processa a transação
- Recebe confirmação

**Diagrama**:

```mermaid
graph TD
    %% Atores
    Cliente((Cliente))
    SistemaBancario((Sistema<br>Bancário))
    
    %% Caso de Uso Principal
    UC1[Realizar Transação<br>com QRCode]
    
    %% Casos de Uso Incluídos
    UC1_1[Digitalizar QRCode]
    UC1_2[Validar QRCode]
    UC1_3[Autenticar com Senha]
    UC1_4[Processar Transação]
    UC1_5[Confirmar Transação]
    
    %% Relacionamentos
    Cliente --- UC1
    SistemaBancario --- UC1_4
    
    %% Inclusões
    UC1 -.-> |<<include>>| UC1_1
    UC1 -.-> |<<include>>| UC1_2
    UC1 -.-> |<<include>>| UC1_3
    UC1 -.-> |<<include>>| UC1_4
    UC1 -.-> |<<include>>| UC1_5
    
    %% Estilo
    classDef actor fill:#a2d2ff,stroke:#333,stroke-width:2px
    classDef usecase fill:#caffbf,stroke:#333,stroke-width:1px
    classDef included fill:#fdffb6,stroke:#333,stroke-width:1px
    
    class Cliente,SistemaBancario actor
    class UC1 usecase
    class UC1_1,UC1_2,UC1_3,UC1_4,UC1_5 included
```

### 2. Realizar Transação com NFC (RN-02)

**Ator Principal**: Cliente

**Fluxo Principal**:
- Seleciona opção de transação NFC
- Estabelece conexão NFC
- Autentica o cliente
- Processa a transação
- Fornece confirmação

**Diagrama**:

```mermaid
graph TD
    %% Atores
    Cliente((Cliente))
    SistemaBancario((Sistema<br>Bancário))
    
    %% Caso de Uso Principal
    UC2[Realizar Transação<br>com NFC]
    
    %% Casos de Uso Incluídos
    UC2_1[Estabelecer Conexão NFC]
    UC2_2[Autenticar Cliente]
    UC2_3[Processar Transação]
    UC2_4[Confirmar Transação]
    
    %% Relacionamentos
    Cliente --- UC2
    SistemaBancario --- UC2_3
    
    %% Inclusões
    UC2 -.-> |<<include>>| UC2_1
    UC2 -.-> |<<include>>| UC2_2
    UC2 -.-> |<<include>>| UC2_3
    UC2 -.-> |<<include>>| UC2_4
    
    %% Estilo
    classDef actor fill:#a2d2ff,stroke:#333,stroke-width:2px
    classDef usecase fill:#caffbf,stroke:#333,stroke-width:1px
    classDef included fill:#fdffb6,stroke:#333,stroke-width:1px
    
    class Cliente,SistemaBancario actor
    class UC2 usecase
    class UC2_1,UC2_2,UC2_3,UC2_4 included
```

### 3. Histórico de Transações (RN-03)

**Ator Principal**: Cliente

**Fluxo Principal**:
- Fornece histórico de transações
- Recupera transações
- Exibe transações
- Filtra ou ordena as transações

**Diagrama**:

```mermaid
graph TD
    %% Atores
    Cliente((Cliente))
    
    %% Caso de Uso Principal
    UC3[Visualizar Histórico<br>de Transações]
    
    %% Casos de Uso Incluídos
    UC3_1[Recuperar Transações]
    UC3_2[Exibir Transações]
    UC3_3[Filtrar Transações]
    UC3_4[Ordenar Transações]
    
    %% Relacionamentos
    Cliente --- UC3
    
    %% Inclusões
    UC3 -.-> |<<include>>| UC3_1
    UC3 -.-> |<<include>>| UC3_2
    
    %% Extensões
    UC3_3 -.-> |<<extend>>| UC3
    UC3_4 -.-> |<<extend>>| UC3
    
    %% Estilo
    classDef actor fill:#a2d2ff,stroke:#333,stroke-width:2px
    classDef usecase fill:#caffbf,stroke:#333,stroke-width:1px
    classDef included fill:#fdffb6,stroke:#333,stroke-width:1px
    classDef extended fill:#ffc6ff,stroke:#333,stroke-width:1px
    
    class Cliente actor
    class UC3 usecase
    class UC3_1,UC3_2 included
    class UC3_3,UC3_4 extended
```

### 4. Gestão de Beneficiários (RN-04)

**Ator Principal**: Cliente

**Fluxo Principal**:
- Adiciona beneficiário
- Valida dados do beneficiário
- Remove beneficiário
- Atualiza beneficiário
- Visualiza beneficiário

**Diagrama**:

```mermaid
graph TD
    %% Atores
    Cliente((Cliente))
    
    %% Caso de Uso Principal
    UC4[Gerenciar<br>Beneficiários]
    
    %% Casos de Uso Incluídos/Estendidos
    UC4_1[Adicionar Beneficiário]
    UC4_2[Remover Beneficiário]
    UC4_3[Atualizar Beneficiário]
    UC4_4[Visualizar Beneficiário]
    UC4_5[Validar Dados<br>do Beneficiário]
    
    %% Relacionamentos
    Cliente --- UC4
    
    %% Extensões
    UC4_1 -.-> |<<extend>>| UC4
    UC4_2 -.-> |<<extend>>| UC4
    UC4_3 -.-> |<<extend>>| UC4
    UC4_4 -.-> |<<extend>>| UC4
    
    %% Inclusões
    UC4_1 -.-> |<<include>>| UC4_5
    UC4_3 -.-> |<<include>>| UC4_5
    
    %% Estilo
    classDef actor fill:#a2d2ff,stroke:#333,stroke-width:2px
    classDef usecase fill:#caffbf,stroke:#333,stroke-width:1px
    classDef extended fill:#ffc6ff,stroke:#333,stroke-width:1px
    classDef included fill:#fdffb6,stroke:#333,stroke-width:1px
    
    class Cliente actor
    class UC4 usecase
    class UC4_1,UC4_2,UC4_3,UC4_4 extended
    class UC4_5 included
```

### 5. Transações entre Contas (RN-05)

**Ator Principal**: Cliente

**Fluxo Principal**:
- Seleciona opção de transação
- Insere dados da transação
- Valida dados da transação
- Exibe senha
- Insere senha
- Valida a senha
- Processa a transação
- Exibe a confirmação

**Diagrama**:

```mermaid
graph TD
    %% Atores
    Cliente((Cliente))
    SistemaBancario((Sistema<br>Bancário))
    
    %% Caso de Uso Principal
    UC5[Realizar Transações<br>entre Contas]
    
    %% Casos de Uso Incluídos
    UC5_1[Inserir Dados<br>da Transação]
    UC5_2[Validar Dados<br>da Transação]
    UC5_3[Autenticar com Senha]
    UC5_4[Processar Transação]
    UC5_5[Confirmar Transação]
    UC4[Gerenciar<br>Beneficiários]
    
    %% Relacionamentos
    Cliente --- UC5
    SistemaBancario --- UC5_4
    
    %% Inclusões
    UC5 -.-> |<<include>>| UC5_1
    UC5 -.-> |<<include>>| UC5_2
    UC5 -.-> |<<include>>| UC5_3
    UC5 -.-> |<<include>>| UC5_4
    UC5 -.-> |<<include>>| UC5_5
    UC5 -.-> |<<include>>| UC4
    
    %% Estilo
    classDef actor fill:#a2d2ff,stroke:#333,stroke-width:2px
    classDef usecase fill:#caffbf,stroke:#333,stroke-width:1px
    classDef included fill:#fdffb6,stroke:#333,stroke-width:1px
    
    class Cliente,SistemaBancario actor
    class UC5 usecase
    class UC5_1,UC5_2,UC5_3,UC5_4,UC5_5,UC4 included
```

### 6. Agendamento de Transação (RN-06)

**Ator Principal**: Cliente

**Fluxo Principal**:
- Agenda transação
- Insere detalhes
- Confirma agendamento
- Atualiza ou remove agendamento
- Exibe agendamento

**Diagrama**:

```mermaid
graph TD
    %% Atores
    Cliente((Cliente))
    
    %% Caso de Uso Principal
    UC6[Agendar<br>Transações]
    
    %% Casos de Uso Incluídos/Estendidos
    UC6_1[Inserir Detalhes<br>do Agendamento]
    UC6_2[Confirmar Agendamento]
    UC6_3[Atualizar Agendamento]
    UC6_4[Remover Agendamento]
    UC6_5[Visualizar Agendamento]
    UC5[Realizar Transações<br>entre Contas]
    
    %% Relacionamentos
    Cliente --- UC6
    
    %% Inclusões
    UC6 -.-> |<<include>>| UC6_1
    UC6 -.-> |<<include>>| UC6_2
    UC6 -.-> |<<include>>| UC5
    
    %% Extensões
    UC6_3 -.-> |<<extend>>| UC6
    UC6_4 -.-> |<<extend>>| UC6
    UC6_5 -.-> |<<extend>>| UC6
    
    %% Estilo
    classDef actor fill:#a2d2ff,stroke:#333,stroke-width:2px
    classDef usecase fill:#caffbf,stroke:#333,stroke-width:1px
    classDef included fill:#fdffb6,stroke:#333,stroke-width:1px
    classDef extended fill:#ffc6ff,stroke:#333,stroke-width:1px
    
    class Cliente actor
    class UC6 usecase
    class UC6_1,UC6_2,UC5 included
    class UC6_3,UC6_4,UC6_5 extended
```

### 7. Transações com MFA (RN-09)

**Atores**: Cliente, Autenticador Externo

**Fluxo Principal**:
- Seleciona a opção de transação com MFA
- Insere dados para transação
- Valida os dados
- Exibe código
- Insere o código
- Valida a senha
- Processa a transação
- Mostra confirmação

**Diagrama**:

```mermaid
graph TD
    %% Atores
    Cliente((Cliente))
    Autenticador((Autenticador<br>Externo))
    SistemaBancario((Sistema<br>Bancário))
    
    %% Caso de Uso Principal
    UC7[Realizar Transação<br>com MFA]
    
    %% Casos de Uso Incluídos
    UC7_1[Inserir Dados<br>da Transação]
    UC7_2[Validar Dados<br>da Transação]
    UC7_3[Gerar Código MFA]
    UC7_4[Validar Código MFA]
    UC7_5[Processar Transação]
    UC7_6[Confirmar Transação]
    UC5[Realizar Transações<br>entre Contas]
    
    %% Relacionamentos
    Cliente --- UC7
    Autenticador --- UC7_3
    Autenticador --- UC7_4
    SistemaBancario --- UC7_5
    
    %% Inclusões e extensões
    UC7 -.-> |<<include>>| UC7_1
    UC7 -.-> |<<include>>| UC7_2
    UC7 -.-> |<<include>>| UC7_3
    UC7 -.-> |<<include>>| UC7_4
    UC7 -.-> |<<include>>| UC7_5
    UC7 -.-> |<<include>>| UC7_6
    UC7 -.-> |<<extend>>| UC5
    
    %% Estilo
    classDef actor fill:#a2d2ff,stroke:#333,stroke-width:2px
    classDef usecase fill:#caffbf,stroke:#333,stroke-width:1px
    classDef included fill:#fdffb6,stroke:#333,stroke-width:1px
    
    class Cliente,Autenticador,SistemaBancario actor
    class UC7 usecase
    class UC7_1,UC7_2,UC7_3,UC7_4,UC7_5,UC7_6 included
```

## Relacionamentos entre Casos de Uso

Alguns casos de uso possuem relacionamentos entre si:

1. **Inclusão (<<include>>)**: Um caso de uso inclui outro caso de uso como parte de seu fluxo
   - "Realizar Transações entre Contas" inclui "Gerenciar Beneficiários"
   - "Agendar Transações" inclui "Realizar Transações entre Contas"

2. **Extensão (<<extend>>)**: Um caso de uso estende outro caso de uso com comportamento adicional
   - "Transações com MFA" estende "Realizar Transações entre Contas" para valores elevados
   - "Filtrar Transações" estende "Visualizar Histórico de Transações"

## Requisitos Não-Funcionais

Além dos casos de uso funcionais, o sistema também deve atender a requisitos não-funcionais:

1. **Segurança**: Todas as transações devem ser criptografadas
2. **Desempenho**: Transações devem ser processadas em menos de 3 segundos
3. **Disponibilidade**: O sistema deve estar disponível 99,9% do tempo
4. **Usabilidade**: Interface intuitiva para todos os casos de uso
``