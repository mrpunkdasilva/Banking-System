package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.jala.university.application.dto.UserDTO;
import org.jala.university.application.service.implementations.UserServiceImpl;
import org.jala.university.application.service.interfaces.UserService;
import org.jala.university.commons.domain.Role;
import org.jala.university.infrastructure.utils.SessionManager;
import org.jala.university.presentation.util.ViewSwitcher;
import org.jala.university.presentation.views.TransactionView;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the user profile screen.
 * <p>
 * This controller manages the user profile interface which allows users to:
 * <ul>
 *     <li>View their personal information (name, email, CPF, phone)</li>
 *     <li>Check and manage two-factor authentication settings</li>
 *     <li>Navigate to password change functionality</li>
 *     <li>Return to the dashboard</li>
 * </ul>
 * <p>
 * The controller loads user data from the current session and displays it
 * in a formatted way, including proper formatting for CPF and phone numbers.
 */
public class ProfileController {
    /** Logger for this class */
    private static final Logger LOGGER = Logger.getLogger(ProfileController.class.getName());

    /** Label displaying the user's initials in the avatar */
    @FXML private Label userInitialsLabel;
    
    /** Label displaying the user's name in the header */
    @FXML private Label userNameHeader;
    
    /** Label displaying the user's role */
    @FXML private Label userRoleLabel;
    
    /** Label displaying the user's full name */
    @FXML private Label nameLabel;
    
    /** Label displaying the user's email address */
    @FXML private Label emailLabel;
    
    /** Label displaying the user's formatted CPF (Brazilian ID) */
    @FXML private Label cpfLabel;
    
    /** Label displaying the user's formatted phone number */
    @FXML private Label phoneLabel;
    
    /** Label displaying the status of two-factor authentication */
    @FXML private Label twoFactorStatusLabel;
    
    /** Button for managing two-factor authentication settings */
    @FXML private Button twoFactorButton;

    /** Service for user-related operations */
    private final UserService userService;
    
    /** Data transfer object containing the current user's information */
    private UserDTO currentUser;

    /**
     * Default constructor.
     * <p>
     * Initializes the necessary services for user data operations.
     */
    public ProfileController() {
        this.userService = new UserServiceImpl();
    }

    /**
     * Initialization method called after FXML loading.
     * <p>
     * This method is automatically called by JavaFX after the FXML file has been loaded.
     * It verifies if a user is logged in and loads their data, or redirects to the login
     * screen if no user session is found.
     */
    @FXML
    private void initialize() {
        // Check if user is logged in
        currentUser = SessionManager.getCurrentUser();
        
        if (currentUser == null) {
            LOGGER.log(Level.WARNING, "No user logged in. Redirecting to login.");
            ViewSwitcher.switchTo(TransactionView.LOGIN);
            return;
        }
        
        // Load user data
        loadUserData();
    }

    /**
     * Loads the current user's data into the interface fields.
     * <p>
     * This method:
     * <ul>
     *     <li>Refreshes user data from the database</li>
     *     <li>Sets the user's name in the header</li>
     *     <li>Generates and displays user initials for the avatar</li>
     *     <li>Formats and displays the user's role</li>
     *     <li>Populates all profile information fields with formatted data</li>
     *     <li>Updates the two-factor authentication status and button text</li>
     * </ul>
     */
    private void loadUserData() {
        try {
            // Update user data from database
            if (currentUser.getId() != null) {
                currentUser = userService.findById(currentUser.getId());
            }
            
            // Fill fields with user data
            String userName = currentUser.getName();
            userNameHeader.setText(userName);
            
            // Set user initials for avatar
            userInitialsLabel.setText(generateInitials(userName));
            
            // Set user role text
            Role userRole = currentUser.getRoles();
            String roleText = (userRole != null) ? formatRoleName(userRole.name()) : "User";
            userRoleLabel.setText(roleText);
            
            // Fill information fields
            nameLabel.setText(userName);
            emailLabel.setText(currentUser.getEmail());
            cpfLabel.setText(formatCpf(currentUser.getCpf()));
            phoneLabel.setText(formatPhoneNumber(currentUser.getPhoneNumber()));
            
            // Update two-factor authentication status
            boolean twoFactorEnabled = currentUser.isTwoFactorEnabled();
            twoFactorStatusLabel.setText(twoFactorEnabled ? "Enabled" : "Disabled");
            twoFactorStatusLabel.getStyleClass().clear();
            twoFactorStatusLabel.getStyleClass().add(twoFactorEnabled ? "status-enabled" : "status-disabled");
            
            // Update two-factor button text
            twoFactorButton.setText(twoFactorEnabled ? "Manage 2FA" : "Setup 2FA");
            
            LOGGER.log(Level.INFO, "User data loaded successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading user data", e);
        }
    }
    
