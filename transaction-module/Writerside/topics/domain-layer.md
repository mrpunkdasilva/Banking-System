# Camada de Domínio

A camada de domínio é o núcleo da aplicação, contendo as regras de negócio e entidades principais. Esta camada é independente de frameworks e tecnologias externas.

## Estrutura

```plaintext
domain/
├── entities/       # Entidades de domínio
├── repositories/   # Interfaces de repositórios
├── services/       # Serviços de domínio
└── exceptions/     # Exceções de domínio
```

## Princípios

1. **Independência**
   - Sem dependências externas
   - Regras de negócio puras
   - Interfaces bem definidas

2. **Imutabilidade**
   - Objetos imutáveis quando possível
   - Estado consistente
   - Thread-safety

3. **Validações**
   - Invariantes de domínio
   - Regras de negócio
   - Estado válido

## Value Objects

```java
public record Money(BigDecimal amount, Currency currency) {
    public Money {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");
        if (amount.scale() > 2) {
            throw new IllegalArgumentException("Amount cannot have more than 2 decimal places");
        }
    }
    
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add different currencies");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }
}
```