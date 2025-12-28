package org.jala.university.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jala.university.commons.domain.BaseEntity;
import org.jala.university.domain.entity.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Table(name = "accounts")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 20)
    private String agency;

    @Column(nullable = false, length = 20, unique = true)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Column(precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.valueOf(50000);

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "sender")
    private Set<Transaction> sentTransactions;

    @OneToMany(mappedBy = "receiver")
    private Set<Transaction> receivedTransactions;

    @ManyToMany
    @JoinTable(
            name = "account_beneficiaries",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "beneficiary_id")
    )
    private Set<Account> beneficiaries;

    @PrePersist
    public void prePersist() {
        if (balance == null) {
            balance = BigDecimal.valueOf(50000);
        }
    }
}
