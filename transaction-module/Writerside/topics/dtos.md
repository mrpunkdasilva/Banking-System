# Data Transfer Objects (DTOs)

DTOs são objetos simples usados para transferir dados entre as camadas da aplicação, especialmente nas operações de entrada e saída do sistema.

## Tipos Principais

### Request DTOs
Usados para receber dados de entrada:
```plaintext
CreateTransactionRequestDTO
├── amount: BigDecimal
├── description: String
└── accountId: UUID
```

### Response DTOs
Usados para retornar dados processados:
```plaintext
TransactionResponseDTO
├── id: UUID
├── amount: BigDecimal
├── status: TransactionStatus
└── createdAt: LocalDateTime
```

## Boas Práticas

1. **Estrutura**
   - Manter campos simples
   - Usar tipos apropriados
   - Incluir apenas dados necessários

2. **Nomenclatura**
   - Sufixo `DTO`
   - Nomes descritivos
   - Padrão consistente

3. **Validação**
   - Campos obrigatórios
   - Formatos válidos
   - Regras básicas

## Benefícios

- Separa dados de regras de negócio
- Protege entidades do domínio
- Simplifica transferência de dados
- Facilita manutenção da API