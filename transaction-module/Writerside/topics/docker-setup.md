# Configuração do Docker

## Docker Compose

O arquivo `docker-compose.yml` define os serviços necessários para o ambiente de desenvolvimento.

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: transaction_mysql
    ports:
      - "3306:3306"
    environment:
      MYSQL_DATABASE: transaction_db
      MYSQL_USER: transaction_user
      MYSQL_PASSWORD: password123
      MYSQL_ROOT_PASSWORD: root
    volumes:
      - mysql_data:/var/lib/mysql

volumes:
  mysql_data:
```

## Comandos Principais

```bash
# Iniciar os serviços
docker-compose up -d

# Verificar status
docker-compose ps

# Parar os serviços
docker-compose down

# Logs
docker-compose logs -f
```

## Verificação

1. Banco MySQL disponível na porta 3306
2. Credenciais configuradas corretamente
3. Volume persistente criado