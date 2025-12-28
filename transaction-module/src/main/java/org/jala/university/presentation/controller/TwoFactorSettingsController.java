package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import org.jala.university.application.service.TwoFactorAuthService;
import org.jala.university.application.service.impl.TwoFactorAuthServiceImpl;
import org.jala.university.presentation.util.SceneManager;
import org.jala.university.presentation.util.SessionManager;
import org.jala.university.presentation.util.ViewSwitcher;
import org.jala.university.presentation.views.TransactionView;

/**
 * Controller for the two-factor authentication settings screen.
 * Allows users to enable or disable 2FA for their account.
 */
public class TwoFactorSettingsController {

    @FXML
    private CheckBox enableTwoFactorCheckbox;
    
    @FXML
    private Label statusLabel;
    
    private final TwoFactorAuthService twoFactorAuthService;
    
    /**
     * Constructor initializing the required services.
     */
    public TwoFactorSettingsController() {
        this.twoFactorAuthService = new TwoFactorAuthServiceImpl();
    }
    
    /**
     * Initializes the controller.
     * Loads the current 2FA settings for the user.
     */
    @FXML
    private void initialize() {
        Long userId = SessionManager.getInstance().getCurrentUserId();
        if (userId != null) {
            boolean enabled = twoFactorAuthService.isTwoFactorEnabled(userId);
            enableTwoFactorCheckbox.setSelected(enabled);
        }
    }
    
    /**
     * Handles saving the 2FA settings.
     */
    @FXML
    private void handleSave() {
        Long userId = SessionManager.getInstance().getCurrentUserId();
        if (userId == null) {
            showStatus("Erro de sessão. Por favor, faça login novamente.", true);
            return;
        }
        
        boolean enabled = enableTwoFactorCheckbox.isSelected();
        boolean success = twoFactorAuthService.setTwoFactorEnabled(userId, enabled);
        
        if (success) {
            String status = enabled ? "ativada" : "desativada";
            showStatus("Autenticação de dois fatores " + status + " com sucesso.", false);
        } else {
            showStatus("Falha ao atualizar as configurações. Tente novamente.", true);
        }
    }
    
    /**
     * Handles the back button click.
     * Returns to the profile screen.
     */
    @FXML
    private void handleBack() {
        try {
            ViewSwitcher.switchTo(TransactionView.PROFILE);
            System.out.println("Navegado de volta para a tela de perfil");
        } catch (Exception e) {
            System.out.println("Erro ao navegar para a tela de perfil: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Shows a status message to the user.
     * 
     * @param message the message to display
     * @param isError whether the message is an error
     */
    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
        
        // Limpar estilos anteriores
        statusLabel.getStyleClass().removeAll("status-success", "status-error");
        
        // Aplicar estilo baseado no tipo de mensagem
        if (isError) {
            statusLabel.setStyle("-fx-text-fill: #dc3545; -fx-border-color: #f5c6cb; -fx-background-color: #f8d7da;");
        } else {
            statusLabel.setStyle("-fx-text-fill: #28a745; -fx-border-color: #c3e6cb; -fx-background-color: #d4edda;");
        }
    }
}