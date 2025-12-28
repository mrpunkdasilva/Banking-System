package org.jala.university.application.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.jala.university.domain.entity.enums.TransactionStatus;
import org.jala.university.application.dto.TransactionHistoryDTO;
import org.jala.university.application.map.TransactionHistoryMapper;
import org.jala.university.application.service.TransactionHistoryService;
import org.jala.university.domain.entity.Transaction;
import org.jala.university.domain.repository.TransactionRepository;
import org.jala.university.infrastructure.config.JPAConfig;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the TransactionHistoryService.
 * Uses a database view approach to efficiently retrieve transaction history.
 */
public class TransactionHistoryServiceImpl implements TransactionHistoryService {
    
    private final TransactionRepository transactionRepository;
    private final EntityManager entityManager;
    
    public TransactionHistoryServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        this.entityManager = JPAConfig.getEntityManagerFactory().createEntityManager();
    }
    
    @Override
    public List<TransactionHistoryDTO> getAccountTransactionHistory(Long accountId) {
        try {
            // Usando JPQL para criar uma "view" virtual
            String jpql = "SELECT t FROM Transaction t " +
                          "WHERE t.sender.id = :accountId OR t.receiver.id = :accountId " +
                          "ORDER BY t.createdAt DESC";
            
            TypedQuery<Transaction> query = entityManager.createQuery(jpql, Transaction.class);
            query.setParameter("accountId", accountId);
            
            List<Transaction> transactions = query.getResultList();
            
            TransactionHistoryMapper mapper = new TransactionHistoryMapper(accountId);
            return transactions.stream()
                    .map(mapper::mapTo)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<TransactionHistoryDTO> getAccountTransactionHistory(Long accountId, LocalDate startDate, LocalDate endDate) {
        try {
            // Convertendo LocalDate para LocalDateTime para incluir todo o dia
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            
            String jpql = "SELECT t FROM Transaction t " +
                          "WHERE (t.sender.id = :accountId OR t.receiver.id = :accountId) " +
                          "AND t.createdAt BETWEEN :startDate AND :endDate " +
                          "ORDER BY t.createdAt DESC";
            
            TypedQuery<Transaction> query = entityManager.createQuery(jpql, Transaction.class);
            query.setParameter("accountId", accountId);
            query.setParameter("startDate", startDateTime);
            query.setParameter("endDate", endDateTime);
            
            List<Transaction> transactions = query.getResultList();
            
            TransactionHistoryMapper mapper = new TransactionHistoryMapper(accountId);
            return transactions.stream()
                    .map(mapper::mapTo)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<TransactionHistoryDTO> getAccountTransactionHistoryByStatus(Long accountId, String statusStr) {
        try {
            TransactionStatus status =
                TransactionStatus.valueOf(statusStr.toUpperCase());
            
            String jpql = "SELECT t FROM Transaction t " +
                          "WHERE (t.sender.id = :accountId OR t.receiver.id = :accountId) " +
                          "AND t.status = :status " +
                          "ORDER BY t.createdAt DESC";
            
            TypedQuery<Transaction> query = entityManager.createQuery(jpql, Transaction.class);
            query.setParameter("accountId", accountId);
            query.setParameter("status", status);
            
            List<Transaction> transactions = query.getResultList();
            
            TransactionHistoryMapper mapper = new TransactionHistoryMapper(accountId);
            return transactions.stream()
                    .map(mapper::mapTo)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            // Status de transação inválido
            e.printStackTrace();
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<TransactionHistoryDTO> getAccountTransactionHistoryByDateRangeAndStatus(
            Long accountId, LocalDate startDate, LocalDate endDate, String statusStr) {
        try {
            // Convertendo LocalDate para LocalDateTime para incluir todo o dia
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            
            TransactionStatus status =
                TransactionStatus.valueOf(statusStr.toUpperCase());
            
            // Usando o repositório para buscar as transações
            List<Transaction> transactions = transactionRepository.findByAccountIdAndDateRangeAndStatus(
                accountId, startDateTime, endDateTime, status);
            
            TransactionHistoryMapper mapper = new TransactionHistoryMapper(accountId);
            return transactions.stream()
                    .map(mapper::mapTo)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            // Status de transação inválido
            e.printStackTrace();
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Service
    @RequiredArgsConstructor
    public static class TransactionService {

        private final org.jala.university.domain.repositories.TransactionRepository transactionRepository;

        /**
         * Busca as transações recentes de uma conta (últimas 10)
         */
        public List<Transaction> getRecentTransactions(Long accountId) {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
            return transactionRepository.findByAccountId(accountId, pageable);
        }

        /**
         * Busca todas as transações de uma conta
         */
        public List<Transaction> getAllTransactionsByAccount(Long accountId) {
            return transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId);
        }

        /**
         * Busca transações por período
         */
        public List<Transaction> getTransactionsByPeriod(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
            return transactionRepository.findByAccountIdAndCreatedAtBetween(accountId, startDate, endDate);
        }

        /**
         * Busca transações recentes com limite personalizado
         */
        public List<Transaction> getRecentTransactions(Long accountId, int limit) {
            Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());
            return transactionRepository.findByAccountId(accountId, pageable);
        }
    }
}
