package org.jala.university.domain.entity.enums;

/**
 * Enumeration representing the possible states of a financial transaction.
 * Used to track the lifecycle of transactions in the system.
 */
public enum TransactionStatus {
    /**
     * Transaction has been initiated but not yet processed
     */
    PENDING,

    /**
     * Transaction has been successfully processed and completed
     */
    COMPLETED,

    /**
     * Transaction has failed during processing
     */
    FAILED,

    /**
     * Transaction has been cancelled by the user or system
     */
    CANCELLED
}
