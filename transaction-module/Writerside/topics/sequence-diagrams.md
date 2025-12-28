# Diagramas de Sequência

## Visão Geral

Os diagramas de sequência ilustram como os objetos interagem em um cenário específico de um caso de uso. Eles mostram a ordem cronológica das mensagens trocadas entre os objetos e são úteis para entender o fluxo de controle em um sistema orientado a objetos.

## 1. Realizar Transação com QRCode

Este diagrama mostra a sequência de interações quando um cliente realiza uma transação usando QRCode.

```mermaid
sequenceDiagram
    actor Cliente
    participant UI as Interface do Usuário
    participant TC as TransactionController
    participant QRS as QRCodeService
    participant TS as TransactionService
    participant Auth as AuthenticationService
    participant SB as Sistema Bancário
    
    Cliente->>UI: Seleciona transação com QRCode
    UI->>TC: iniciarTransacaoQRCode()
    TC->>UI: Exibe scanner QRCode
    Cliente->>UI: Digitaliza QRCode
    UI->>TC: processarQRCode(dados)
    TC->>QRS: validarQRCode(dados)
    QRS-->>TC: Dados validados
    TC->>UI: Exibe detalhes da transação
    Cliente->>UI: Confirma detalhes
    UI->>TC: confirmarDetalhes()
    TC->>UI: Solicita senha
    Cliente->>UI: Insere senha
    UI->>TC: validarSenha(senha)
    TC->>Auth: autenticar(senha)
    Auth-->>TC: Autenticação bem-sucedida
    TC->>TS: processarTransacao(dados)
    TS->>SB: executarTransacao(dados)
    SB-->>TS: Transação concluída
    TS-->>TC: Resultado da transação
    TC->>UI: Exibe confirmação
    UI->>Cliente: Mostra recibo da transação
```

## 2. Realizar Transação com NFC

Este diagrama mostra a sequência de interações quando um cliente realiza uma transação usando NFC.

```mermaid
sequenceDiagram
    actor Cliente
    participant UI as Interface do Usuário
    participant TC as TransactionController
    participant NFCS as NFCService
    participant TS as TransactionService
    participant Auth as AuthenticationService
    participant SB as Sistema Bancário
    
    Cliente->>UI: Seleciona transação NFC
    UI->>TC: iniciarTransacaoNFC()
    TC->>NFCS: ativarNFC()
    NFCS-->>TC: NFC ativado
    TC->>UI: Solicita aproximação do dispositivo
    Cliente->>UI: Aproxima dispositivo
    NFCS->>TC: dadosRecebidos(dados)
    TC->>NFCS: validarDados(dados)
    NFCS-->>TC: Dados validados
    TC->>UI: Exibe detalhes da transação
    Cliente->>UI: Confirma detalhes
    UI->>TC: confirmarDetalhes()
    TC->>Auth: autenticarNFC(token)
    Auth-->>TC: Autenticação bem-sucedida
    TC->>TS: processarTransacao(dados)
    TS->>SB: executarTransacao(dados)
    SB-->>TS: Transação concluída
    TS-->>TC: Resultado da transação
    TC->>UI: Exibe confirmação
    UI->>Cliente: Mostra recibo da transação
```

## 3. Visualizar Histórico de Transações

Este diagrama mostra a sequência de interações quando um cliente visualiza seu histórico de transações.

```mermaid
sequenceDiagram
    actor Cliente
    participant UI as Interface do Usuário
    participant HC as HistoryController
    participant HS as HistoryService
    participant DB as Banco de Dados
    
    Cliente->>UI: Acessa histórico de transações
    UI->>HC: carregarHistorico()
    HC->>HS: obterHistoricoTransacoes(usuarioId)
    HS->>DB: consultarTransacoes(usuarioId)
    DB-->>HS: Lista de transações
    HS-->>HC: TransactionHistoryDTO[]
    HC->>UI: Exibe lista de transações
    UI->>Cliente: Mostra histórico
    
    Cliente->>UI: Aplica filtro
    UI->>HC: filtrarTransacoes(filtro)
    HC->>HS: filtrarHistorico(filtro)
    HS-->>HC: Transações filtradas
    HC->>UI: Atualiza lista
    UI->>Cliente: Mostra resultados filtrados
    
    Cliente->>UI: Seleciona transação
    UI->>HC: selecionarTransacao(id)
    HC->>HS: obterDetalhesTransacao(id)
    HS->>DB: consultarDetalhes(id)
    DB-->>HS: Detalhes da transação
    HS-->>HC: TransactionDetailsDTO
    HC->>UI: Exibe detalhes
    UI->>Cliente: Mostra detalhes da transação
```