    /**
     * Generates user initials for display in the avatar.
     * <p>
     * Takes the first letter of the first name and the first letter of the last name.
     * If the user has only one name, returns just the first letter of that name.
     * 
     * @param fullName The user's full name
     * @return Initials (up to 2 characters)
     */
    private String generateInitials(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "?";
        }
        
        String[] nameParts = fullName.trim().split("\\s+");
        if (nameParts.length == 1) {
            // If there's only one part in the name, return the first letter
            return nameParts[0].substring(0, 1).toUpperCase();
        } else {
            // Return the first letter of the first and last name
            String firstInitial = nameParts[0].substring(0, 1).toUpperCase();
            String lastInitial = nameParts[nameParts.length - 1].substring(0, 1).toUpperCase();
            return firstInitial + lastInitial;
        }
    }
    
    /**
     * Formats the role name for display.
     * <p>
     * Converts the enum role name to a more user-friendly display format.
     * 
     * @param roleName The role name in enum format
     * @return Formatted role name for display
     */
    private String formatRoleName(String roleName) {
        if (roleName == null) {
            return "User";
        };

        return switch (roleName) {
            case "ADMIN" -> "Administrator";
            case "USER" -> "User";
            case "MANAGER" -> "Manager";
            default -> roleName.substring(0, 1).toUpperCase() + roleName.substring(1).toLowerCase();
        };
    }
    
    /**
     * Formats the CPF (Brazilian ID) for display (XXX.XXX.XXX-XX).
     * <p>
     * Adds punctuation to the CPF number to make it more readable.
     * 
     * @param cpf Unformatted CPF
     * @return Formatted CPF
     */
    private String formatCpf(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            return "Not provided";
        };
        if (cpf.length() != 11) {
            return cpf;
        };

        String s = cpf.substring(0, 3) + "." +
                cpf.substring(3, 6) + "." +
                cpf.substring(6, 9) + "-" +
                cpf.substring(9);
        return s;
    }
    
    /**
     * Formats the phone number for display.
     * <p>
     * Converts a raw phone number into a formatted string with appropriate
     * punctuation based on the number of digits.
     * 
     * @param phoneNumber Unformatted phone number
     * @return Formatted phone number
     */
    private String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) return "Not provided";
        
        // Remove non-numeric characters
        String digitsOnly = phoneNumber.replaceAll("\\D", "");

        return switch (digitsOnly.length()) {
            case 11 ->
                // Format: (XX) XXXXX-XXXX (mobile)
                    "(" + digitsOnly.substring(0, 2) + ") " +
                            digitsOnly.substring(2, 7) + "-" +
                            digitsOnly.substring(7);
            case 10 ->
                // Format: (XX) XXXX-XXXX (landline)
                    "(" + digitsOnly.substring(0, 2) + ") " +
                            digitsOnly.substring(2, 6) + "-" +
                            digitsOnly.substring(6);
            default ->
                // Return the original number if unable to format
                    phoneNumber;
        };
    }

    /**
     * Handles the click on the back to dashboard button.
     * <p>
     * Navigates back to the main dashboard screen.
     */
    @FXML
    private void handleBackToDashboard() {
        try {
            ViewSwitcher.switchTo(TransactionView.DASHBOARD);
            LOGGER.log(Level.INFO, "Navigated back to dashboard");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error navigating to dashboard", e);
        }
    }

    /**
     * Handles the click on the two-factor authentication settings button.
     * <p>
     * Navigates to the two-factor authentication settings screen where the user
     * can enable, disable, or manage their two-factor authentication setup.
     */
    @FXML
    private void handleTwoFactorSettings() {
        try {
            ViewSwitcher.switchTo(TransactionView.TWO_FACTOR_SETTINGS);
            LOGGER.log(Level.INFO, "Navigated to two-factor settings screen");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error navigating to two-factor settings screen", e);
        }
    }

    /**
     * Handles the click on the change password button.
     * <p>
     * This is a placeholder for future implementation. Currently displays
     * an information alert indicating that the feature is under development.
     */
    @FXML
    private void handleChangePassword() {
        // Future implementation - currently just displays an alert
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION,
                "Password change functionality will be implemented soon.",
                javafx.scene.control.ButtonType.OK
        );
        alert.setTitle("Change Password");
        alert.setHeaderText("Feature Under Development");
        alert.showAndWait();
    }
}
