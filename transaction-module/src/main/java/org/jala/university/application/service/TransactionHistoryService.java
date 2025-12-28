package org.jala.university.application.service;

import org.jala.university.application.dto.TransactionHistoryDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for retrieving transaction history data.
 */
public interface TransactionHistoryService {
    
    /**
     * Retrieves the transaction history for a specific account.
     *
     * @param accountId the ID of the account
     * @return a list of transaction history DTOs
     */
    List<TransactionHistoryDTO> getAccountTransactionHistory(Long accountId);
    
    /**
     * Retrieves transactions for the specified account within a date range.
     *
     * @param accountId the ID of the account
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return a list of transaction history DTOs
     */
    List<TransactionHistoryDTO> getAccountTransactionHistory(Long accountId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Retrieves transactions for the specified account with a specific status.
     *
     * @param accountId the ID of the account
     * @param status the transaction status (e.g., "PENDING", "COMPLETED", "FAILED", "CANCELLED")
     * @return a list of transaction history DTOs
     */
    List<TransactionHistoryDTO> getAccountTransactionHistoryByStatus(Long accountId, String status);
    
    /**
     * Retrieves transactions for the specified account within a date range and with a specific status.
     *
     * @param accountId the ID of the account
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @param status the transaction status (e.g., "PENDING", "COMPLETED", "FAILED", "CANCELLED")
     * @return a list of transaction history DTOs
     */
    List<TransactionHistoryDTO> getAccountTransactionHistoryByDateRangeAndStatus(
            Long accountId, LocalDate startDate, LocalDate endDate, String status);
}
