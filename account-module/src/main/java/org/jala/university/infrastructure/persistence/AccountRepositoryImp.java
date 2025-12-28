package org.jala.university.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.Account;
import org.jala.university.domain.repository.AccountRepository;
import org.jala.university.infrastructure.config.JPAConfig;

public class AccountRepositoryImp extends CrudRepository<Account, Long> implements AccountRepository {

    private final EntityManager entityManager = JPAConfig.getEntityManagerFactory().createEntityManager();

    public AccountRepositoryImp(EntityManager entityManager) {
        super(Account.class, entityManager);
    }

    @Override
    public Account findAccountByUserId(Long userId) {
        String jpql = "SELECT a FROM Account a WHERE a.user.id = :userId";
        return entityManager.createQuery(jpql, Account.class).setParameter("userId", userId).getSingleResult();
    }

    @Override
    public Account findByAccountId(Long accountId) {
        String jpql = "SELECT a FROM Account a WHERE a.id = :accountId";
        return entityManager.createQuery(jpql, Account.class).setParameter("accountId", accountId).getSingleResult();
    }

    @Override
    public Account findByAccountNumber(String accountNumber) {
        String jpql = "SELECT a FROM Account a WHERE a.accountNumber = :accountNumber";
        return entityManager.createQuery(jpql, Account.class)
                .setParameter("accountNumber", accountNumber)
                .getSingleResult();
    }
}
