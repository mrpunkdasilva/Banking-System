package org.jala.university.application.service.interfaces;

import org.jala.university.application.dto.UserDTO;

/**
 * Service interface for user-related operations.
 */
public interface UserService {
    /**
     * Find a user by ID.
     *
     * @param id the user ID
     * @return the user DTO
     */
    UserDTO findById(Long id);
    
    /**
     * Update a user.
     *
     * @param id the user ID
     * @param userDTO the updated user data
     */
    void update(Long id, UserDTO userDTO);
    
    /**
     * Get the user's name by account ID.
     *
     * @param accountId the account ID
     * @return the user's name or null if not found
     */
    String getUserNameByAccountId(Long accountId);
}
