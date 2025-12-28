
package org.jala.university.domain.repository;

import org.jala.university.domain.entity.User;

import java.util.Optional;

/**
 * Repository interface for User entities.
 */
public interface UserRepository {
    
    /**
     * Saves a user to the database.
     * 
     * @param user the user to save
     * @return the saved user
     */
    User save(User user);
    
    /**
     * Finds a user by ID.
     * 
     * @param id the ID to search for
     * @return the found user or null if not found
     */
    User findById(Long id);
    
    /**
     * Finds a user by email.
     * 
     * @param email the email to search for
     * @return an Optional containing the found user or empty if not found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Finds a user by CPF.
     * 
     * @param cpf the CPF to search for
     * @return an Optional containing the found user or empty if not found
     */
    Optional<User> findByCpf(String cpf);
    
    /**
     * Finds a user by account ID.
     * 
     * @param accountId the account ID to search for
     * @return an Optional containing the found user or empty if not found
     */
    Optional<User> findByAccountId(Long accountId);
    
    /**
     * Checks if a user with the given email exists.
     * 
     * @param email the email to check
     * @return true if a user with the email exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Checks if a user with the given CPF exists.
     * 
     * @param cpf the CPF to check
     * @return true if a user with the CPF exists, false otherwise
     */
    boolean existsByCpf(String cpf);
}
