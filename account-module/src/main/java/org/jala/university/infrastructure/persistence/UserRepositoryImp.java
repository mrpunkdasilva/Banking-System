package org.jala.university.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.jala.university.domain.entity.User;
import org.jala.university.domain.repository.UserRepository;
import org.jala.university.infrastructure.config.JPAConfig;

import java.util.Optional;

/**
 * Implementation of the UserRepository interface.
 * Handles database operations for User entities.
 */
public class UserRepositoryImp implements UserRepository {

    private final EntityManager entityManager;

    /**
     * Constructor with entity manager parameter.
     * 
     * @param entityManager the entity manager to use
     */
    public UserRepositoryImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    /**
     * Default constructor that creates a new entity manager.
     */
    public UserRepositoryImp() {
        this.entityManager = JPAConfig.getEntityManager();
    }

    /**
     * Saves a user to the database.
     * 
     * @param user the user to save
     * @return the saved user
     */
    @Override
    public User save(User user) {
        entityManager.persist(user);
        return user;
    }

    /**
     * Finds a user by ID.
     * 
     * @param id the ID to search for
     * @return the found user or null if not found
     */
    @Override
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    /**
     * Finds a user by email.
     * 
     * @param email the email to search for
     * @return an Optional containing the found user or empty if not found
     */
    @Override
    public Optional<User> findByEmail(String email) {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * Finds a user by CPF.
     * 
     * @param cpf the CPF to search for
     * @return an Optional containing the found user or empty if not found
     */
    @Override
    public Optional<User> findByCpf(String cpf) {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.cpf = :cpf", User.class);
            query.setParameter("cpf", cpf);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    
    /**
     * Finds a user by account ID.
     * 
     * @param accountId the account ID to search for
     * @return an Optional containing the found user or empty if not found
     */
    @Override
    public Optional<User> findByAccountId(Long accountId) {
        try {
            // Verificar se a transação está ativa
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            
            // Consulta direta para obter o usuário associado à conta
            TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u JOIN u.accounts a WHERE a.id = :accountId", User.class);
            query.setParameter("accountId", accountId);
            
            User result = query.getSingleResult();
            
            // Commit da transação se foi iniciada aqui
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().commit();
            }
            
            return Optional.ofNullable(result);
        } catch (NoResultException e) {
            // Rollback da transação se foi iniciada aqui e ocorreu erro
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.out.println("Nenhum usuário encontrado para o accountId: " + accountId);
            return Optional.empty();
        } catch (Exception e) {
            // Rollback da transação se foi iniciada aqui e ocorreu erro
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.out.println("Erro ao buscar usuário por accountId: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Checks if a user with the given email exists.
     * 
     * @param email the email to check
     * @return true if a user with the email exists, false otherwise
     */
    @Override
    public boolean existsByEmail(String email) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }

    /**
     * Checks if a user with the given CPF exists.
     * 
     * @param cpf the CPF to check
     * @return true if a user with the CPF exists, false otherwise
     */
    @Override
    public boolean existsByCpf(String cpf) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(u) FROM User u WHERE u.cpf = :cpf", Long.class);
        query.setParameter("cpf", cpf);
        return query.getSingleResult() > 0;
    }
}
