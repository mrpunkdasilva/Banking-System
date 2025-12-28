package org.jala.university.presentation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jala.university.application.dto.TransactionHistoryDTO;
import org.jala.university.application.service.TransactionHistoryService;
import org.jala.university.application.service.impl.TransactionHistoryServiceImpl;
import org.jala.university.domain.entity.enums.TransactionStatus;
import org.jala.university.domain.repository.TransactionRepository;
import org.jala.university.infrastructure.persistence.TransactionRepositoryImpl;
import org.jala.university.infrastructure.utils.SessionManager;
import org.jala.university.presentation.util.ViewSwitcher;
import org.jala.university.presentation.views.TransactionView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionHistoryController {
    @FXML
    private TableView<TransactionHistoryDTO> transactionTable;
    @FXML
    private Label statusLabel;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<String> statusFilterComboBox;
    @FXML
    private Button filterButton;
    @FXML
    private Button clearFilterButton;

    private final ObservableList<TransactionHistoryDTO> transactionList = FXCollections.observableArrayList();
    private final TransactionHistoryService transactionHistoryService;

    public TransactionHistoryController() {
        // Inicialização do serviço com o novo repositório
        TransactionRepository transactionRepository = new TransactionRepositoryImpl();
        this.transactionHistoryService = new TransactionHistoryServiceImpl(transactionRepository);
    }

    @FXML
    private void initialize() {
        // Check if user is logged in
        if (SessionManager.getCurrentAccountId() == null) {
            showMessage("Erro", "Nenhuma conta selecionada. Faça login novamente.");
            return;
        }

        // Setup table
        setupTable();

        // Setup filters
        setupFilters();

        // Load transactions
        loadTransactions();
    }

    private void setupTable() {
        // Create columns
        TableColumn<TransactionHistoryDTO, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<TransactionHistoryDTO, TransactionStatus> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        // Aplicar estilo baseado no status
        statusColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(TransactionStatus item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    getStyleClass().removeAll("status-completed", "status-pending", "status-failed", "status-cancelled");
                } else {
                    setText(formatStatus(item));
                    getStyleClass().removeAll("status-completed", "status-pending", "status-failed", "status-cancelled");

                    switch (item) {
                        case COMPLETED -> getStyleClass().add("status-completed");
                        case PENDING -> getStyleClass().add("status-pending");
                        case FAILED -> getStyleClass().add("status-failed");
                        case CANCELLED -> getStyleClass().add("status-cancelled");
                    }
                }
            }
        });

        TableColumn<TransactionHistoryDTO, BigDecimal> amountColumn = new TableColumn<>("Valor");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        // Aplicar estilo baseado no valor (positivo/negativo)
        amountColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    getStyleClass().removeAll("amount-positive", "amount-negative");
                } else {
                    setText(String.format("R$ %.2f", item));
                    getStyleClass().removeAll("amount-positive", "amount-negative");

                    if (item.compareTo(BigDecimal.ZERO) >= 0) {
                        getStyleClass().add("amount-positive");
                    } else {
                        getStyleClass().add("amount-negative");
                    }
                }
            }
        });

        TableColumn<TransactionHistoryDTO, LocalDateTime> dateColumn = new TableColumn<>("Data");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        // Formatar data
        dateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                }
            }
        });

        TableColumn<TransactionHistoryDTO, String> descriptionColumn = new TableColumn<>("Descrição");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<TransactionHistoryDTO, String> counterpartyColumn = new TableColumn<>("Contraparte");
        counterpartyColumn.setCellValueFactory(new PropertyValueFactory<>("counterpartyName"));

        // Add columns to table
        transactionTable.getColumns().addAll(
                idColumn, statusColumn, amountColumn, dateColumn, descriptionColumn, counterpartyColumn
        );

        // Set data source
        transactionTable.setItems(transactionList);

        // Add double-click handler to open transaction details
        transactionTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && transactionTable.getSelectionModel().getSelectedItem() != null) {
                TransactionHistoryDTO selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
                openTransactionDetails(selectedTransaction.getId());
            }
        });
    }

    private void setupFilters() {
        // Setup date pickers
        startDatePicker.setValue(LocalDate.now().minusMonths(1));
        endDatePicker.setValue(LocalDate.now());

        // Setup status filter
        List<String> statusOptions = Arrays.stream(TransactionStatus.values())
                .map(this::formatStatus)
                .collect(Collectors.toList());
        statusOptions.add(0, "TODOS"); // Adiciona opção para mostrar todos os status

        statusFilterComboBox.setItems(FXCollections.observableArrayList(statusOptions));
        statusFilterComboBox.getSelectionModel().selectFirst();

        // Setup filter button
        filterButton.setOnAction(event -> applyFilters());

        // Setup clear filter button
        clearFilterButton.setOnAction(event -> clearFilters());
    }

    private String formatStatus(TransactionStatus status) {
        return switch (status) {
            case COMPLETED -> "Concluída";
            case PENDING -> "Pendente";
            case FAILED -> "Falha";
            case CANCELLED -> "Cancelada";
            default -> status.toString();
        };
    }

    private void loadTransactions() {
        transactionList.clear();
        try {
            Long accountId = SessionManager.getCurrentAccountId();
            if (accountId != null) {
                // Carregar apenas transações reais
                List<TransactionHistoryDTO> transactions = transactionHistoryService.getAccountTransactionHistory(accountId);

                if (transactions.isEmpty()) {
                    showMessage("Informação", "Nenhuma transação encontrada para esta conta.");
                } else {
                    transactionList.addAll(transactions);
                }
            } else {
                showMessage("Erro", "Nenhuma conta selecionada");
            }
        } catch (Exception e) {
            showMessage("Erro", "Falha ao carregar transações: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void applyFilters() {
        transactionList.clear();
        try {
            Long accountId = SessionManager.getCurrentAccountId();
            if (accountId == null) {
                showMessage("Erro", "Nenhuma conta selecionada");
                return;
            }

            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String selectedStatus = statusFilterComboBox.getValue();

            List<TransactionHistoryDTO> filteredTransactions;

            // Aplicar filtros apenas em dados reais
            if ("TODOS".equals(selectedStatus)) {
                filteredTransactions = transactionHistoryService.getAccountTransactionHistory(
                        accountId, startDate, endDate
                );
            } else {
                String statusCode = mapDisplayStatusToEnum(selectedStatus);
                filteredTransactions = transactionHistoryService.getAccountTransactionHistoryByDateRangeAndStatus(
                        accountId, startDate, endDate, statusCode
                );
            }

            transactionList.addAll(filteredTransactions);

            if (filteredTransactions.isEmpty()) {
                showMessage("Informação", "Nenhuma transação encontrada com os filtros aplicados.");
            }
        } catch (Exception e) {
            showMessage("Erro", "Falha ao aplicar filtros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String mapDisplayStatusToEnum(String displayStatus) {
        return switch (displayStatus) {
            case "Pendente" -> "PENDING";
            case "Concluída" -> "COMPLETED";
            case "Falha" -> "FAILED";
            case "Cancelada" -> "CANCELLED";
            default -> null;
        };
    }

    private void clearFilters() {
        startDatePicker.setValue(LocalDate.now().minusMonths(1));
        endDatePicker.setValue(LocalDate.now());
        statusFilterComboBox.getSelectionModel().selectFirst();
        loadTransactions();
    }

    private void openTransactionDetails(Long transactionId) {
        // Armazena o ID da transação selecionada na sessão
        SessionManager.setTemporaryData("selectedTransactionId", transactionId);

        // Navega para a tela de detalhes
        ViewSwitcher.switchTo(TransactionView.TRANSACTION_DETAILS);
    }

    @FXML
    private void handleBack() {
        try {
            ViewSwitcher.switchTo(TransactionView.DASHBOARD);
            System.out.println("Voltando para o dashboard");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao voltar para o dashboard: " + e.getMessage());
        }
    }

    private void showMessage(String type, String message) {
        statusLabel.setText(message);

        // Remover todas as classes de estilo anteriores
        statusLabel.getStyleClass().removeAll("status-info", "status-error", "status-success");

        // Aplicar a classe de estilo apropriada
        switch (type) {
            case "Informação" -> statusLabel.getStyleClass().add("status-info");
            case "Sucesso" -> statusLabel.getStyleClass().add("status-success");
            case "Erro" -> statusLabel.getStyleClass().add("status-error");
            default -> statusLabel.getStyleClass().add("status-info");
        }
    }
}
