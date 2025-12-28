package org.jala.university.application.service;

import org.jala.university.application.dto.TransactionDetailsDTO;

/**
 * Service interface for retrieving transaction details.
 */
public interface TransactionDetailsService {
    
    /**
     * Gets the details of a transaction.
     * 
     * @param transactionId the transaction ID
     * @return the transaction details
     */
    TransactionDetailsDTO getTransactionDetails(Long transactionId);
    
    /**
     * Gets the details of a transaction for a specific account.
     * 
     * @param transactionId the transaction ID
     * @param accountId the account ID
     * @return the transaction details
     */
    TransactionDetailsDTO getTransactionDetails(Long transactionId, Long accountId);
}
