# Interface do Usuário

A interface do usuário foi desenvolvida usando JavaFX, seguindo um design moderno e responsivo. O sistema utiliza FXML para separação entre layout e lógica, e controllers para gerenciar as interações.

## Estrutura de Arquivos

```plaintext
src/main/
├── resources/
│   ├── fxml/
│   │   ├── login-view.fxml
│   │   ├── signup-view.fxml
│   │   ├── dashboard-view.fxml
│   │   ├── transfer-view.fxml
│   │   ├── history-view.fxml
│   │   ├── transaction-details-screen.fxml
│   │   └── beneficiary-view.fxml
│   └── css/
│       └── styles.css
└── java/org/jala/university/presentation/
    ├── controller/
    │   ├── LoginController.java
    │   ├── SignUpController.java
    │   ├── DashboardController.java
    │   ├── TransferController.java
    │   ├── TransactionHistoryController.java
    │   ├── TransactionHistoryDetailsController.java
    │   └── BeneficiaryController.java
    └── util/
        ├── ViewSwitcher.java
        └── SessionManager.java
```

## Padrões de Design

### MVC (Model-View-Controller)
- **Model**: Entidades e DTOs que representam os dados
- **View**: Arquivos FXML que definem a interface
- **Controller**: Classes Java que controlam a interação entre Model e View

### FXML
- Layouts declarativos em XML
- Separação clara entre design e lógica
- Facilidade de manutenção e atualização

### CSS
- Estilização separada da estrutura
- Temas consistentes em toda a aplicação
- Personalização de componentes

## Principais Telas

### Tela de Login
Interface de autenticação com campos para email e senha, além de opção para cadastro de novos usuários.

### Dashboard
Tela principal que exibe saldo, transações recentes e acesso rápido às principais funcionalidades.

### Transferências
Interface para realização de transferências entre contas, com validação de dados e confirmação.

### Histórico de Transações
Visualização detalhada do histórico de transações com filtros e opções de busca.

### Detalhes da Transação
Tela que apresenta informações detalhadas sobre uma transação específica.

### Gerenciamento de Beneficiários
Interface para adicionar, editar e remover beneficiários para transferências.

## Navegação entre Telas

O sistema utiliza o padrão ViewSwitcher para gerenciar a navegação entre telas, permitindo:

- Transição suave entre diferentes interfaces
- Manutenção do estado da aplicação durante a navegação
- Gerenciamento centralizado de rotas

## Responsividade

A interface foi projetada para se adaptar a diferentes tamanhos de tela:

1. **Layout Fluido**
   - Uso de containers que se ajustam ao tamanho da janela
   - Elementos que redimensionam proporcionalmente

2. **Media Queries em CSS**
   - Estilos específicos para diferentes tamanhos de tela
   - Ajustes de fonte e espaçamento

3. **Componentes Adaptáveis**
   - Tabelas com colunas que se ajustam
   - Formulários que reorganizam campos em telas menores

## Acessibilidade

O sistema implementa as seguintes práticas de acessibilidade:

1. **Navegação por Teclado**
   - Todos os elementos podem ser acessados via teclado
   - Ordem de tabulação lógica

2. **Textos Alternativos**
   - Descrições para elementos visuais
   - Mensagens de erro claras e específicas

3. **Contraste Adequado**
   - Cores com contraste suficiente para leitura
   - Opção de tema de alto contraste
