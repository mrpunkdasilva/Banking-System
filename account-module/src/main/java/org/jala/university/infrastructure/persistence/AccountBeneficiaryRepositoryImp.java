package org.jala.university.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import org.jala.university.domain.entity.AccountBeneficiary;
import org.jala.university.domain.entity.AccountBeneficiaryId;
import org.jala.university.domain.repository.AccountBeneficiaryRepository;
import org.jala.university.infrastructure.config.JPAConfig;

public class AccountBeneficiaryRepositoryImp implements AccountBeneficiaryRepository {

    private final EntityManager entityManager;

    public AccountBeneficiaryRepositoryImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public AccountBeneficiaryRepositoryImp() {
        this(JPAConfig.getEntityManagerFactory().createEntityManager());
    }

    @Override
    public AccountBeneficiary findById(AccountBeneficiaryId id) {
        return entityManager.find(AccountBeneficiary.class, id);
    }

    @Override
    public void save(AccountBeneficiary accountBeneficiary) {
        try {
            entityManager.getTransaction().begin();
            
            if (entityManager.find(AccountBeneficiary.class, accountBeneficiary.getId()) == null) {
                entityManager.persist(accountBeneficiary);
            } else {
                entityManager.merge(accountBeneficiary);
            }
            
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Erro ao salvar AccountBeneficiary: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void delete(AccountBeneficiaryId id) {
        try {
            entityManager.getTransaction().begin();
            AccountBeneficiary accountBeneficiary = entityManager.find(AccountBeneficiary.class, id);
            if (accountBeneficiary != null) {
                entityManager.remove(accountBeneficiary);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }
    }
}
