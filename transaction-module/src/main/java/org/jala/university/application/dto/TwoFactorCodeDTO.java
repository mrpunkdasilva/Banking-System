package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a two-factor authentication code.
 */
@Builder
@Getter
@Setter
@ToString
public class TwoFactorCodeDTO {
    /**
     * Unique identifier for the code
     */
    private Long id;
    
    /**
     * The user ID associated with this code
     */
    private Long userId;
    
    /**
     * The verification code
     */
    private String code;
    
    /**
     * When the code expires
     */
    private LocalDateTime expiresAt;
    
    /**
     * When the code was created
     */
    private LocalDateTime createdAt;
}