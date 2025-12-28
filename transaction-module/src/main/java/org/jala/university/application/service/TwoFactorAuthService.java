package org.jala.university.application.service;

/**
 * Service for handling two-factor authentication.
 */
public interface TwoFactorAuthService {

    /**
     * Checks if two-factor authentication is enabled for a user.
     *
     * @param userId the user ID
     * @return true if 2FA is enabled, false otherwise
     */
    boolean isTwoFactorEnabled(Long userId);

    /**
     * Enables or disables two-factor authentication for a user.
     *
     * @param userId the user ID
     * @param enabled whether 2FA should be enabled
     * @return true if the operation was successful, false otherwise
     */
    boolean setTwoFactorEnabled(Long userId, boolean enabled);

    /**
     * Generates and sends a verification code to the user.
     *
     * @param userId the user ID
     * @return true if the code was generated and sent successfully, false otherwise
     */
    boolean generateAndSendCode(Long userId);

    /**
     * Verifies a two-factor authentication code.
     *
     * @param userId the user ID
     * @param code the verification code
     * @return true if the code is valid, false otherwise
     */
    boolean verifyCode(Long userId, String code);

    /**
     * Resends the verification code to the user.
     *
     * @param userId the user ID
     * @return true if the code was resent successfully, false otherwise
     */
    boolean resendCode(Long userId);
}