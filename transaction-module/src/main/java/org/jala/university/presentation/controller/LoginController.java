package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.jala.university.application.dto.UserDTO;
import org.jala.university.application.service.AuthenticationService;
import org.jala.university.application.service.SignupService;
import org.jala.university.application.service.impl.AuthenticationServiceImpl;
import org.jala.university.application.service.impl.SignupServiceImpl;
import org.jala.university.presentation.util.ViewSwitcher;
import org.jala.university.presentation.views.TransactionView;

/**
 * Controller for the login screen.
 * Handles user authentication and navigation to other screens.
 */
public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Label errorLabel;
    @FXML
    private Circle whiteCircle;
    @FXML
    private Circle redCircle;

    private final AuthenticationService authenticationService;
    private final SignupService signupService;

    /**
     * Constructor initializing the required dependencies.
     */
    public LoginController() {
        this.authenticationService = new AuthenticationServiceImpl();
        this.signupService = new SignupServiceImpl();
    }

    /**
     * Initializes the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        whiteCircle.setOnMouseClicked(event -> handleMinimizeWindow());
        redCircle.setOnMouseClicked(event -> handleCloseWindow());

        // Esconder mensagem de erro inicialmente
        if (errorLabel != null) {
            errorLabel.setVisible(false);
        }
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

    /**
     * Handles the login button click.
     */
    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Por favor, preencha todos os campos.");
            return;
        }

        try {
            String token = authenticationService.authenticate(email, password);

            if (token == null) {
                // 2FA é necessário
                UserDTO user = signupService.findUserByEmail(email);
                navigateToTwoFactorVerification(user);
            } else {
                // Autenticação bem-sucedida, redirecionar para o dashboard
                ViewSwitcher.switchTo(TransactionView.DASHBOARD);
            }
        } catch (Exception e) {
            showError("Credenciais inválidas. Tente novamente.");
        }
    }

    /**
     * Handles the sign-up button click.
     * Navigates to the sign-up screen.
     */
    @FXML
    private void handleSignUp() {
        ViewSwitcher.switchTo(TransactionView.SIGNUP);
    }

    /**
     * Navigates to the two-factor verification screen.
     *
     * @param user the user requiring 2FA verification
     */
    private void navigateToTwoFactorVerification(UserDTO user) {
        ViewSwitcher.switchTo(TransactionView.TWO_FACTOR_VERIFICATION, user);
    }

    /**
     * Shows an error message to the user.
     *
     * @param message the error message to display
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
