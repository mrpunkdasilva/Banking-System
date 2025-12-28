package org.jala.university.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.jala.university.domain.entity.Beneficiary;
import org.jala.university.domain.repository.BeneficiaryRepository;
import org.jala.university.infrastructure.config.JPAConfig;

import java.util.List;
import java.util.Optional;

public class BeneficiaryRepositoryImp implements BeneficiaryRepository {

    private final EntityManager entityManager;

    public BeneficiaryRepositoryImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public BeneficiaryRepositoryImp() {
        this(JPAConfig.getEntityManagerFactory().createEntityManager());
    }

    @Override
    public void addBeneficiary(Beneficiary beneficiary) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(beneficiary);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public void removeBeneficiary(Long beneficiaryId) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Beneficiary beneficiary = entityManager.find(Beneficiary.class, beneficiaryId);
            if (beneficiary != null) {
                entityManager.remove(beneficiary);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Beneficiary> listBeneficiaries() {
        TypedQuery<Beneficiary> query = entityManager.createQuery("SELECT b FROM Beneficiary b", Beneficiary.class);
        return query.getResultList();
    }

    @Override
    public boolean existsBeneficiary(String document, String accountNumber, Long accountId) {
        TypedQuery<Long> query = entityManager.createQuery(
                """
                        SELECT
                            COUNT(b)
                        FROM Beneficiary b
                        JOIN b.accountBeneficiaries ab
                        WHERE
                            (b.document = :document OR b.accountNumber = :accountNumber) AND
                            ab.account.id = :accountId
                        """,
                Long.class);
        query.setParameter("document", document);
        query.setParameter("accountNumber", accountNumber);
        query.setParameter("accountId", accountId);
        return query.getSingleResult() > 0;
    }

    @Override
    public Optional<Beneficiary> findBeneficiaryById(Long beneficiaryId) {
        return Optional.ofNullable(entityManager.find(Beneficiary.class, beneficiaryId));
    }

    @Override
    public void updateBeneficiary(Beneficiary beneficiary) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(beneficiary);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public boolean existsOtherBeneficiaryWithSameData(
            Long currentBeneficiaryId, String document, String accountNumber
    ) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(b) FROM Beneficiary b WHERE b.id != :id AND (b.document = :document OR b.accountNumber = :accountNumber)",
                Long.class);
        query.setParameter("id", currentBeneficiaryId);
        query.setParameter("document", document);
        query.setParameter("accountNumber", accountNumber);
        return query.getSingleResult() > 0;
    }

    @Override
    public List<Beneficiary> findBeneficiariesByAccountId(Long accountId) {
        TypedQuery<Beneficiary> query = entityManager.createQuery(
                "SELECT b FROM Beneficiary b JOIN b.accountBeneficiaries ab WHERE ab.account.id = :accountId",
                Beneficiary.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }
}
