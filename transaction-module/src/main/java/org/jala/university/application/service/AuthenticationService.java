package org.jala.university.application.service;

import org.jala.university.application.dto.TwoFactorVerificationDTO;

/**
 * Service for handling user authentication.
 */
public interface AuthenticationService {
    
    /**
     * Authenticates a user with email and password.
     * 
     * @param email the user's email
     * @param password the user's password
     * @return authentication token or null if 2FA is required
     */
    String authenticate(String email, String password);
    
    /**
     * Verifies a two-factor authentication code.
     * 
     * @param verificationDTO the verification data
     * @return authentication token or null if verification fails
     */
    String verifyTwoFactor(TwoFactorVerificationDTO verificationDTO);
    
    /**
     * Logs out the current user.
     */
    void logout();
}
