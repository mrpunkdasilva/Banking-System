package org.jala.university.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for two-factor verification.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwoFactorVerificationDTO {

    /**
     * The user ID
     */
    private Long userId;

    /**
     * The verification code
     */
    private String code;
}