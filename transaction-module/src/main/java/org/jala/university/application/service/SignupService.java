package org.jala.university.application.service;

import org.jala.university.application.dto.UserDTO;

/**
 * Service interface for user registration operations.
 */
public interface SignupService {
    
    /**
     * Saves a new user to the database.
     * 
     * @param userDTO the user data to save
     * @return the saved user data
     */
    UserDTO save(UserDTO userDTO);
    
    /**
     * Finds a user by email.
     * 
     * @param email the email to search for
     * @return the found user
     */
    UserDTO findUserByEmail(String email);
    
    /**
     * Finds a user by CPF.
     * 
     * @param cpf the CPF to search for
     * @return the found user
     */
    UserDTO findUserByCpf(String cpf);
    
    /**
     * Checks if an email is already registered.
     * 
     * @param email the email to check
     * @return true if the email is already registered, false otherwise
     */
    boolean isEmailRegistered(String email);
    
    /**
     * Checks if a CPF is already registered.
     * 
     * @param cpf the CPF to check
     * @return true if the CPF is already registered, false otherwise
     */
    boolean isCpfRegistered(String cpf);
}