## 4. Gerenciar Beneficiários

Este diagrama mostra a sequência de interações quando um cliente gerencia seus beneficiários.

```mermaid
sequenceDiagram
    actor Cliente
    participant UI as Interface do Usuário
    participant BC as BeneficiaryController
    participant BS as BeneficiaryService
    participant VS as ValidationService
    participant DB as Banco de Dados
    
    Cliente->>UI: Acessa gerenciamento de beneficiários
    UI->>BC: carregarBeneficiarios()
    BC->>BS: listarBeneficiarios(usuarioId)
    BS->>DB: consultarBeneficiarios(usuarioId)
    DB-->>BS: Lista de beneficiários
    BS-->>BC: BeneficiaryDTO[]
    BC->>UI: Exibe lista de beneficiários
    UI->>Cliente: Mostra beneficiários
    
    Cliente->>UI: Adiciona novo beneficiário
    UI->>BC: adicionarBeneficiario(dados)
    BC->>VS: validarDadosBeneficiario(dados)
    VS-->>BC: Dados validados
    BC->>BS: criarBeneficiario(dados)
    BS->>DB: inserirBeneficiario(dados)
    DB-->>BS: Beneficiário criado
    BS-->>BC: Resultado da operação
    BC->>UI: Atualiza lista
    UI->>Cliente: Mostra confirmação
    
    Cliente->>UI: Remove beneficiário
    UI->>BC: removerBeneficiario(id)
    BC->>BS: excluirBeneficiario(id)
    BS->>DB: deletarBeneficiario(id)
    DB-->>BS: Beneficiário removido
    BS-->>BC: Resultado da operação
    BC->>UI: Atualiza lista
    UI->>Cliente: Mostra confirmação
```

## 5. Realizar Transações entre Contas

Este diagrama mostra a sequência de interações quando um cliente realiza uma transferência entre contas.

```mermaid
sequenceDiagram
    actor Cliente
    participant UI as Interface do Usuário
    participant TC as TransferController
    participant BS as BeneficiaryService
    participant TS as TransactionService
    participant VS as ValidationService
    participant Auth as AuthenticationService
    participant SB as Sistema Bancário
    
    Cliente->>UI: Seleciona transferência entre contas
    UI->>TC: iniciarTransferencia()
    TC->>BS: listarBeneficiarios(usuarioId)
    BS-->>TC: Lista de beneficiários
    TC->>UI: Exibe formulário de transferência
    
    Cliente->>UI: Seleciona beneficiário
    Cliente->>UI: Preenche valor e detalhes
    UI->>TC: processarTransferencia(dados)
    TC->>VS: validarDadosTransferencia(dados)
    VS-->>TC: Dados validados
    
    TC->>UI: Solicita senha
    Cliente->>UI: Insere senha
    UI->>TC: confirmarTransferencia(senha)
    TC->>Auth: autenticar(senha)
    Auth-->>TC: Autenticação bem-sucedida
    
    TC->>TS: executarTransferencia(dados)
    TS->>SB: processarTransferencia(dados)
    SB-->>TS: Transferência concluída
    TS-->>TC: Resultado da transferência
    TC->>UI: Exibe confirmação
    UI->>Cliente: Mostra recibo da transferência
```

## 6. Agendar Transação

Este diagrama mostra a sequência de interações quando um cliente agenda uma transação.

