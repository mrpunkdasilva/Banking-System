package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jala.university.application.dto.TwoFactorVerificationDTO;
import org.jala.university.application.dto.UserDTO;
import org.jala.university.application.service.AuthenticationService;
import org.jala.university.application.service.TwoFactorAuthService;
import org.jala.university.application.service.impl.AuthenticationServiceImpl;
import org.jala.university.application.service.impl.TwoFactorAuthServiceImpl;
import org.jala.university.domain.entity.Transaction;
import org.jala.university.presentation.util.ViewSwitcher;
import org.jala.university.presentation.views.TransactionView;

/**
 * Controller for the two-factor verification screen.
 * Handles the verification of 2FA codes and user interactions.
 */
public class TwoFactorVerificationController {

    @FXML
    private TextField codeField;
    
    @FXML
    private Button verifyButton;
    
    @FXML
    private Button resendButton;
    
    @FXML
    private Label errorLabel;
    
    private final TwoFactorAuthService twoFactorAuthService;
    private final AuthenticationService authenticationService;
    private UserDTO currentUser;
    
    /**
     * Constructor initializing the required services.
     */
    public TwoFactorVerificationController() {
        this.twoFactorAuthService = new TwoFactorAuthServiceImpl();
        this.authenticationService = new AuthenticationServiceImpl();
    }
    
    /**
     * Sets the current user for verification.
     *
     * @param user the user requiring 2FA verification
     */
    public void setUser(UserDTO user) {
        this.currentUser = user;
    }

    /**
     * Handles the verification of the entered code.
     */
    @FXML
    private void handleVerify() {
        String code = codeField.getText().trim();
        
        if (code.isEmpty()) {
            showError("Por favor, digite o código de verificação.");
            return;
        }
        
        if (currentUser == null || currentUser.getId() == null) {
            showError("Erro de sessão. Por favor, faça login novamente.");
            return;
        }
        
        TwoFactorVerificationDTO verificationDTO = new TwoFactorVerificationDTO();
        verificationDTO.setUserId(currentUser.getId());
        verificationDTO.setCode(code);
        
        String token = authenticationService.verifyTwoFactor(verificationDTO);
        
        if (token == null) {
            showError("Código inválido ou expirado. Tente novamente.");
            return;
        }
        
        // Autenticação bem-sucedida, redirecionar para o dashboard
        try {
            ViewSwitcher.switchTo(TransactionView.DASHBOARD);
            System.out.println("Stage redimensionável após 2FA");
        } catch (Exception e) {
            showError("Erro ao carregar o dashboard. Tente novamente.");
            e.printStackTrace();
        }
    }
    
    /**
     * Handles the resending of the verification code.
     */
    @FXML
    private void handleResend() {

        if (currentUser == null || currentUser.getId() == null) {
            showError("Erro de sessão. Por favor, faça login novamente.");
            return;
        }
        
        boolean sent = twoFactorAuthService.resendCode(currentUser.getId());
        
        if (sent) {
            errorLabel.setVisible(true);
            errorLabel.setText("Um novo código foi enviado para seu email.");
            errorLabel.setStyle("-fx-text-fill: green;");
        } else {
            showError("Falha ao reenviar o código. Tente novamente mais tarde.");
        }
    }
    
    /**
     * Shows an error message to the user.
     * 
     * @param message the error message to display
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setStyle("-fx-text-fill: red;");
    }

     public void initData(UserDTO user) {
        this.currentUser = user;
    }
}