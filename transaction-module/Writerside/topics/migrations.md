# Migrações de Banco de Dados

## Estratégia de Migração

O sistema utiliza a estratégia de migração automática do Hibernate com `hibernate.hbm2ddl.auto=update` em desenvolvimento.

### Configuração

```xml
<property name="hibernate.hbm2ddl.auto" value="update"/>
```

## Scripts de Migração

### V1 - Estrutura Inicial
```sql
-- Criação das tabelas principais
CREATE TABLE users ( ... );
CREATE TABLE accounts ( ... );
CREATE TABLE transactions ( ... );
CREATE TABLE account_beneficiaries ( ... );

-- Índices iniciais
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_cpf ON users(cpf);
```

## Boas Práticas

1. **Versionamento**
   - Scripts incrementais
   - Nomenclatura clara (V1, V2, etc.)
   - Documentação de mudanças

2. **Segurança**
   - Backup antes de migrações
   - Scripts idempotentes
   - Rollback planejado

3. **Ambiente**
   - Teste em desenvolvimento
   - Validação em staging
   - Execução em produção