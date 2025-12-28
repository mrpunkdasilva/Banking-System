# Módulo de Transações Bancárias

Este módulo é responsável por gerenciar todas as transações bancárias do sistema, incluindo transferências, depósitos, saques, histórico e extrato. Ele segue os princípios da **Clean Architecture** e utiliza **JavaFX** para a interface gráfica.

## Funcionalidades

- Transferências entre contas
- Depósitos e saques
- Histórico de transações
- Extrato bancário
- Gerenciamento de beneficiários

## Stack Tecnológica

- **Java 17**
- **JavaFX** (interface gráfica)
- **JPA** (persistência)
- **MySQL** (banco de dados relacional)
- **Maven** (build e dependências)
- **Docker** (infraestrutura/containerização)

## Arquitetura

O projeto segue a **Clean Architecture** e o padrão **MVC/MVVM** para a interface. As principais camadas são:

```
src/
├── main/
│   ├── java/
│   │   └── org/jala/university/
│   │       ├── domain/         # Entidades e regras de negócio
│   │       ├── application/    # Casos de uso e regras de aplicação
│   │       ├── infrastructure/ # Implementações técnicas (persistência, config, util)
│   │       └── presentation/   # Interface do usuário (controllers, views, util)
│   └── resources/
│       ├── fxml/               # Layouts das telas
│       ├── styles/             # CSS
│       └── properties/         # Configurações
```

### Padrões de Projeto Utilizados

- **Repository**: abstração de acesso a dados
- **Factory**: criação de objetos complexos
- **Adapter**: integração com sistemas externos
- **Facade**: interface unificada para subsistemas
- **MVVM/MVC**: separação entre lógica, interface e controle

## Pré-requisitos

- Java Development Kit (JDK) 17+
- Docker e Docker Compose
- Maven 3.8+
- Git 2.x
- IDE (IntelliJ IDEA recomendado)

## Configuração e Execução

### 1. Clonar o repositório

```bash
git clone <url-do-repositorio>
cd transaction-module
```

### 2. Subir o banco de dados (MySQL via Docker)

```bash
docker-compose up -d
```

- Host: `localhost`
- Porta: `3306`
- Banco: `bank_db`
- Usuário: `root`
- Senha: `root`

> **Observação:** O container do banco de dados será criado com o nome `bank_mysql` e as credenciais acima. Para acessar o banco manualmente:
> 
> ```bash
> docker exec -it bank_mysql mysql -uroot -proot bank_db
> ```


### 3. Instalar dependências e rodar o projeto

```bash
./mvn-with-auth clean install -U
./mvn-with-auth javafx:run
```

#### Via IDE

- Abra o projeto na IDE.
- Configure o JDK 17.
- Importe as dependências Maven (usando o script `./mvn-with-auth`).
- Execute a classe principal: `org.jala.university.presentation.LoginApplication`

#### Via Maven (usando script)

```bash
./mvn-with-auth clean install -U
./mvn-with-auth javafx:run
```

## Testes

Para rodar os testes automatizados:

```bash
./mvn-with-auth test
```

## Dados de Teste

| Nome                              | CPF              | Celular         | Senha/E-mail                            |
|------------------------------------|------------------|-----------------|-----------------------------------------|
| Sabrina Yasmin Julia Brito         | 238.214.768-77   | (19) 99447-2641 | Sabrina-brito98@poli.ufrj.br            |
| Sérgio Pietro Silva                | 050.773.898-53   | (19) 98277-5853 | Sergio-silva99@terrabrasil.com.br       |
| Esther Aurora Laís Gonçalves       | 975.224.868-31   | (19) 98415-0289 | Esthe12r_aurora_goncalves@tce.sp.gov.br |
| Emilly Rafaela Josefa Viana        | 159.646.878-57   | (19) 99298-0936 | Emi32lly_viana@bravura.com.br           |
| Julia Rosa Dias                    | 487.250.398-81   | (19) 98838-9026 | Juli123a_dias@origembr.com              |

## Troubleshooting

- **Erro de conexão com o banco**: verifique se o container MySQL está rodando (`docker ps`), revise as credenciais e portas.
- **Problemas com JavaFX**: confira o caminho do JavaFX SDK e as dependências no `pom.xml`.
- **Erro de build Maven**: limpe o cache com `mvn clean` e `mvn dependency:purge-local-repository`.

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Minha contribuição'`)
4. Push para sua branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## Documentação

- Documentação detalhada disponível na pasta [`docs/`](docs/)
- Consulte também:
  - [Primeiros Passos](docs/getting-started.html)
  - [Arquitetura](docs/architecture.html)
  - [Entidades](docs/entities.html)
  - [Serviços](docs/services.html)

## Recursos Externos

- [JavaFX Documentation](https://openjfx.io/)
- [Maven Guide](https://maven.apache.org/guides/)
- [Docker Documentation](https://docs.docker.com/)

---

> **Desenvolvido para fins acadêmicos - Disciplina de Desenvolvimento de Software 2**
