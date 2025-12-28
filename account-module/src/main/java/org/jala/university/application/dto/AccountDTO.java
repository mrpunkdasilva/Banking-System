package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jala.university.domain.entity.enums.AccountType;

import java.math.BigDecimal;

/**
 * Data Transfer Object representing a bank account.
 * This class encapsulates account information for transfer between layers.
 */
@Builder
@Getter
@Setter
public class AccountDTO {

    private final short MAX_LENGTH_AGENCY = 4;
    private final short MAX_LENGTH_ACCOUNT_NUMBER = 8;

    /**
     * Unique identifier for the account
     */
    private Long id;

    /**
     * ID of the user who owns this account
     */
    private Long userId;

    /**
     * Bank agency number (4 digits)
     */
    private String agency;

    /**
     * Account number (8 digits)
     */
    private String accountNumber;

    /**
     * Type of account (CHECKING or SAVINGS)
     * @see AccountType
     */
    private AccountType accountType;

    /**
     * Current balance of the account
     * Represented with precision of 2 decimal places
     */
    private BigDecimal balance;

    /**
     * Returns a string representation of the account details.
     * Masks sensitive information for security.
     *
     * @return formatted string with account information
     */
    @Override
    public String toString() {
        return String.format(
            "AccountDTO(id=%d, userId=%d, agency=****%s, accountNumber=****%s, type=%s, balance=%s)",
            id,
            userId,
            maskString(agency),
            maskString(accountNumber),
            accountType,
            balance
        );
    }

    /**
     * Returns the last 4 characters of a string, if available.
     * Used for masking sensitive account information.
     *
     * @param input the string to mask
     * @return last 4 characters of the input, or full string if less than 4 characters
     */
    private String maskString(String input) {
        if (input == null || input.length() <= MAX_LENGTH_AGENCY) {
            return input;
        }
        return input.substring(input.length() - MAX_LENGTH_AGENCY);
    }
}
