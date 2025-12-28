package org.jala.university.application.service.impl;

import org.jala.university.application.dto.UserDTO;
import org.jala.university.application.map.UserMapper;
import org.jala.university.application.service.SignupService;
import org.jala.university.domain.entity.User;
import org.jala.university.domain.repository.UserRepository;
import org.jala.university.infrastructure.persistence.UserRepositoryImp;
import org.jala.university.infrastructure.config.JPAConfig;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 * Implementation of the SignupService that handles user registration operations.
 * This service is responsible for creating new user accounts and managing user data.
 * Implements AutoCloseable to ensure proper resource cleanup.
 *
 * @version 1.0
 * @see SignupService
 * @see UserRepository
 * @see UserMapper
 */
@Service
public class SignupServiceImpl implements SignupService, AutoCloseable {

    /** Repository for user data persistence operations */
    private final UserRepository userRepository;

    /** Mapper for converting between User entities and DTOs */
    private final UserMapper userMapper;

    /** Entity manager for handling JPA operations */
    private final EntityManager entityManager;

    /**
     * Constructs a new SignupServiceImp.
     * Initializes the required dependencies for user registration operations.
     */
    public SignupServiceImpl() {
        this.entityManager = JPAConfig.getEntityManagerFactory().createEntityManager();
        this.userRepository = new UserRepositoryImp(entityManager);
        this.userMapper = new UserMapper();
    }

    /**
     * Creates a new user account with the provided user data.
     * The password is hashed using BCrypt before storage.
     *
     * @param userDTO the user data transfer object containing registration information
     * @return UserDTO containing the saved user's information
     * @throws RuntimeException if there's an error during user creation
     */
    @Override
    public UserDTO save(UserDTO userDTO) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            User userMapped = userMapper.mapFrom(userDTO);

            String passwordHash = BCrypt.hashpw(userMapped.getPassword(), BCrypt.gensalt());
            userMapped.setPassword(passwordHash);

            entityManager.persist(userMapped);
            transaction.commit();

            return userMapper.mapTo(userMapped);
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error creating user account", e);
        }
    }

    /**
     * Finds a user by their email address.
     *
     * @param email the email address to search for
     * @return UserDTO containing the found user's information
     * @throws RuntimeException if no user is found with the given email
     */
    @Override
    public UserDTO findUserByEmail(String email) {
        try {
            User userFound = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));
            return userMapper.mapTo(userFound);
        } catch (Exception e) {
            throw new RuntimeException("Error finding user by email", e);
        }
    }

    /**
     * Finds a user by their CPF.
     *
     * @param cpf the CPF to search for
     * @return UserDTO containing the found user's information
     * @throws RuntimeException if no user is found with the given CPF
     */
    @Override
    public UserDTO findUserByCpf(String cpf) {
        try {
            User userFound = userRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("User with CPF " + cpf + " not found"));
            return userMapper.mapTo(userFound);
        } catch (RuntimeException e) {
            // Just rethrow the exception directly to preserve the original message
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error finding user by CPF", e);
        }
    }

    /**
     * Checks if an email is already registered.
     *
     * @param email the email to check
     * @return true if the email is already registered, false otherwise
     */
    @Override
    public boolean isEmailRegistered(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Checks if a CPF is already registered.
     *
     * @param cpf the CPF to check
     * @return true if the CPF is already registered, false otherwise
     */
    @Override
    public boolean isCpfRegistered(String cpf) {
        return userRepository.existsByCpf(cpf);
    }

    /**
     * Closes the entity manager and releases resources.
     * Should be called when the service is no longer needed.
     * This method is automatically called when using try-with-resources.
     */
    @Override
    public void close() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }
}