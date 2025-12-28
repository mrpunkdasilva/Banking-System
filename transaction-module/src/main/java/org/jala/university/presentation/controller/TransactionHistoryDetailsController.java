package org.jala.university.presentation.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.jala.university.application.dto.TransactionDetailsDTO;
import org.jala.university.application.service.TransactionDetailsService;
import org.jala.university.application.service.impl.TransactionDetailsServiceImpl;
import org.jala.university.domain.entity.enums.TransactionStatus;
import org.jala.university.infrastructure.utils.SessionManager;
import org.jala.university.presentation.util.PDFProofGenerator;
import org.jala.university.presentation.util.ViewSwitcher;
import org.jala.university.presentation.views.TransactionView;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller responsible for displaying detailed information about a specific transaction.
 * This controller is used when a user selects a transaction from the history list.
 */
public class TransactionHistoryDetailsController {
    
    private static final Logger LOGGER = Logger.getLogger(TransactionHistoryDetailsController.class.getName());
    
    @FXML private Label statusIconLabel;
    @FXML private Label statusLabel;
    @FXML private Label amountLabel;
    @FXML private Label transactionIdLabel;
    @FXML private Label createdAtLabel;
    @FXML private Label transactionTypeLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label senderNameLabel;
    @FXML private Label senderDocumentLabel;
    @FXML private Label senderAccountNumberLabel;
    @FXML private Label senderBankLabel;
    @FXML private Label receiverNameLabel;
    @FXML private Label receiverDocumentLabel;
    @FXML private Label receiverAccountNumberLabel;
    @FXML private Label receiverBankLabel;
    @FXML private Button backButton;
    @FXML private Button printButton;
    
    private Long currentTransactionId;
    private TransactionDetailsService detailsService;
    
    /**
     * Constructor for the controller.
     * Initializes the transaction details service.
     */
    public TransactionHistoryDetailsController() {
        // O serviço será inicializado no método initialize
    }
    
    /**
     * Initializes the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        LOGGER.info("Inicializando TransactionHistoryDetailsController");
        
        // Inicializar o serviço
        this.detailsService = new TransactionDetailsServiceImpl();
        
        // Check if user is logged in
        if (SessionManager.getCurrentAccountId() == null) {
            showError("Nenhuma conta selecionada. Faça login novamente.");
            return;
        }
        
        // Get the transaction ID from session
        Object transactionIdObj = SessionManager.getTemporaryData("selectedTransactionId");
        LOGGER.info("ID da transação recuperado da sessão: " + transactionIdObj);
        
        if (transactionIdObj instanceof Long) {
            setTransactionId((Long) transactionIdObj);
        } else {
            showError("ID da transação não encontrado ou inválido");
        }
    }
    
    /**
     * Sets the transaction ID and loads the transaction details.
     *
     * @param transactionId the ID of the transaction to display
     */
    public void setTransactionId(Long transactionId) {
        LOGGER.info("Definindo ID da transação: " + transactionId);
        this.currentTransactionId = transactionId;
        
        // Carregamos os detalhes em um thread separado para não bloquear a UI
        Platform.runLater(this::loadTransactionDetails);
    }
    
    /**
     * Loads the transaction details from the service and displays them.
     */
    private void loadTransactionDetails() {
        if (currentTransactionId == null) {
            showError("ID da transação não especificado");
            return;
        }
        
        try {
            Long currentAccountId = SessionManager.getCurrentAccountId();
            LOGGER.info("Carregando detalhes da transação " + currentTransactionId + 
                       " para a conta " + currentAccountId);
            
            TransactionDetailsDTO details = detailsService.getTransactionDetails(currentTransactionId, currentAccountId);
            
            if (details == null) {
                showError("Transação não encontrada");
                return;
            }
            
            LOGGER.info("Detalhes da transação carregados com sucesso: " + details);
            displayTransactionDetails(details);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar detalhes da transação", e);
            showError("Erro ao carregar detalhes da transação: " + e.getMessage());
        }
    }
    
