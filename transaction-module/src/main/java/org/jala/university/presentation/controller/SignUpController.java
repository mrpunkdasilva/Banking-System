package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import org.jala.university.application.dto.AccountDTO;
import org.jala.university.application.dto.UserDTO;
import org.jala.university.application.service.SignupService;
import org.jala.university.application.service.TwoFactorAuthService;
import org.jala.university.application.service.impl.SignupServiceImpl;
import org.jala.university.application.service.impl.TwoFactorAuthServiceImpl;
import org.jala.university.application.service.implementations.AccountServiceImpl;
import org.jala.university.application.service.interfaces.AccountService;
import org.jala.university.application.validator.SignupValidator;
import org.jala.university.commons.domain.Role;
import org.jala.university.domain.entity.enums.AccountType;
import org.jala.university.domain.repository.UserRepository;
import org.jala.university.domain.validator.ValidationException;
import org.jala.university.domain.validator.implementation.CPFValidator;
import org.jala.university.domain.validator.implementation.EmailValidator;
import org.jala.university.domain.validator.implementation.PasswordValidator;
import org.jala.university.domain.validator.implementation.PhoneValidator;
import org.jala.university.infrastructure.persistence.UserRepositoryImp;
import org.jala.university.infrastructure.utils.Mercury;
import org.jala.university.presentation.util.ViewSwitcher;
import org.jala.university.presentation.views.TransactionView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller for the sign-up screen.
 * Handles user registration and account creation.
 */
public class SignUpController {
    
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField cpfField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> accountTypeCombo;
    @FXML private CheckBox enableTwoFactorCheckbox;
    
    private final SignupService signupService;
    private final AccountService accountService;
    private final SignupValidator signupValidator;
    private final Mercury mercury;
    private final TwoFactorAuthService twoFactorAuthService;
    
    /**
     * Constructor initializing the required dependencies.
     */
    public SignUpController() {
        this.signupService = new SignupServiceImpl();
        this.accountService = new AccountServiceImpl();
        this.twoFactorAuthService = new TwoFactorAuthServiceImpl();
        
        // Inicializar os validadores necessários
        EmailValidator emailValidator = new EmailValidator();
        PhoneValidator phoneValidator = new PhoneValidator();
        CPFValidator cpfValidator = new CPFValidator();
        PasswordValidator passwordValidator = new PasswordValidator();
        
        // Inicializar o repositório de usuários
        UserRepository userRepository = new UserRepositoryImp();
        
        // Inicializar o validador de cadastro com todas as dependências necessárias
        this.signupValidator = new SignupValidator(
            userRepository, 
            emailValidator, 
            phoneValidator, 
            cpfValidator, 
            passwordValidator
        );
        
        this.mercury = new Mercury();
    }
    
    /**
     * Initializes the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Inicializar o combo de tipos de conta
        accountTypeCombo.getItems().addAll("Conta Corrente", "Conta Poupança");
        accountTypeCombo.getSelectionModel().selectFirst();
        
        // Configurar máscaras para os campos
        setupInputMasks();
    }
    
    /**
     * Sets up input masks for the form fields.
     */
    private void setupInputMasks() {
        // Máscara para CPF: 000.000.000-00
        cpfField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 14) {
                cpfField.setText(oldValue);
                return;
            }
            
            String value = newValue.replaceAll("[^0-9]", "");
            StringBuilder formatted = new StringBuilder();
            
            for (int i = 0; i < value.length(); i++) {
                if (i == 3 || i == 6) {
                    formatted.append(".");
                } else if (i == 9) {
                    formatted.append("-");
                }
                formatted.append(value.charAt(i));
            }
            
            cpfField.setText(formatted.toString());
        });
        
        // Máscara para telefone: (00) 00000-0000
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 15) {
                phoneField.setText(oldValue);
                return;
            }
            
            String value = newValue.replaceAll("[^0-9]", "");
            StringBuilder formatted = new StringBuilder();
            
            for (int i = 0; i < value.length(); i++) {
                if (i == 0) {
                    formatted.append("(");
                } else if (i == 2) {
                    formatted.append(") ");
                } else if (i == 7) {
                    formatted.append("-");
                }
                formatted.append(value.charAt(i));
            }
            
            phoneField.setText(formatted.toString());
        });
    }
    
    /**
     * Handles the sign-up button click.
     * Validates the form data and creates a new user and account.
     */
    @FXML
    private void handleRegister() {
        try {
            // Verificar se as senhas coincidem
            if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                mercury.showErrorAlert("Erro de validação", "As senhas não coincidem.");
                return;
            }
            
            UserDTO userDTO = UserDTO.builder()
                    .name(fullNameField.getText())
                    .email(emailField.getText())
                    .password(passwordField.getText())
                    .cpf(cpfField.getText())
                    .phoneNumber(phoneField.getText())
                    .roles(Role.USER)
                    .build();

            signupValidator.validate(userDTO);
            UserDTO savedUser = signupService.save(userDTO);

            // Criar conta para o usuário
            AccountDTO createdAccount = accountService.createAccount(
                    savedUser.getCpf(),
                    accountTypeCombo.getValue().equals("Conta Corrente") ? AccountType.CHECKING : AccountType.SAVINGS
            );

            // Configurar autenticação de dois fatores se selecionado
            if (enableTwoFactorCheckbox != null && enableTwoFactorCheckbox.isSelected()) {
                twoFactorAuthService.setTwoFactorEnabled(savedUser.getId(), true);
                
                // Se 2FA está ativado, gerar e enviar código
                boolean codeSent = twoFactorAuthService.generateAndSendCode(savedUser.getId());
                
                if (codeSent) {
                    mercury.showSuccessAlert("Cadastrado com sucesso! Sua conta: " + createdAccount.getAccountNumber() + 
                                            "\nUm código de verificação foi enviado para seu email.");
                    
                    // Navegar para a tela de verificação de 2FA
                    navigateToTwoFactorVerification(savedUser);
                    return;
                } else {
                    mercury.showWarningAlert("Aviso", "Cadastrado com sucesso, mas não foi possível enviar o código de verificação. " +
                                            "Você poderá configurar a autenticação de dois fatores mais tarde.");
                }
            }

            // Exibir mensagem de sucesso e incluir o número da conta criada
            mercury.showSuccessAlert("Cadastrado com sucesso! Sua conta: " + createdAccount.getAccountNumber());
            ViewSwitcher.switchTo(TransactionView.LOGIN);
            
        } catch (ValidationException e) {
            mercury.showErrorAlert("Erro de validação", e.getMessage());
        } catch (Exception e) {
            mercury.showErrorAlert("Erro no cadastro", "Ocorreu um erro ao tentar realizar o cadastro.");
            e.printStackTrace();
        }
    }

    /**
     * Handles the back button click.
     * Navigates back to the login screen.
     */
    @FXML
    private void handleBack() {
        ViewSwitcher.switchTo(TransactionView.LOGIN);
    }

    /**
     * Navigates to the two-factor verification screen.
     * 
     * @param user the user requiring 2FA verification
     */
    private void navigateToTwoFactorVerification(UserDTO user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/two-factor-verification.fxml"));
            Parent root = loader.load();
            
            TwoFactorVerificationController controller = loader.getController();
            controller.setUser(user);
            
            Stage stage = (Stage) fullNameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            mercury.showErrorAlert("Erro", "Erro ao carregar a tela de verificação. Tente novamente.");
            ViewSwitcher.switchTo(TransactionView.LOGIN);
        }
    }
}
