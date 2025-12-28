package org.jala.university.application.service.interfaces;

import org.jala.university.application.dto.AccountDTO;
import org.jala.university.domain.entity.enums.AccountType;

import java.math.BigDecimal;

/**
 * Service interface for managing accounts.
 * Provides methods for account creation and management.
 */
public interface AccountService {
    /**
     * Creates a new account for the given user.
     *
     * @param cpf user CPF
     * @param accountType account type
     * @return created account DTO
     */
    AccountDTO createAccount(String cpf, AccountType accountType);
    
    /**
     * Retrieves the current balance for an account.
     *
     * @param accountId the ID of the account
     * @return the current balance of the account
     */
    BigDecimal getAccountBalance(Long accountId);

    /**
     * Updates the balance of a user's account.
     *
     * @param userId the ID of the user
     * @param newBalance the new balance to set
     * @throws RuntimeException if the user account doesn't have enough balance
     */
    void updateUserAccountBalance(Long userId, BigDecimal newBalance);
    
    /**
     * Retrieves account information for a specific user.
     *
     * @param userId the ID of the user
     * @return the account DTO associated with the user
     */
    AccountDTO getAccountByUserId(Long userId);
    AccountDTO getAccountById(Long accountId);
    AccountDTO getAccountByAccountNumber(String accountNumber);
}