    /**
     * Displays the transaction details in the UI.
     *
     * @param details the transaction details to display
     */
    private void displayTransactionDetails(TransactionDetailsDTO details) {
        try {
            // Limpar estilos anteriores para evitar duplicação
            amountLabel.getStyleClass().removeAll("debit-amount", "credit-amount");
            statusLabel.getStyleClass().removeAll(
                "status-completed", "status-pending", "status-failed", "status-cancelled");
            
            // Formatar a data
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String formattedDate = details.getCreatedAt().format(formatter);
            
            // Definir o ícone de status
            String statusIcon = switch (details.getStatus()) {
                case COMPLETED -> "✓";
                case PENDING -> "⏳";
                case FAILED -> "✗";
                case CANCELLED -> "⊘";
                default -> "?";
            };
            
            // Definir o tipo de transação
            String transactionType = details.isDebit() ? "Enviada" : "Recebida";
            
            // Set labels
            statusIconLabel.setText(statusIcon);
            statusLabel.setText(formatStatus(details.getStatus()));
            transactionIdLabel.setText(details.getId().toString());
            amountLabel.setText(formatCurrency(details.getAmount()));
            createdAtLabel.setText(formattedDate);
            transactionTypeLabel.setText(transactionType);
            descriptionLabel.setText(details.getDescription() != null ? 
                                    details.getDescription() : "Sem descrição");
            
            // Sender details
            senderNameLabel.setText(details.getSenderName());
            senderDocumentLabel.setText("XXX.XXX.XXX-XX"); // Placeholder for document
            senderAccountNumberLabel.setText(details.getSenderAccountNumber());
            senderBankLabel.setText("Banco XYZ"); // Placeholder for bank
            
            // Receiver details
            receiverNameLabel.setText(details.getReceiverName());
            receiverDocumentLabel.setText("XXX.XXX.XXX-XX"); // Placeholder for document
            receiverAccountNumberLabel.setText(details.getReceiverAccountNumber());
            receiverBankLabel.setText("Banco XYZ"); // Placeholder for bank
            
            // Apply styling based on transaction type and status
            if (details.isDebit()) {
                amountLabel.getStyleClass().add("debit-amount");
            } else {
                amountLabel.getStyleClass().add("credit-amount");
            }
            
            statusLabel.getStyleClass().add("status-" + details.getStatus().toString().toLowerCase());
            
            // Setup action buttons
            setupActionButtons(details);
            
            LOGGER.info("Detalhes da transação exibidos com sucesso na UI");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao exibir detalhes da transação na UI", e);
            showError("Erro ao exibir detalhes: " + e.getMessage());
        }
    }
    
    /**
     * Sets up the action buttons based on transaction details.
     *
     * @param details the transaction details
     */
    private void setupActionButtons(TransactionDetailsDTO details) {
        // Print button action
        printButton.setOnAction(event -> {
            try {
                javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
                fileChooser.setTitle("Salvar Comprovante");
                fileChooser.getExtensionFilters().add(
                        new javafx.stage.FileChooser.ExtensionFilter("Arquivo PDF", "*.pdf")
                );
                java.io.File file = fileChooser.showSaveDialog(printButton.getScene().getWindow());

                if (file != null) {
                    PDFProofGenerator.generate(details, file.getAbsolutePath());
                    showMessage("Sucesso", "Comprovante salvo com sucesso!");
                }
            } catch (Exception e) {
                showMessage("Erro", "Erro ao gerar comprovante: " + e.getMessage());
            }
        });
    }
    
    /**
     * Shows a message to the user.
     *
     * @param type the message type
     * @param message the message text
     */
    private void showMessage(String type, String message) {
        LOGGER.info("Exibindo mensagem: " + type + " - " + message);
        
        Alert alert = new Alert(
            type.equals("Erro") ? Alert.AlertType.ERROR : 
            type.equals("Sucesso") ? Alert.AlertType.INFORMATION : 
            Alert.AlertType.WARNING
        );
        alert.setTitle(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Formats a status enum to a user-friendly string.
     *
     * @param status the transaction status
     * @return a formatted status string
     */
    private String formatStatus(TransactionStatus status) {
        return switch (status) {
            case COMPLETED -> "Concluída";
            case PENDING -> "Pendente";
            case FAILED -> "Falha";
            case CANCELLED -> "Cancelada";
            default -> status.toString();
        };
    }
    
    /**
     * Formats a currency amount.
     *
     * @param amount the amount to format
     * @return a formatted currency string
     */
    private String formatCurrency(BigDecimal amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return currencyFormatter.format(amount);
    }
    
    /**
     * Shows an error message.
     *
     * @param message the error message to display
     */
    private void showError(String message) {
        LOGGER.warning("ERRO: " + message);
        
        // Exibir alerta visual para o usuário
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
            
            // Voltar para a tela anterior após erro crítico
            if (message.contains("Nenhuma conta selecionada") || 
                message.contains("ID da transação não encontrado")) {
                ViewSwitcher.switchTo(TransactionView.HISTORIC);
            }
        });
    }
    
    /**
     * Handles the back button click.
     * Returns to the transaction history view.
     */
    @FXML
    private void handleBack() {
        LOGGER.info("Voltando para a tela de histórico de transações");
        ViewSwitcher.switchTo(TransactionView.HISTORIC);
    }
}
