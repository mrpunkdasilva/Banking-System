package org.jala.university.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jala.university.commons.domain.BaseEntity;

import java.time.LocalDateTime;

/**
 * Entity representing a two-factor authentication code.
 */
@Entity
@Table(name = "two_factor_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwoFactorCode implements BaseEntity<Long> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String code;
    
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "used", nullable = false)
    private boolean used = false;
}
