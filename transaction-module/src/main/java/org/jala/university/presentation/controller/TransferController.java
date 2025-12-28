package org.jala.university.presentation.controller;

import jakarta.persistence.EntityManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.jala.university.application.dto.AccountDTO;
import org.jala.university.application.dto.BeneficiaryDTO;
import org.jala.university.application.dto.TransactionDTO;
import org.jala.university.application.dto.UserDTO;
import org.jala.university.application.map.AccountMapper;
import org.jala.university.application.map.BeneficiaryMapper;
import org.jala.university.application.map.TransactionMapper;
import org.jala.university.application.service.TransactionsService;
import org.jala.university.application.service.impl.TransactionsServiceImpl;
import org.jala.university.application.service.implementations.AccountServiceImpl;
import org.jala.university.application.service.implementations.UserServiceImpl;
import org.jala.university.application.service.interfaces.AccountService;
import org.jala.university.application.service.interfaces.UserService;
import org.jala.university.application.usecases.BeneficiaryUseCases;
import org.jala.university.domain.entity.Beneficiary;
import org.jala.university.domain.repository.AccountBeneficiaryRepository;
import org.jala.university.domain.repository.AccountRepository;
import org.jala.university.domain.repository.BeneficiaryRepository;
import org.jala.university.domain.services.ValidationService;
import org.jala.university.infrastructure.config.JPAConfig;
import org.jala.university.infrastructure.persistence.AccountBeneficiaryRepositoryImp;
import org.jala.university.infrastructure.persistence.AccountRepositoryImp;
import org.jala.university.infrastructure.persistence.BeneficiaryRepositoryImp;
import org.jala.university.infrastructure.utils.SessionManager;
import org.jala.university.presentation.util.ViewSwitcher;
import org.jala.university.presentation.views.TransactionView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class TransferController {

    private static final Logger LOGGER = LogManager.getLogger(TransferController.class);

    @FXML
    private ComboBox<AccountDTO> sourceAccountCombo;

    @FXML
    private ComboBox<BeneficiaryDTO> destinationAccountCombo;

    @FXML
    private TextField amountField;

    @FXML
    private DatePicker scheduleDatePicker;

    @FXML
    private TextArea descriptionField;

    @FXML
    private Button transferButton;

    @FXML
    private Hyperlink cancelLink;

    @FXML
    private Circle whiteCircle;

    @FXML
    private Circle redCircle;

    // Novos botões de navegação
    @FXML
    private Button dashboardButton;

    @FXML
    private Button historyButton;

    @FXML
    private Button beneficiariesButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Label userNameLabel;

    private AccountDTO selectedSourceAccount;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    private final AccountService accountService;
    private final BeneficiaryUseCases beneficiaryUseCases;
    ValidationService validationService = new ValidationService();
    EntityManager entityManager = JPAConfig.getEntityManagerFactory().createEntityManager();
    BeneficiaryRepository beneficiaryRepository = new BeneficiaryRepositoryImp(entityManager);
    AccountRepository accountRepository = new AccountRepositoryImp(entityManager);
    AccountBeneficiaryRepository accountBeneficiaryRepository = new AccountBeneficiaryRepositoryImp(entityManager);
    private final BeneficiaryMapper beneficiaryMapper = new BeneficiaryMapper();
    private final TransactionsService transactionsService;
    private final AccountMapper accountMapper = new AccountMapper();
    private final TransactionMapper transactionMapper = new TransactionMapper();
    private final UserService userService;

    public TransferController() {
        this.beneficiaryUseCases = new BeneficiaryUseCases(
                beneficiaryRepository,
                accountRepository,
                accountBeneficiaryRepository,
                validationService
        );
        this.transactionsService = new TransactionsServiceImpl();
        this.accountService = new AccountServiceImpl();
        this.userService = new UserServiceImpl();
    }

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private void initialize() {
        whiteCircle.setOnMouseClicked(event -> handleMinimizeWindow());
        redCircle.setOnMouseClicked(event -> handleCloseWindow());

        // Verificar se há uma conta selecionada (mesmo padrão do DashboardController)
        Long accountId = SessionManager.getCurrentAccountId();
        if (accountId == null) {
            LOGGER.log(Level.WARN, "Nenhuma conta selecionada. Redirecionando para login.");
            ViewSwitcher.switchTo(TransactionView.LOGIN);
            return;
        }

        // Carregar e exibir o nome do usuário
        loadUserName();

        // Criar contas de exemplo
        AccountDTO accountById = this.accountService.getAccountById(accountId);
        List<Beneficiary> allBeneficiariesFromAccount = this.beneficiaryUseCases.listBeneficiariesByAccountId(accountId);
        List<BeneficiaryDTO> beneficiaryDTOS = allBeneficiariesFromAccount.stream().map(this.beneficiaryMapper::mapTo).toList();

        // Configurar ComboBoxes
        sourceAccountCombo.getItems().addAll(accountById);
        destinationAccountCombo.getItems().addAll(beneficiaryDTOS);

        // Configurar formatação para exibir as contas nos ComboBoxes
        StringConverter<AccountDTO> accountConverter = new StringConverter<AccountDTO>() {
            @Override
            public String toString(AccountDTO account) {
                if (account == null) return "";
                return account.getAccountNumber();
            }

            @Override
            public AccountDTO fromString(String string) {
                return null; // Não é necessário para este caso
            }
        };

        StringConverter<BeneficiaryDTO> beneficiaryDTO = new StringConverter<>() {
            @Override
            public String toString(BeneficiaryDTO beneficiaryDTO) {
                if (beneficiaryDTO == null) return "";
                return beneficiaryDTO.getName();
            }

            @Override
            public BeneficiaryDTO fromString(String s) {
                return null;
            }
        };

        sourceAccountCombo.setConverter(accountConverter);
        destinationAccountCombo.setConverter(beneficiaryDTO);

        // Selecionar a conta de Melina por padrão
        sourceAccountCombo.setValue(accountById);
        selectedSourceAccount = accountById;


        // Adicionar listener para atualizar o saldo disponível quando a conta de origem mudar
        sourceAccountCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedSourceAccount = newVal;

                // Remover a conta selecionada das opções de destino
                updateDestinationAccounts();
            }
        });

        // Adicionar listener para remover a conta de destino das opções de origem
        destinationAccountCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Garantir que a mesma conta não possa ser selecionada como origem e destino
                if (sourceAccountCombo.getValue() != null &&
                        sourceAccountCombo.getValue().getAccountNumber().equals(newVal.getAccountNumber())) {
                    sourceAccountCombo.setValue(null);
                }
            }
        });

        // Configurar formatação de moeda para o campo de valor
        amountField.setText("R$0.00");
        amountField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) return;

            String cleanValue = newValue.replaceAll("[^\\d]", "");

            if (cleanValue.isEmpty()) {
                Platform.runLater(() -> amountField.setText("R$0.00"));
                return;
            }

            try {
                double value = Double.parseDouble(cleanValue) / 100.0;
                String formatted = "R$" + String.format("%.2f", value);

                if (!newValue.equals(formatted)) {
                    Platform.runLater(() -> {
                        int caret = amountField.getCaretPosition();
                        amountField.setText(formatted);
                        amountField.positionCaret(Math.min(caret, formatted.length()));
                    });
                }
            } catch (NumberFormatException e) {
                Platform.runLater(() -> amountField.setText("R$0.00"));
            }
        });

        // Configurar o DatePicker
        configureDatePicker();

        // Adicionar texto de exemplo na descrição
        descriptionField.setText("PopCorn and Ice Cream Sinners.");
    }

    /**
     * Minimiza a janela atual quando o círculo branco é clicado
     */
    private void handleMinimizeWindow() {
        Stage stage = (Stage) whiteCircle.getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * Fecha a janela atual quando o círculo vermelho é clicado
     */
    private void handleCloseWindow() {
        Stage stage = (Stage) redCircle.getScene().getWindow();
        stage.close();
    }

    private void configureDatePicker() {
        scheduleDatePicker.setEditable(true);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        scheduleDatePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.trim().isEmpty())
                    return null;

                try {
                    return LocalDate.parse(string, dateFormatter);
                } catch (DateTimeParseException e) {
                    // Não é necessário lançar exceção; apenas retorna null.
                    return null;
                }
            }
        });

        // Impede datas passadas
        scheduleDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        // Formata e corrige entrada quando o foco sai do campo
        scheduleDatePicker.getEditor()
                .focusedProperty()
                .addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        String input = scheduleDatePicker.getEditor().getText();
                        if (input == null || input.trim().isEmpty()) return;

                        try {
                            LocalDate parsedDate = LocalDate.parse(input, dateFormatter);
                            scheduleDatePicker.setValue(parsedDate);
                        } catch (DateTimeParseException e) {
                            // Limpa campo se a data for inválida
                            scheduleDatePicker.getEditor().setText("");
                            scheduleDatePicker.setValue(null);
                        }
                    }
                });
    }


    private void updateDestinationAccounts() {
        BeneficiaryDTO currentDestination = destinationAccountCombo.getValue();
        destinationAccountCombo.getItems().clear();

        // Adicionar todas as contas exceto a selecionada como origem
        /**for (AccountDTO account : sourceAccountCombo.getItems()) {
         if (selectedSourceAccount == null || !account.getAccountNumber().equals(selectedSourceAccount.getAccountNumber())) {
         destinationAccountCombo.getItems().add(account);
         }
         }**/

        // Restaurar a seleção anterior se ainda for válida
        if (
                currentDestination != null
                        &&
                        (selectedSourceAccount == null ||
                                !currentDestination
                                        .getAccountNumber()
                                        .equals(selectedSourceAccount.getAccountNumber()))
        ) {
            destinationAccountCombo.setValue(currentDestination);
        } else if (!destinationAccountCombo.getItems().isEmpty()) {
            destinationAccountCombo.setValue(destinationAccountCombo.getItems().get(0));
        }
    }

    @FXML
    private void handleTransfer() {
        // Validar campos
        if (!validateFields()) {
            return;
        }

        AccountDTO source = sourceAccountCombo.getValue();
        BeneficiaryDTO destination = destinationAccountCombo.getValue();

        // Extrair o valor numérico do campo de valor (remover o símbolo R$)
        String amountText = amountField.getText().replace("R$", "")
                .replace(",", ".")
                .trim();
        System.out.println("amountText: " + amountText);
        double amount = Double.parseDouble(amountText);
        System.out.println("amount converted: " + amount);

        String description = descriptionField.getText();
        LocalDate scheduleDate = scheduleDatePicker.getValue();

        Long currentAccountId = SessionManager.getCurrentAccountId();
        AccountDTO sourceAccount = this.accountService.getAccountById(currentAccountId);
        AccountDTO destinationAccount = this.accountService
                .getAccountByAccountNumber(destination.getAccountNumber());

        // Simular a transferência (chama um serviço)
        boolean success = performTransfer(sourceAccount, destinationAccount, amount, description);
        if (scheduleDate != null && scheduleDate.isAfter(LocalDate.now())) {
            // Simular o agendamento da transferência
            showScheduleConfirmationDialog(sourceAccount, destinationAccount, amount, scheduleDate, description);
        } else {
            if (success) {
                // Mostrar confirmação
                showConfirmationDialog(sourceAccount, destinationAccount, amount);

                // Limpar campos
                amountField.setText("R$0.00");
                descriptionField.clear();
                scheduleDatePicker.setValue(null);
            }
        }


        if (amountText.isEmpty()) {
            showError("Please enter an amount.");
            return;
        }


        if (amount <= 0) {
            showError("Amount must be greater than zero.");
            return;
        }
    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Transfer Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private boolean validateFields() {
        StringBuilder errorMessage = new StringBuilder();

        if (sourceAccountCombo.getValue() == null) {
            errorMessage.append("Por favor, selecione uma conta de origem.\n");
        }

        if (destinationAccountCombo.getValue() == null) {
            errorMessage.append("Por favor, selecione uma conta de destino.\n");
        }

        String amountText = amountField.getText().replace("R$", "")
                .replace(",", ".")
                .trim();

        if (amountText.isEmpty() || amountText.equals("0.00")) {
            errorMessage.append("Por favor, informe o valor da transferência.\n");
        } else {
            try {
                double amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    errorMessage.append("O valor da transferência deve ser maior que zero.\n");
                }
            } catch (NumberFormatException e) {
                errorMessage.append("Valor de transferência inválido.\n");
            }
        }

        // Validar data de agendamento
        LocalDate scheduleDate = scheduleDatePicker.getValue();
        if (scheduleDate != null && scheduleDate.isBefore(LocalDate.now())) {
            errorMessage.append("A data de agendamento deve ser hoje ou uma data futura.\n");
        }

        if (!errorMessage.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro na Transferência");
            alert.setHeaderText("Corrija os seguintes erros:");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private boolean performTransfer(AccountDTO source, AccountDTO destination, double amount, String description) {
        // Simular uma transferência (chamaria uma API)
        System.out.println("Performing transfer..." + amount);
        try {
            // Verificar saldo
            if (source.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro na Transferência");
                alert.setHeaderText("Saldo Insuficiente");
                alert.setContentText("Você não tem saldo suficiente para realizar esta transferência.");
                alert.showAndWait();
                return false;
            }

            LocalDate scheduleDate = scheduleDatePicker.getValue();

            // Atualizar saldos
            source.setBalance(source.getBalance().subtract(BigDecimal.valueOf(amount)));
            destination.setBalance(destination.getBalance().add(BigDecimal.valueOf(amount)));

            TransactionDTO transaction = TransactionDTO.builder()
                    .sender(this.accountMapper.mapFrom(source))
                    .receiver(this.accountMapper.mapFrom(destination))
                    .amount(BigDecimal.valueOf(amount))
                    .description(description)
                    .transactionSchedule(scheduleDate != null ? scheduleDate : LocalDate.now())
                    .build();

            this.transactionsService.save(transaction);


            return true;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro na Transferência");
            alert.setHeaderText("Falha na Transferência");
            alert.setContentText("Ocorreu um erro ao processar sua transferência. Por favor, tente novamente.");
            alert.showAndWait();
            return false;
        }
    }

    private void showConfirmationDialog(AccountDTO source, AccountDTO destination, double amount) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Transferência Realizada");
        alert.setHeaderText("Transferência Concluída com Sucesso");

        String content = String.format(
                "Transferência de %s realizada com sucesso!\n\n" +
                        "De: %s\n" +
                        "Para: %s\n" +
                        "Valor: %s\n" +
                        "Novo saldo: %s",
                currencyFormat.format(amount).replace("$", "R$"),
                source.getAccountNumber(),
                destination.getAccountNumber(),
                currencyFormat.format(amount).replace("$", "R$"),
                currencyFormat.format(source.getBalance()).replace("$", "R$")
        );

        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showScheduleConfirmationDialog(
            AccountDTO source,
            AccountDTO destination,
            double amount,
            LocalDate scheduleDate,
            String description
    ) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Transferência Agendada");
        alert.setHeaderText("Transferência Agendada com Sucesso");

        String content = String.format(
                "Transferência de %s agendada com sucesso!\n\n" +
                        "De: %s\n" +
                        "Para: %s\n" +
                        "Valor: %s\n" +
                        "Data de execução: %s\n" +
                        "Descrição: %s",
                currencyFormat.format(amount).replace("$", "R$"),
                source.getAccountNumber(),
                destination.getAccountNumber(),
                currencyFormat.format(amount).replace("$", "R$"),
                dateFormatter.format(scheduleDate),
                description.isEmpty() ? "(Sem descrição)" : description
        );

        alert.setContentText(content);
        alert.showAndWait();

        // Limpar campos após agendamento
        amountField.setText("R$0.00");
        descriptionField.clear();
        scheduleDatePicker.setValue(null);
    }

    @FXML
    private void handleCancel() {
        // Voltar para a tela anterior
        ViewSwitcher.switchTo(TransactionView.DASHBOARD);
    }

    // Novos métodos para navegação
    @FXML
    private void handleDashboard() {
        try {
            ViewSwitcher.switchTo(TransactionView.DASHBOARD);
            LOGGER.log(Level.INFO, "Navegado para tela de dashboard");
        } catch (Exception e) {
            LOGGER.log(Level.WARN, "Erro ao navegar para tela de dashboard", e);
        }
    }


    @FXML
    private void handleViewHistory() {
        try {
            ViewSwitcher.switchTo(TransactionView.HISTORIC);
            LOGGER.log(Level.INFO, "Navegado para tela de histórico");
        } catch (Exception e) {
            LOGGER.log(Level.WARN, "Erro ao navegar para tela de histórico", e);
        }
    }


    @FXML
    private void handleBeneficiaries() {
        try {
            ViewSwitcher.switchTo(TransactionView.BENEFICIARY);
            LOGGER.log(Level.INFO, "Navegado para tela de beneficiários");
        } catch (Exception e) {
            LOGGER.log(Level.WARN, "Erro ao navegar para tela de beneficiários", e);
        }
    }


    @FXML
    private void handleLogout() {
        // Mostrar diálogo de confirmação
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.CONFIRMATION,
                "Tem certeza que deseja sair?",
                javafx.scene.control.ButtonType.YES,
                javafx.scene.control.ButtonType.NO
        );
        alert.setTitle("Confirmação de Logout");
        alert.setHeaderText("Sair do Sistema");

        // Esperar pela resposta do usuário
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.YES) {
                // Limpar dados da sessão
                SessionManager.clearSession();

                // Log da ação de logout
                LOGGER.log(Level.INFO, "Usuário realizou logout com sucesso");

                try {
                    ViewSwitcher.switchTo(TransactionView.LOGIN);
                    LOGGER.log(Level.INFO, "Redirecionado para tela de login");
                } catch (Exception e) {
                    LOGGER.log(Level.WARN, "Erro ao redirecionar para tela de login", e);
                }
            }
        });
    }

    @FXML
    private Label avatarText; // Para as iniciais no avatar

    /**
     * Gera as iniciais do nome do usuário para o avatar
     */
    private String generateInitials(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "US";
        }

        String[] nameParts = fullName.trim().split("\\s+");
        if (nameParts.length == 1) {
            return nameParts[0].substring(0, Math.min(2, nameParts[0].length())).toUpperCase();
        } else {
            return (nameParts[0].substring(0, 1) + nameParts[nameParts.length - 1].substring(0, 1)).toUpperCase();
        }
    }


    /**
     * Carrega e exibe o nome do usuário logado (versão simplificada)
     */
    private void loadUserName() {
        if (userNameLabel == null) {
            LOGGER.log(Level.WARN, "userNameLabel não foi injetado corretamente");
        } else {
            userNameLabel.setText("Bem-vindo(a)");
            UserDTO currentUser = SessionManager.getCurrentUser();
            if (currentUser != null && currentUser.getName() != null) {
                userNameLabel.setText(currentUser.getName()); // Removendo o "Bem-vindo(a)," para ficar só o nome

                // Atualizar as iniciais no avatar se o campo existir
                if (avatarText != null) {
                    avatarText.setText(generateInitials(currentUser.getName()));
                }

                LOGGER.log(Level.INFO, "Nome do usuário obtido do SessionManager: " + currentUser.getName());
            }
        }
    }

    /**
     * Define informações padrão do usuário em caso de erro
     */
    private void setDefaultUserInfo() {
        userNameLabel.setText("Usuário");
        if (avatarText != null) {
            avatarText.setText("US");
        }
        LOGGER.warn("Usando informações padrão do usuário");
    }
}
