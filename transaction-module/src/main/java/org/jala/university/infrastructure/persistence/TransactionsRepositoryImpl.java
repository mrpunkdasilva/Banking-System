package org.jala.university.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.Transaction;
import org.jala.university.domain.repository.TransactionsRepository;
import org.jala.university.infrastructure.config.JPAConfig;

import java.util.List;

public class TransactionsRepositoryImpl extends CrudRepository<Transaction, Long> implements TransactionsRepository {
    private final EntityManager entityManager;
     public TransactionsRepositoryImpl(EntityManager entityManager) {
        super(Transaction.class, entityManager);
        this.entityManager = JPAConfig.getEntityManagerFactory().createEntityManager();
    }

    public List<Transaction> findAllByUserId(Long userId) {
        String jpql = "SELECT t FROM Transaction t WHERE t.receiver.id = :userId OR t.sender.id = :userId";
        TypedQuery<Transaction> query = entityManager.createQuery(jpql, Transaction.class);
        query.setParameter("userId", userId);
        try {
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
