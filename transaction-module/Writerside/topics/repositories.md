# Repositórios

## Interfaces Base

```java
public interface Repository<T extends BaseEntity<ID>, ID> {
    Optional<T> findById(ID id);
    T save(T entity);
    void delete(T entity);
    boolean exists(ID id);
}
```

## Repositórios de Domínio

### UserRepository
```java
public interface UserRepository extends Repository<User, UUID> {
    Optional<User> findByEmail(Email email);
    boolean existsByEmail(Email email);
    List<User> findByRole(Role role);
}
```

### TransactionRepository
```java
public interface TransactionRepository extends Repository<Transaction, UUID> {
    List<Transaction> findByAccount(Account account);
    List<Transaction> findByStatus(TransactionStatus status);
    List<Transaction> findScheduledBetween(LocalDateTime start, LocalDateTime end);
}
```

## Boas Práticas

1. **Interfaces Claras**
   - Métodos bem nomeados
   - Parâmetros tipados
   - Documentação quando necessário

2. **Consultas Específicas**
   - Métodos para casos de uso
   - Critérios de busca
   - Ordenação quando relevante

3. **Performance**
   - Paginação quando necessário
   - Consultas otimizadas
   - Carregamento sob demanda