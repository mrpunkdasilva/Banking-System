package org.jala.university.presentation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jala.university.application.dto.TransactionHistoryDTO;
import org.jala.university.application.dto.UserDTO;
import org.jala.university.application.service.TransactionHistoryService;
import org.jala.university.application.service.impl.TransactionHistoryServiceImpl;
import org.jala.university.application.service.implementations.AccountServiceImpl;
import org.jala.university.application.service.implementations.UserServiceImpl;
import org.jala.university.application.service.interfaces.AccountService;
import org.jala.university.application.service.interfaces.UserService;
import org.jala.university.domain.entity.enums.TransactionStatus;
import org.jala.university.domain.repository.TransactionRepository;
import org.jala.university.infrastructure.persistence.TransactionRepositoryImpl;
import org.jala.university.infrastructure.utils.SessionManager;
import org.jala.university.presentation.util.CurrencyFormatter;
import org.jala.university.presentation.util.DateTimeFormatter;
import org.jala.university.presentation.util.ViewSwitcher;
import org.jala.university.presentation.views.TransactionView;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardController {
    private static final Logger LOGGER = Logger.getLogger(DashboardController.class.getName());
    private static final int MAX_RECENT_TRANSACTIONS = 5;

    @FXML
    private Label balanceLabel;
    @FXML
    private Label transactionCountLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private TableView<TransactionHistoryDTO> recentTransactionsTable;
    @FXML
    private TableColumn<TransactionHistoryDTO, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<TransactionHistoryDTO, String> descriptionColumn;
    @FXML
    private TableColumn<TransactionHistoryDTO, BigDecimal> amountColumn;
    @FXML
    private TableColumn<TransactionHistoryDTO, TransactionStatus> statusColumn;

    private final TransactionHistoryService transactionHistoryService;
    private final AccountService accountService;
    private final UserService userService;
    private final ObservableList<TransactionHistoryDTO> recentTransactions = FXCollections.observableArrayList();

    public DashboardController() {
        TransactionRepository transactionRepository = new TransactionRepositoryImpl();
        this.transactionHistoryService = new TransactionHistoryServiceImpl(transactionRepository);
        this.accountService = new AccountServiceImpl();
        this.userService = new UserServiceImpl();
    }

    @FXML
    private void initialize() {
        Long accountId = SessionManager.getCurrentAccountId();
        if (userNameLabel == null) {
            LOGGER.log(Level.SEVERE, "userNameLabel não foi injetado corretamente");
        } else {
            userNameLabel.setText("Bem-vindo(a)");
            UserDTO currentUser = SessionManager.getCurrentUser();
            if (currentUser != null && currentUser.getName() != null) {
                userNameLabel.setText("Bem-vindo(a), " + currentUser.getName());
                LOGGER.log(Level.INFO, "Nome do usuário obtido do SessionManager: " + currentUser.getName());
            }
        }
        if (accountId == null) {
            LOGGER.log(Level.WARNING, "Nenhuma conta selecionada. Redirecionando para login.");
            ViewSwitcher.switchTo(TransactionView.LOGIN);
            return;
        }
        setupTransactionsTable();
        loadDashboardData(accountId);
    }

    @FXML
    public void onSceneReady() {
    }

    private void setupTransactionsTable() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setCellFactory(column -> new DateTimeFormatter<>());
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setCellFactory(column -> new CurrencyFormatter<>());
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        recentTransactionsTable.setItems(recentTransactions);
    }

    private void loadDashboardData(Long accountId) {
        try {
            BigDecimal balance = accountService.getAccountBalance(accountId);
            balanceLabel.setText("R$ " + balance.toString());
            List<TransactionHistoryDTO> transactions = transactionHistoryService.getAccountTransactionHistory(accountId);
            transactionCountLabel.setText(String.valueOf(transactions.size()));
            recentTransactions.clear();
            int count = Math.min(transactions.size(), MAX_RECENT_TRANSACTIONS);
            if (count > 0) {
                recentTransactions.addAll(transactions.subList(0, count));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar dados do dashboard", e);
        }
    }

    /**
     * Handles the action when the user clicks the view statement button.
     * <p>
     * Navigates to the account statement view which displays detailed
     * transaction information for the current account.
     */
    @FXML
    private void handleViewStatement() {
        try {
            LOGGER.log(Level.INFO, "Navegado para tela de extrato");
            ViewSwitcher.switchTo(TransactionView.STATEMENT);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao navegar para tela de extrato", e);
        }
    }

    /**
     * Handles the action when the user clicks the new transaction button.
     * <p>
     * Navigates to the transaction creation view where the user can
     * initiate a new financial transaction.
     */
    @FXML
    private void handleNewTransaction() {
        try {
            ViewSwitcher.switchTo(TransactionView.TRANSACTIONS);
            LOGGER.log(Level.INFO, "Navegado para tela de transferência");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao navegar para tela de transferência", e);
        }
    }

    /**
     * Handles the action when the user clicks the view history button.
     * <p>
     * Navigates to the transaction history view which displays a complete
     * list of all transactions for the current account.
     */
    @FXML
    private void handleViewHistory() {
        try {
            ViewSwitcher.switchTo(TransactionView.HISTORIC);
            LOGGER.log(Level.INFO, "Navegado para tela de histórico");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao navegar para tela de histórico", e);
        }
    }

    /**
     * Handles the action when the user clicks the beneficiary management button.
     * <p>
     * Navigates to the beneficiary management view where the user can add,
     * edit, or remove beneficiaries for transactions.
     */
    @FXML
    private void handleBeneficiaryManagement() {
        try {
            ViewSwitcher.switchTo(TransactionView.BENEFICIARY);
            LOGGER.log(Level.INFO, "Navegado para tela de beneficiários");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao navegar para tela de beneficiários", e);
        }
    }

    /**
     * Handles the action when the user clicks the view profile button.
     * <p>
     * Navigates to the user profile view where the user can view and
     * potentially edit their personal information.
     */
    @FXML
    private void handleViewProfile() {
        try {
            ViewSwitcher.switchTo(TransactionView.PROFILE);
            LOGGER.log(Level.INFO, "Navegado para tela de perfil");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao navegar para tela de perfil", e);
        }
    }

    /**
     * Handles the action when the user clicks the logout button.
     * <p>
     * Displays a confirmation dialog and, if confirmed:
     * <ul>
     *     <li>Clears the user session data</li>
     *     <li>Logs the logout action</li>
     *     <li>Redirects to the login screen</li>
     * </ul>
     * <p>
     * If the user cancels the logout, no action is taken.
     */
    @FXML
    private void handleLogout() {
        // Show confirmation dialog
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.CONFIRMATION,
                "Tem certeza que deseja sair?",
                javafx.scene.control.ButtonType.YES,
                javafx.scene.control.ButtonType.NO
        );
        alert.setTitle("Confirmação de Logout");
        alert.setHeaderText("Sair do Sistema");

        // Wait for user response
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.YES) {
                // Limpar dados da sessão
                SessionManager.clearSession();

                // Log the logout action
                LOGGER.log(Level.INFO, "Usuário realizou logout com sucesso");

                try {
                    ViewSwitcher.switchTo(TransactionView.LOGIN);
                    LOGGER.log(Level.INFO, "Redirecionado para tela de login");
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Erro ao redirecionar para tela de login", e);
                }
            }
        });
    }
}
