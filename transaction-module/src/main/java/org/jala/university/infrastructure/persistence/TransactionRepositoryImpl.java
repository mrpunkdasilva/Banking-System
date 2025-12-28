package org.jala.university.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.Transaction;
import org.jala.university.domain.entity.enums.TransactionStatus;
import org.jala.university.domain.repository.TransactionRepository;
import org.jala.university.infrastructure.config.JPAConfig;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the TransactionRepository interface.
 * Provides concrete implementations for transaction-related database operations.
 */
public class TransactionRepositoryImpl extends CrudRepository<Transaction, Long> implements TransactionRepository {
    
    private final EntityManager entityManager;
    
    /**
     * Constructs a new TransactionRepositoryImpl with the provided EntityManager.
     *
     * @param entityManager the JPA EntityManager to use
     */
    public TransactionRepositoryImpl(EntityManager entityManager) {
        super(Transaction.class, entityManager);
        this.entityManager = entityManager;
    }
    
    /**
     * Default constructor that creates an EntityManager from the JPAConfig.
     */
    public TransactionRepositoryImpl() {
        this(JPAConfig.getEntityManagerFactory().createEntityManager());
    }
    
    @Override
    public List<Transaction> findAllByUserId(Long userId) {
        try {
            String jpql = "SELECT t FROM Transaction t " +
                          "JOIN t.sender s JOIN s.user su " +
                          "JOIN t.receiver r JOIN r.user ru " +
                          "WHERE su.id = :userId OR ru.id = :userId " +
                          "ORDER BY t.createdAt DESC";
            
            TypedQuery<Transaction> query = entityManager.createQuery(jpql, Transaction.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Transaction> findByAccountId(Long accountId) {
        try {
            String jpql = "SELECT t FROM Transaction t " +
                          "WHERE t.sender.id = :accountId OR t.receiver.id = :accountId " +
                          "ORDER BY t.createdAt DESC";
            
            TypedQuery<Transaction> query = entityManager.createQuery(jpql, Transaction.class);
            query.setParameter("accountId", accountId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Transaction> findByStatus(TransactionStatus status) {
        try {
            String jpql = "SELECT t FROM Transaction t WHERE t.status = :status ORDER BY t.createdAt DESC";
            TypedQuery<Transaction> query = entityManager.createQuery(jpql, Transaction.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Transaction> findByDateRange(LocalDateTime start, LocalDateTime end) {
        try {
            String jpql = "SELECT t FROM Transaction t " +
                          "WHERE t.createdAt BETWEEN :start AND :end " +
                          "ORDER BY t.createdAt DESC";
            
            TypedQuery<Transaction> query = entityManager.createQuery(jpql, Transaction.class);
            query.setParameter("start", start);
            query.setParameter("end", end);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Transaction> findByAccountIdAndStatus(Long accountId, TransactionStatus status) {
        try {
            String jpql = "SELECT t FROM Transaction t " +
                          "WHERE (t.sender.id = :accountId OR t.receiver.id = :accountId) " +
                          "AND t.status = :status " +
                          "ORDER BY t.createdAt DESC";
            
            TypedQuery<Transaction> query = entityManager.createQuery(jpql, Transaction.class);
            query.setParameter("accountId", accountId);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Transaction> findByAccountIdAndDateRange(Long accountId, LocalDateTime start, LocalDateTime end) {
        try {
            String jpql = "SELECT t FROM Transaction t " +
                          "WHERE (t.sender.id = :accountId OR t.receiver.id = :accountId) " +
                          "AND t.createdAt BETWEEN :start AND :end " +
                          "ORDER BY t.createdAt DESC";
            
            TypedQuery<Transaction> query = entityManager.createQuery(jpql, Transaction.class);
            query.setParameter("accountId", accountId);
            query.setParameter("start", start);
            query.setParameter("end", end);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Transaction> findByAccountIdAndDateRangeAndStatus(
            Long accountId, LocalDateTime start, LocalDateTime end, TransactionStatus status) {
        try {
            String jpql = "SELECT t FROM Transaction t " +
                          "WHERE (t.sender.id = :accountId OR t.receiver.id = :accountId) " +
                          "AND t.createdAt BETWEEN :start AND :end " +
                          "AND t.status = :status " +
                          "ORDER BY t.createdAt DESC";
            
            TypedQuery<Transaction> query = entityManager.createQuery(jpql, Transaction.class);
            query.setParameter("accountId", accountId);
            query.setParameter("start", start);
            query.setParameter("end", end);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
