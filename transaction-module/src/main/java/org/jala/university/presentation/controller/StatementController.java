package org.jala.university.presentation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.jala.university.application.dto.TransactionHistoryDTO;
import org.jala.university.application.service.TransactionHistoryService;
import org.jala.university.application.service.impl.TransactionHistoryServiceImpl;
import org.jala.university.domain.entity.enums.TransactionStatus;
import org.jala.university.domain.repository.TransactionRepository;
import org.jala.university.infrastructure.persistence.TransactionRepositoryImpl;
import org.jala.university.infrastructure.utils.SessionManager;
import org.jala.university.presentation.util.PDFExporter;
import org.jala.university.presentation.util.CSVExporter;
import org.jala.university.presentation.util.ViewSwitcher;
import org.jala.university.presentation.views.TransactionView;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class StatementController {

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TableView<TransactionHistoryDTO> statementTable;
    @FXML private TableColumn<TransactionHistoryDTO, LocalDateTime> dateColumn;
    @FXML private TableColumn<TransactionHistoryDTO, String> descriptionColumn;
    @FXML private TableColumn<TransactionHistoryDTO, String> counterpartyColumn;
    @FXML private TableColumn<TransactionHistoryDTO, TransactionStatus> statusColumn;
    @FXML private TableColumn<TransactionHistoryDTO, BigDecimal> amountColumn;
    @FXML private Button exportCsvButton;
    @FXML private Button exportPdfButton;

    private final TransactionHistoryService transactionHistoryService;
    private final ObservableList<TransactionHistoryDTO> filteredTransactions = FXCollections.observableArrayList();

    public StatementController() {
        TransactionRepository repo = new TransactionRepositoryImpl();
        this.transactionHistoryService = new TransactionHistoryServiceImpl(repo);
    }

    @FXML
    public void initialize() {
        // Configure table columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setCellFactory(column -> new TableCell<TransactionHistoryDTO, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                }
            }
        });
        
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        counterpartyColumn.setCellValueFactory(new PropertyValueFactory<>("counterpartyName"));
        
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(column -> new TableCell<TransactionHistoryDTO, TransactionStatus>() {
            @Override
            protected void updateItem(TransactionStatus status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status.toString());
                    if (status == TransactionStatus.COMPLETED) {
                        setStyle("-fx-text-fill: green;");
                    } else if (status == TransactionStatus.FAILED) {
                        setStyle("-fx-text-fill: red;");
                    } else if (status == TransactionStatus.PENDING) {
                        setStyle("-fx-text-fill: orange;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
        
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setCellFactory(column -> new TableCell<TransactionHistoryDTO, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    // Format as currency
                    setText(String.format("R$ %.2f", amount));
                    
                    // Color based on debit/credit
                    TransactionHistoryDTO transaction = getTableView().getItems().get(getIndex());
                    if (transaction != null) {
                        if (transaction.isDebit()) {
                            setStyle("-fx-text-fill: red;");
                        } else {
                            setStyle("-fx-text-fill: green;");
                        }
                    }
                }
            }
        });
        
        // Set the table data source
        statementTable.setItems(filteredTransactions);
        
        // Load all transactions immediately
        loadAllTransactions();
        
        // Set default date range for date pickers (last 30 days)
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);
        startDatePicker.setValue(startDate);
        endDatePicker.setValue(endDate);
    }
    
    private void loadAllTransactions() {
        Long accountId = SessionManager.getCurrentAccountId();
        if (accountId == null) {
            showAlert("Nenhuma conta selecionada.");
            return;
        }
        
        // Load all transactions without date filtering
        List<TransactionHistoryDTO> transactions = transactionHistoryService.getAccountTransactionHistory(accountId);
        
        // Filter to show only COMPLETED transactions
        List<TransactionHistoryDTO> completedTransactions = transactions.stream()
                .filter(tx -> tx.getStatus() == TransactionStatus.COMPLETED)
                .collect(Collectors.toList());
        
        filteredTransactions.setAll(completedTransactions);
        
        if (completedTransactions.isEmpty()) {
            showAlert("Nenhuma transação concluída encontrada.");
        }
    }

    @FXML
    private void handleFilter() {
        Long accountId = SessionManager.getCurrentAccountId();
        if (accountId == null) {
            showAlert("Nenhuma conta selecionada.");
            return;
        }

        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        
        if (start == null || end == null) {
            showAlert("Selecione o período para o extrato.");
            return;
        }
        
        if (end.isBefore(start)) {
            showAlert("A data final deve ser posterior à data inicial.");
            return;
        }

        // Convert LocalDate to LocalDateTime to include the entire day
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);
        
        List<TransactionHistoryDTO> filtered = transactionHistoryService.getAccountTransactionHistory(accountId, start, end);
        
        // Filter to show only COMPLETED transactions
        List<TransactionHistoryDTO> completedTransactions = filtered.stream()
                .filter(tx -> tx.getStatus() == TransactionStatus.COMPLETED)
                .collect(Collectors.toList());
                
        filteredTransactions.setAll(completedTransactions);

        if (completedTransactions.isEmpty()) {
            showAlert("Nenhuma transação concluída encontrada para o período selecionado.");
        }
    }

    @FXML
    private void handleExportCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Extrato CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        
        // Set default filename with pattern EXTRATO_{DIA_MES_SEGUNDOS_ANO}
        String defaultFileName = generateDefaultFileName("csv");
        fileChooser.setInitialFileName(defaultFileName);
        
        File file = fileChooser.showSaveDialog(statementTable.getScene().getWindow());
        if (file != null) {
            CSVExporter.exportToCsv(filteredTransactions, file);
        }
    }

    @FXML
    private void handleExportPdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Extrato PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        
        // Set default filename with pattern EXTRATO_{DIA_MES_SEGUNDOS_ANO}
        String defaultFileName = generateDefaultFileName("pdf");
        fileChooser.setInitialFileName(defaultFileName);
        
        File file = fileChooser.showSaveDialog(statementTable.getScene().getWindow());
        if (file != null) {
            PDFExporter.exportToPdf(filteredTransactions, file);
        }
    }
    
    /**
     * Generates a default filename with the pattern EXTRATO_{DIA_MES_SEGUNDOS_ANO}
     * @param extension The file extension (without dot)
     * @return The formatted filename
     */
    private String generateDefaultFileName(String extension) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_ss_yyyy");
        return "EXTRATO_" + now.format(formatter) + "." + extension;
    }

    @FXML
    private void handleBack() {
        try {
            ViewSwitcher.switchTo(TransactionView.DASHBOARD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
