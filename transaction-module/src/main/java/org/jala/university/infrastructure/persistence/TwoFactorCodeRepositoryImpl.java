package org.jala.university.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.TwoFactorCode;
import org.jala.university.domain.entity.User;
import org.jala.university.domain.repository.TwoFactorCodeRepository;
import org.jala.university.infrastructure.config.JPAConfig;

import java.time.LocalDateTime;

public class TwoFactorCodeRepositoryImpl extends CrudRepository<TwoFactorCode, Long> implements TwoFactorCodeRepository {

    private final EntityManager entityManager;

    public TwoFactorCodeRepositoryImpl(EntityManager entityManager) {
        super(TwoFactorCode.class, entityManager);
        this.entityManager = entityManager;
    }

    public TwoFactorCodeRepositoryImpl() {
        this(JPAConfig.getEntityManagerFactory().createEntityManager());
    }

    @Override
    public TwoFactorCode findLatestActiveByUser(User user) {
        try {
            String jpql = "SELECT t FROM TwoFactorCode t WHERE t.user = :user AND t.expiresAt > :now ORDER BY t.createdAt DESC";
            TypedQuery<TwoFactorCode> query = entityManager.createQuery(jpql, TwoFactorCode.class);
            query.setParameter("user", user);
            query.setParameter("now", LocalDateTime.now());
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public TwoFactorCode findByCodeAndUser(String code, User user) {
        try {
            String jpql = "SELECT t FROM TwoFactorCode t WHERE t.code = :code AND t.user = :user AND t.expiresAt > :now";
            TypedQuery<TwoFactorCode> query = entityManager.createQuery(jpql, TwoFactorCode.class);
            query.setParameter("code", code);
            query.setParameter("user", user);
            query.setParameter("now", LocalDateTime.now());
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void invalidateAllActiveCodesForUser(User user) {
        entityManager.getTransaction().begin();
        String jpql = "UPDATE TwoFactorCode t SET t.used = true WHERE t.user = :user AND t.used = false";
        entityManager.createQuery(jpql)
                .setParameter("user", user)
                .executeUpdate();
        entityManager.getTransaction().commit();
    }
    
    @Override
    public TwoFactorCode findLatestValidCode(Long userId) {
        try {
            String jpql = "SELECT t FROM TwoFactorCode t WHERE t.user.id = :userId AND t.expiresAt > :now ORDER BY t.createdAt DESC";
            TypedQuery<TwoFactorCode> query = entityManager.createQuery(jpql, TwoFactorCode.class);
            query.setParameter("userId", userId);
            query.setParameter("now", LocalDateTime.now());
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}