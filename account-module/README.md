# Account Module

O Account Module é responsável por gerenciar contas bancárias, usuários e beneficiários no sistema bancário. Este módulo fornece funcionalidades para criar, atualizar, consultar e gerenciar contas bancárias e seus relacionamentos.

## Estrutura do Projeto

O projeto segue uma arquitetura em camadas baseada:

```
src/main/java/org/jala/university/
├── application/
│   ├── dto/                 # Data Transfer Objects
│   ├── map/                 # Mapeadores entre entidades e DTOs
│   ├── mock/                # Implementações mock para testes
│   └── service/
│       ├── implementations/ # Implementações de serviços
│       └── interfaces/      # Interfaces de serviços
├── domain/
│   ├── entity/              # Entidades de domínio
│   │   └── enums/           # Enumerações
│   └── repository/          # Interfaces de repositório
├── infrastructure/
│   ├── config/              # Configurações de infraestrutura
│   └── persistence/         # Implementações de repositórios
└── presentation/
    └── controller/          # Controladores da interface do usuário
```

## Principais Componentes

### Entidades

- **Account**: Representa uma conta bancária com informações como número da conta, agência, tipo de conta e saldo.
- **User**: Representa um usuário do sistema bancário.
- **AccountBeneficiary**: Representa um relacionamento entre uma conta e um beneficiário.

### Repositórios

- **AccountRepository**: Interface para operações de persistência relacionadas a contas.
- **UserRepository**: Interface para operações de persistência relacionadas a usuários.
- **AccountBeneficiaryRepository**: Interface para operações de persistência relacionadas a beneficiários de contas.

### Serviços

- **AccountService**: Serviço para operações de negócio relacionadas a contas.
- **UserService**: Serviço para operações de negócio relacionadas a usuários.

### Infraestrutura

- **JPAConfig**: Configuração do JPA para acesso ao banco de dados.
- **AccountRepositoryImp**: Implementação do repositório de contas usando JPA.
- **AccountBeneficiaryRepositoryImp**: Implementação do repositório de beneficiários usando JPA.

## Integração com Outros Módulos

### Transaction Module

O Account Module é utilizado pelo Transaction Module para:
- Validar contas de origem e destino em transações
- Atualizar saldos de contas após transações
- Verificar relacionamentos entre contas e beneficiários

## Configuração e Instalação

### Pré-requisitos

- Java 17 ou superior
- Maven 3.8 ou superior
- MySQL 8.0 ou superior
- Docker e Docker Compose

### Instalação

1. Clone o repositório:
```bash
git clone https://gitlab.com/jala-university1/cohort-2/desarrollo-de-software-2-es/practitioners/capstone/account-module.git
```

2. Navegue até o diretório do projeto:
```bash
cd account-module
```

3. Compile o projeto:
```bash
mvn clean install
```

### Usando Docker

O projeto inclui configuração Docker para facilitar o desenvolvimento e implantação.

1. Construa a imagem Docker:
```bash
docker build -t account-module:latest .
```

2. Execute o container:
```bash
docker run -p 8080:8080 account-module:latest
```

Alternativamente, você pode usar o Docker Compose para iniciar o módulo junto com suas dependências:

```bash
docker compose up -d
```

O arquivo `docker-compose.yml` configura:
- O serviço account-module
- Um banco de dados MySQL
- Volumes para persistência de dados
- Redes para comunicação entre serviços

### Configuração do Banco de Dados

O módulo utiliza JPA com Hibernate para persistência. Configure o arquivo `persistence.xml` com as credenciais do seu banco de dados:

```xml
<persistence-unit name="default">
    <properties>
        <property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/bank_db"/>
        <property name="jakarta.persistence.jdbc.user" value="root"/>
        <property name="jakarta.persistence.jdbc.password" value="root"/>
        <property name="jakarta.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
        <property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect"/>
        <property name="hibernate.show_sql" value="true"/>
        <property name="hibernate.format_sql" value="true"/>
        <property name="hibernate.hbm2ddl.auto" value="update"/>
    </properties>
</persistence-unit>
```

## Uso como Dependência

Para usar o Account Module como dependência em outro projeto, adicione a seguinte configuração ao seu `pom.xml`:

```xml
<dependency>
    <groupId>org.jala.university</groupId>
    <artifactId>account-module</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<repositories>
    <repository>
        <id>account-module</id>
        <url>https://gitlab.com/api/v4/projects/68637701/packages/maven</url>
    </repository>
</repositories>
```

## Testes

Execute os testes com o seguinte comando:

```bash
mvn test
```

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Faça commit das suas alterações (`git commit -m 'Adiciona nova feature'`)
4. Faça push para a branch (`git push origin feature/nova-feature`)
5. Abra um Merge Request
