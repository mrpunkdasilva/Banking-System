# Banco de Dados

O sistema utiliza MySQL como banco de dados principal, com JPA/Hibernate para mapeamento objeto-relacional.

## Configuração

```xml
<persistence-unit name="transaction-pu" transaction-type="RESOURCE_LOCAL">
    <properties>
        <property name="jakarta.persistence.jdbc.url" 
                  value="jdbc:mysql://localhost:3306/transaction_db"/>
        <property name="hibernate.dialect" 
                  value="org.hibernate.dialect.MySQLDialect"/>
        <property name="hibernate.show_sql" value="true"/>
        <property name="hibernate.format_sql" value="true"/>
    </properties>
</persistence-unit>
```

## Estrutura Principal

```plaintext
transaction_db
├── users
├── accounts
├── transactions
└── account_beneficiaries
```

## Boas Práticas

1. **Nomenclatura**
   - Tabelas em plural (users, accounts)
   - Colunas em snake_case
   - Chaves estrangeiras com sufixo _id

2. **Integridade**
   - Constraints de chave estrangeira
   - Índices para performance
   - Unicidade onde necessário

3. **Auditoria**
   - Campos created_at e updated_at
   - Tracking de modificações