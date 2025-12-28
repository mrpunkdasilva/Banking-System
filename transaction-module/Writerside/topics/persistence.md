# Persistência

## Implementação Base

O projeto utiliza uma implementação base de repositório que fornece operações CRUD comuns:

```java
public abstract class CrudRepository<T extends BaseEntity<ID>, ID> {
    protected final EntityManager entityManager;
    
    // Operações básicas: save, findById, delete, etc.
}
```

## Configuração JPA

```plaintext
Principais Configurações:
├── Hibernate como provedor JPA
├── Connection pool com HikariCP
└── Transações gerenciadas por JPA
```

## Boas Práticas

1. **Performance**
   - Consultas otimizadas
   - Paginação quando necessário
   - Índices apropriados

2. **Transações**
   - Escopo bem definido
   - Isolamento adequado
   - Tratamento de deadlocks

3. **Segurança**
   - Prevenção de SQL Injection
   - Validação de inputs
   - Controle de acesso

## Exemplo de Uso

```java
@Repository
public class UserRepositoryImpl extends CrudRepository<User, Long> {
    // Implementações específicas para User
}
```