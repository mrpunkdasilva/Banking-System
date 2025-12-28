# Serviços de Domínio

## Serviços Base

```java
public abstract class DomainService {
    protected final EventPublisher eventPublisher;
    
    protected DomainService(EventPublisher eventPublisher) {
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
    }
    
    protected void publishEvent(DomainEvent event) {
        eventPublisher.publish(event);
    }
}
```

## Implementações

### TransactionService
```java
public class TransactionService extends DomainService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    
    public void executeTransaction(Transaction transaction) {
        validateTransaction(transaction);
        
        try {
            accountService.debit(transaction.source(), transaction.amount());
            accountService.credit(transaction.destination(), transaction.amount());
            
            transaction.complete();
            transactionRepository.save(transaction);
            
            publishEvent(new TransactionCompletedEvent(transaction));
        } catch (Exception e) {
            transaction.fail();
            transactionRepository.save(transaction);
            publishEvent(new TransactionFailedEvent(transaction, e));
            throw new TransactionExecutionException("Failed to execute transaction", e);
        }
    }
    
    private void validateTransaction(Transaction transaction) {
        if (transaction.status() != TransactionStatus.PENDING) {
            throw new InvalidTransactionStateException(
                "Transaction must be in PENDING state");
        }
        
        if (transaction.scheduledFor().isAfter(LocalDateTime.now())) {
            throw new InvalidTransactionStateException(
                "Transaction is scheduled for future execution");
        }
    }
}
```

## Boas Práticas

1. **Regras de Negócio**
   - Validações de domínio
   - Invariantes preservados
   - Transações atômicas

2. **Eventos de Domínio**
   - Notificação de mudanças
   - Auditoria
   - Integração desacoplada

3. **Tratamento de Erros**
   - Exceções específicas
   - Estados consistentes
   - Rollback quando necessário