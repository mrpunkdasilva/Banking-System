package org.jala.university.application.dto.auth;

/**
 * Record representing the data transfer object for user signup information.
 * This immutable record encapsulates all necessary fields for user registration.
 *
 * @param fullName    the user's full name
 * @param email      the user's email address
 * @param password   the user's chosen password
 * @param cpf        the user's CPF (Brazilian tax ID)
 * @param phoneNumber the user's phone number
 * @param accountType the type of account to be created (e.g., "Conta Corrente", "Conta Poupança")
 */
public record SignupDTO(
        String fullName,
        String email,
        String password,
        String cpf,
        String phoneNumber,
        String accountType
) {
    /**
     * Validates and constructs a new SignupDTO.
     * Ensures all required fields are non-null.
     *
     * @throws IllegalArgumentException if any required field is null
     */
    public SignupDTO {
        if (fullName == null) throw new IllegalArgumentException("Full name cannot be null");
        if (email == null) throw new IllegalArgumentException("Email cannot be null");
        if (password == null) throw new IllegalArgumentException("Password cannot be null");
        if (cpf == null) throw new IllegalArgumentException("CPF cannot be null");
        if (phoneNumber == null) throw new IllegalArgumentException("Phone number cannot be null");
        if (accountType == null) throw new IllegalArgumentException("Account type cannot be null");
    }
}
