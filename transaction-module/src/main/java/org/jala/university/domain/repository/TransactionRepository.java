package org.jala.university.domain.repository;

import org.jala.university.commons.domain.Repository;
import org.jala.university.domain.entity.Transaction;
import org.jala.university.domain.entity.enums.TransactionStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Transaction entities.
 * Provides methods to query and manipulate transaction data.
 */
public interface TransactionRepository extends Repository<Transaction, Long> {
    
    /**
     * Finds all transactions where the specified user is either the sender or receiver.
     *
     * @param userId the ID of the user
     * @return a list of transactions involving the user
     */
    List<Transaction> findAllByUserId(Long userId);
    
    /**
     * Finds all transactions for a specific account (as sender or receiver).
     *
     * @param accountId the ID of the account
     * @return a list of transactions involving the account
     */
    List<Transaction> findByAccountId(Long accountId);
    
    /**
     * Finds transactions with a specific status.
     *
     * @param status the transaction status to filter by
     * @return a list of transactions with the specified status
     */
    List<Transaction> findByStatus(TransactionStatus status);
    
    /**
     * Finds transactions created between the specified date range.
     *
     * @param start the start date/time
     * @param end the end date/time
     * @return a list of transactions within the date range
     */
    List<Transaction> findByDateRange(LocalDateTime start, LocalDateTime end);
    
    /**
     * Finds transactions for an account with a specific status.
     *
     * @param accountId the ID of the account
     * @param status the transaction status to filter by
     * @return a list of transactions for the account with the specified status
     */
    List<Transaction> findByAccountIdAndStatus(Long accountId, TransactionStatus status);
    
    /**
     * Finds transactions for an account within a date range.
     *
     * @param accountId the ID of the account
     * @param start the start date/time
     * @param end the end date/time
     * @return a list of transactions for the account within the date range
     */
    List<Transaction> findByAccountIdAndDateRange(Long accountId, LocalDateTime start, LocalDateTime end);
    
    /**
     * Finds transactions for an account within a date range and with a specific status.
     *
     * @param accountId the ID of the account
     * @param start the start date/time
     * @param end the end date/time
     * @param status the transaction status to filter by
     * @return a list of transactions for the account within the date range and with the specified status
     */
    List<Transaction> findByAccountIdAndDateRangeAndStatus(
            Long accountId, LocalDateTime start, LocalDateTime end, TransactionStatus status);
}
