package org.jala.university.application.service.impl;

import jakarta.persistence.EntityManager;
import org.jala.university.application.dto.AccountDTO;
import org.jala.university.application.dto.TransactionDTO;
import org.jala.university.application.map.TransactionMapper;
import org.jala.university.application.service.TransactionsService;
import org.jala.university.application.service.implementations.AccountServiceImpl;
import org.jala.university.application.service.implementations.UserServiceImpl;
import org.jala.university.application.service.interfaces.AccountService;
import org.jala.university.application.service.interfaces.UserService;
import org.jala.university.domain.entity.Transaction;
import org.jala.university.domain.entity.enums.TransactionStatus;
import org.jala.university.domain.repository.TransactionsRepository;
import org.jala.university.infrastructure.config.JPAConfig;
import org.jala.university.infrastructure.persistence.TransactionsRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the TransactionsService interface that provides functionality
 * for managing financial transactions between user accounts.
 * <p>
 * This service handles operations such as:
 * <ul>
 *     <li>Retrieving transactions for a specific user or all transactions</li>
 *     <li>Finding transactions by ID</li>
 *     <li>Processing new transactions including balance verification</li>
 *     <li>Deleting transactions</li>
 * </ul>
 * <p>
 * The service uses a repository pattern for data access and implements
 * transaction management to ensure data consistency during financial operations.
 */
@Service
public class TransactionsServiceImpl implements TransactionsService {
    /**
     * Repository for accessing and manipulating transaction data in the database.
     */
    private final TransactionsRepository transactionsRepository;

    /**
     * Mapper for converting between Transaction entities and TransactionDTO objects.
     */
    private final TransactionMapper mapper;

    /**
     * Service for user-related operations.
     */
    private final UserService userService;

    private final EntityManager entityManager = JPAConfig.getEntityManagerFactory().createEntityManager();

    /**
     * Service for account-related operations, used for balance checks and updates.
     */
    private final AccountService accountService;

    /**
     * Constructs a new TransactionsServiceImpl with all required dependencies.
     * <p>
     * Initializes the transaction mapper, entity manager, transactions repository,
     * user service, and account service.
     */
    public TransactionsServiceImpl() {
        this.mapper = new TransactionMapper();
        this.transactionsRepository = new TransactionsRepositoryImpl(entityManager);
        this.userService = new UserServiceImpl();
        this.accountService = new AccountServiceImpl();
    }

    /**
     * Retrieves all transactions for a specific user.
     *
     * @param userId The ID of the user whose transactions are to be retrieved
     * @return A list of TransactionDTO objects representing the user's transactions
     */
    @Override
    public List<TransactionDTO> findAll(Long userId) {
        return this.transactionsRepository.findAllByUserId(userId)
                .stream()
                .map(this.mapper::mapTo)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all transactions in the system.
     *
     * @return A list of all TransactionDTO objects in the system
     */
    @Override
    public List<TransactionDTO> findAll() {
        return this.transactionsRepository.findAll()
                .stream()
                .map(this.mapper::mapTo)
                .collect(Collectors.toList());
    }

    /**
     * Finds a transaction by its ID.
     *
     * @param id The ID of the transaction to find
     * @return The TransactionDTO object representing the found transaction
     */
    @Override
    public TransactionDTO findById(Long id) {
        Transaction transaction = this.transactionsRepository.findById(id);
        return this.mapper.mapTo(transaction);
    }

    /**
     * Processes and saves a new transaction.
     * <p>
     * This method handles the entire transaction process including:
     * <ul>
     *     <li>Checking if the transaction is scheduled for today or a future date</li>
     *     <li>Verifying the sender has sufficient balance</li>
     *     <li>Transferring funds between accounts</li>
     *     <li>Setting appropriate transaction status based on the outcome</li>
     * </ul>
     * <p>
     * The method uses SERIALIZABLE isolation level to ensure data consistency
     * during the transaction process.
     *
     * @param transaction The TransactionDTO containing transaction details
     * @return true if the transaction was processed successfully, false otherwise
     * @throws RuntimeException if an error occurs during transaction processing
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean save(TransactionDTO transaction) {
        Transaction transactionConverted = this.mapper.mapFrom(transaction);
        if (!isTransactionForToday(transactionConverted.getTransactionSchedule())) {
            transactionConverted.setStatus(TransactionStatus.PENDING);
            this.transactionsRepository.save(transactionConverted);
            return true;
        }

        transactionConverted.setStatus(TransactionStatus.PENDING);

        try {
            Long senderId = transactionConverted.getSender().getId();
            Long receiverId = transactionConverted.getReceiver().getId();

            AccountDTO senderAccount = this.getAccountByUserId(senderId);
            AccountDTO receiverAccount = this.getAccountByUserId(receiverId);
            boolean isSenderAvailableToSend = checkSenderBalance(transactionConverted.getAmount(), senderAccount.getBalance());
            if (!isSenderAvailableToSend) {
                transactionConverted.setStatus(TransactionStatus.CANCELLED);
                this.transactionsRepository.save(transactionConverted);
                return false;
            }

            BigDecimal currentSenderBalance = senderAccount.getBalance();
            BigDecimal newSenderBalance = currentSenderBalance.subtract(transactionConverted.getAmount());
            this.updateUserAccountBalance(senderId, newSenderBalance);

            BigDecimal newReceiverBalance = receiverAccount.getBalance().add(transactionConverted.getAmount());
            this.updateUserAccountBalance(receiverId, newReceiverBalance);

            transactionConverted.setStatus(TransactionStatus.COMPLETED);
            this.transactionsRepository.save(transactionConverted);

            return true;
        } catch (RuntimeException e) {
            transactionConverted.setStatus(TransactionStatus.FAILED);
            this.transactionsRepository.save(transactionConverted);
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a transaction by its ID.
     *
     * @param id The ID of the transaction to delete
     * @return true if the transaction was successfully deleted, false otherwise
     */
    @Override
    public boolean delete(Long id) {
        try {
            this.transactionsRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if a transaction is scheduled for the current date.
     *
     * @param transactionDate The scheduled date of the transaction
     * @return true if the transaction is scheduled for today, false otherwise
     */
    private boolean isTransactionForToday(LocalDate transactionDate) {
        return transactionDate.equals(LocalDate.now());
    }

    /**
     * Verifies if the sender has sufficient balance to complete the transaction.
     *
     * @param amount The amount to be transferred
     * @param balance The current balance of the sender
     * @return true if the sender has sufficient balance, false otherwise
     */
    private boolean checkSenderBalance(BigDecimal amount, BigDecimal balance) {
        return balance.compareTo(amount) >= 0;
    }

    /**
     * Retrieves the account information for a specific user.
     *
     * @param userId The ID of the user whose account information is needed
     * @return The AccountDTO containing the user's account information
     */
    private AccountDTO getAccountByUserId(Long userId) {
        return this.accountService.getAccountByUserId(userId);
    }

    /**
     * Updates the balance of a user's account.
     *
     * @param userId The ID of the user whose account balance is to be updated
     * @param newBalance The new balance to set for the user's account
     */
    private void updateUserAccountBalance(Long userId, BigDecimal newBalance) {
        this.accountService.updateUserAccountBalance(userId, newBalance);
    }
}