```mermaid
sequenceDiagram
    actor Cliente
    participant UI as Interface do Usuário
    participant SC as ScheduleController
    participant SS as ScheduleService
    participant TS as TransactionService
    participant VS as ValidationService
    participant Auth as AuthenticationService
    participant DB as Banco de Dados
    
    Cliente->>UI: Seleciona agendamento de transação
    UI->>SC: iniciarAgendamento()
    SC->>UI: Exibe formulário de agendamento
    
    Cliente->>UI: Preenche detalhes e data
    UI->>SC: processarAgendamento(dados)
    SC->>VS: validarDadosAgendamento(dados)
    VS-->>SC: Dados validados
    
    SC->>UI: Solicita senha
    Cliente->>UI: Insere senha
    UI->>SC: confirmarAgendamento(senha)
    SC->>Auth: autenticar(senha)
    Auth-->>SC: Autenticação bem-sucedida
    
    SC->>SS: criarAgendamento(dados)
    SS->>DB: salvarAgendamento(dados)
    DB-->>SS: Agendamento criado
    SS-->>SC: Resultado do agendamento
    SC->>UI: Exibe confirmação
    UI->>Cliente: Mostra detalhes do agendamento
    
    Cliente->>UI: Visualiza agendamentos
    UI->>SC: listarAgendamentos()
    SC->>SS: obterAgendamentos(usuarioId)
    SS->>DB: consultarAgendamentos(usuarioId)
    DB-->>SS: Lista de agendamentos
    SS-->>SC: ScheduleDTO[]
    SC->>UI: Exibe lista de agendamentos
    UI->>Cliente: Mostra agendamentos
```

## 7. Realizar Transação com MFA

Este diagrama mostra a sequência de interações quando um cliente realiza uma transação com autenticação multifatorial.

```mermaid
sequenceDiagram
    actor Cliente
    participant UI as Interface do Usuário
    participant TC as TransactionController
    participant TS as TransactionService
    participant VS as ValidationService
    participant Auth as AuthenticationService
    participant MFA as MFAService
    participant AE as Autenticador Externo
    participant SB as Sistema Bancário
    
    Cliente->>UI: Inicia transação de alto valor
    UI->>TC: processarTransacao(dados)
    TC->>VS: validarDados(dados)
    VS-->>TC: Dados validados
    TC->>TS: verificarNecessidadeMFA(valor)
    TS-->>TC: MFA necessário
    
    TC->>MFA: solicitarCodigo(usuarioId)
    MFA->>AE: gerarCodigoMFA(usuarioId)
    AE-->>MFA: Código gerado
    MFA-->>TC: Solicitação enviada
    TC->>UI: Solicita código MFA
    
    Cliente->>UI: Insere código MFA
    UI->>TC: validarCodigoMFA(codigo)
    TC->>MFA: verificarCodigo(usuarioId, codigo)
    MFA->>AE: validarCodigo(usuarioId, codigo)
    AE-->>MFA: Código válido
    MFA-->>TC: Validação bem-sucedida
    
    TC->>UI: Solicita senha
    Cliente->>UI: Insere senha
    UI->>TC: confirmarTransacao(senha)
    TC->>Auth: autenticar(senha)
    Auth-->>TC: Autenticação bem-sucedida
    
    TC->>TS: executarTransacao(dados)
    TS->>SB: processarTransacao(dados)
    SB-->>TS: Transação concluída
    TS-->>TC: Resultado da transação
    TC->>UI: Exibe confirmação
    UI->>Cliente: Mostra recibo da transação
```

## Padrões Comuns

Nos diagramas de sequência acima, podemos observar alguns padrões comuns:

1. **Validação de Dados**: Antes de processar qualquer operação, os dados são validados
2. **Autenticação**: Operações sensíveis exigem autenticação do usuário
3. **Padrão MVC/MVVM**: Separação clara entre interface do usuário, controladores e serviços
4. **Confirmação de Operações**: Após cada operação, o usuário recebe uma confirmação

Estes padrões garantem a segurança, consistência e usabilidade do sistema.