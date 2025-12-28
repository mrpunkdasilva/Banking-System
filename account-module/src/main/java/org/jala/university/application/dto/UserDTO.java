package org.jala.university.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jala.university.commons.domain.Role;
import org.jala.university.domain.entity.Account;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for User entity.
 * Used to transfer user data between layers without exposing entity details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String cpf;
    private String phoneNumber;
    private Role roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Account> accounts;
    private boolean twoFactorEnabled;
    
    /**
     * Checks if two-factor authentication is enabled.
     * 
     * @return true if 2FA is enabled, false otherwise
     */
    public boolean isTwoFactorEnabled() {
        return twoFactorEnabled;
    }
}
