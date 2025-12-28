# Entidades

## Entidade Base

```java
@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity<ID> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

## Entidades Principais

### User
```java
@Data
@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity<Long> {
    @Column(nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 250)
    private String password;

    @Column(nullable = false, length = 13, unique = true)
    private String cpf;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Account> accounts;
}
```

### Transaction
```java
@Data
@Entity
@Table(name = "transactions")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends BaseEntity<Long> {
    @ManyToOne
    @JoinColumn(name = "source_account_id", nullable = false)
    private Account source;

    @ManyToOne
    @JoinColumn(name = "destination_account_id", nullable = false)
    private Account destination;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(nullable = false)
    private LocalDateTime scheduledFor;

    @PrePersist
    @PreUpdate
    private void validateTransaction() {
        if (source.equals(destination)) {
            throw new InvalidTransactionException("Source and destination accounts cannot be the same");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Amount must be positive");
        }
    }
}
```

## Anotações Lombok

- `@Data`: Gera getters, setters, equals, hashCode e toString
- `@Builder`: Implementa o padrão Builder
- `@NoArgsConstructor`: Gera construtor sem argumentos
- `@AllArgsConstructor`: Gera construtor com todos os argumentos

## Anotações JPA

- `@Entity`: Marca a classe como entidade JPA
- `@Table`: Define o nome da tabela
- `@Column`: Configura propriedades da coluna
- `@ManyToOne`: Define relacionamento muitos-para-um
- `@OneToMany`: Define relacionamento um-para-muitos
- `@Enumerated`: Configura persistência de enums
