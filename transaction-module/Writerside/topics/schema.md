# Esquema do Banco de Dados

## Tabelas Principais

### Users
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(250) NOT NULL,
    cpf VARCHAR(13) NOT NULL UNIQUE,
    phone_number VARCHAR(15) NOT NULL,
    roles VARCHAR(20) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME
);
```

### Accounts
```sql
CREATE TABLE accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    agency VARCHAR(20) NOT NULL,
    account_number VARCHAR(20) NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    balance DECIMAL(10,2) DEFAULT 0.00,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Transactions
```sql
CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    description VARCHAR(255) NOT NULL,
    created_at DATETIME,
    FOREIGN KEY (sender_id) REFERENCES accounts(id),
    FOREIGN KEY (receiver_id) REFERENCES accounts(id)
);
```

### Account Beneficiaries
```sql
CREATE TABLE account_beneficiaries (
    account_id BIGINT NOT NULL,
    beneficiary_id BIGINT NOT NULL,
    favorite BIT(1),
    PRIMARY KEY (account_id, beneficiary_id),
    FOREIGN KEY (account_id) REFERENCES accounts(id),
    FOREIGN KEY (beneficiary_id) REFERENCES accounts(id)
);
```

## Índices

```sql
-- Users
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_cpf ON users(cpf);

-- Accounts
CREATE INDEX idx_accounts_user ON accounts(user_id);
CREATE INDEX idx_accounts_number ON accounts(account_number);

-- Transactions
CREATE INDEX idx_transactions_sender ON transactions(sender_id);
CREATE INDEX idx_transactions_receiver ON transactions(receiver_id);
CREATE INDEX idx_transactions_created ON transactions(created_at);
```