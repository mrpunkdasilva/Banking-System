# Variáveis de Ambiente

## Arquivo .env

```properties
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=transaction_db
DB_USER=transaction_user
DB_PASS=password123

# Application
APP_PORT=8080
APP_ENV=development
```

## Configuração no IDE

### IntelliJ IDEA
1. Edit Configurations
2. Add VM Options:
```
-Ddb.host=${DB_HOST}
-Ddb.port=${DB_PORT}
-Ddb.name=${DB_NAME}
-Ddb.user=${DB_USER}
-Ddb.pass=${DB_PASS}
```

## Variáveis Necessárias

| Variável | Descrição | Valor Padrão |
|----------|-----------|--------------|
| DB_HOST | Host do banco de dados | localhost |
| DB_PORT | Porta do MySQL | 3306 |
| DB_NAME | Nome do banco | transaction_db |
| DB_USER | Usuário do banco | transaction_user |
| DB_PASS | Senha do banco | password123 |
| APP_PORT | Porta da aplicação | 8080 |
| APP_ENV | Ambiente atual | development |

## Segurança

- Nunca commite o arquivo `.env`
- Use `.env.example` como template
- Mantenha senhas seguras em produção