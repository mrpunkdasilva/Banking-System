package org.jala.university.domain.repository;

import org.jala.university.commons.domain.Repository;
import org.jala.university.domain.entity.TwoFactorCode;
import org.jala.university.domain.entity.User;

/**
 * Repository for managing two-factor authentication codes.
 */
public interface TwoFactorCodeRepository extends Repository<TwoFactorCode, Long> {

    /**
     * Finds the latest active code for a user.
     * @param user the user
     * @return the latest active code, or null if none exists
     */
    TwoFactorCode findLatestActiveByUser(User user);

    /**
     * Finds a code by its value and user.
     * @param code the code value
     * @param user the user
     * @return the code, or null if none exists
     */
    TwoFactorCode findByCodeAndUser(String code, User user);

    /**
     * Invalidates all active codes for a user.
     * @param user the user
     */
    void invalidateAllActiveCodesForUser(User user);
    
    /**
     * Finds the latest valid code for a user by user ID.
     * @param userId the user ID
     * @return the latest valid code, or null if none exists
     */
    TwoFactorCode findLatestValidCode(Long userId);
}
