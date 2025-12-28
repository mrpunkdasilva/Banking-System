package org.jala.university.application.service.impl;

import org.jala.university.application.dto.TransactionDetailsDTO;
import org.jala.university.application.service.TransactionDetailsService;
import org.jala.university.domain.entity.Transaction;
import org.jala.university.domain.repository.TransactionRepository;
import org.jala.university.infrastructure.persistence.TransactionRepositoryImpl;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Implementation of the TransactionDetailsService.
 */
public class TransactionDetailsServiceImpl implements TransactionDetailsService {
    
    private final TransactionRepository transactionRepository;
    private final DateTimeFormatter dateFormatter;
    private final NumberFormat currencyFormatter;
    
    /**
     * Constructor initializing the required dependencies.
     */
    public TransactionDetailsServiceImpl() {
        this.transactionRepository = new TransactionRepositoryImpl();
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    }
    
    /**
     * Constructor with repository parameter.
     * 
     * @param transactionRepository the transaction repository to use
     */
    public TransactionDetailsServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    }
    
    /**
     * Gets the details of a transaction.
     * 
     * @param transactionId the transaction ID
     * @return the transaction details
     */
    @Override
    public TransactionDetailsDTO getTransactionDetails(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId);
        
        if (transaction == null) {
            return null;
        }
        
        String senderName = transaction.getSender().getUser().getName();
        String senderAccount = transaction.getSender().getAccountNumber();
        
        String receiverName = transaction.getReceiver().getUser().getName();
        String receiverAccount = transaction.getReceiver().getAccountNumber();
        
        return TransactionDetailsDTO.builder()
                .id(transaction.getId())
                .createdAt(transaction.getCreatedAt())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .status(transaction.getStatus())
                .senderName(senderName)
                .senderAccountNumber(senderAccount)
                .receiverName(receiverName)
                .receiverAccountNumber(receiverAccount)
                .build();
    }
    
    /**
     * Gets the details of a transaction for a specific account.
     * 
     * @param transactionId the transaction ID
     * @param accountId the account ID
     * @return the transaction details
     */
    @Override
    public TransactionDetailsDTO getTransactionDetails(Long transactionId, Long accountId) {
        Transaction transaction = transactionRepository.findById(transactionId);
        
        if (transaction == null) {
            return null;
        }
        
        // Verificar se a conta está envolvida na transação
        boolean isAccountInvolved = transaction.getSender().getId().equals(accountId) || 
                                   transaction.getReceiver().getId().equals(accountId);
        
        if (!isAccountInvolved) {
            return null; // A conta não está envolvida nesta transação
        }
        
        String senderName = transaction.getSender().getUser().getName();
        String senderAccount = transaction.getSender().getAccountNumber();
        
        String receiverName = transaction.getReceiver().getUser().getName();
        String receiverAccount = transaction.getReceiver().getAccountNumber();
        
        boolean isDebit = transaction.getSender().getId().equals(accountId);
        
        return TransactionDetailsDTO.builder()
                .id(transaction.getId())
                .createdAt(transaction.getCreatedAt())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .status(transaction.getStatus())
                .senderName(senderName)
                .senderAccountNumber(senderAccount)
                .receiverName(receiverName)
                .receiverAccountNumber(receiverAccount)
                .isDebit(isDebit)
                .build();
    }
}