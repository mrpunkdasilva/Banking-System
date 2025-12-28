# Primeiros Passos

Este guia fornece instruções detalhadas para configurar e executar o Módulo de Transações em seu ambiente de desenvolvimento.

## Pré-requisitos

### Software Necessário
- Java Development Kit (JDK) 17 ou superior
  - Recomendamos o Amazon Corretto ou OpenJDK
  - Variável JAVA_HOME deve estar configurada
- Docker Desktop (Windows/Mac) ou Docker Engine (Linux)
  - Docker Compose v2.0 ou superior
- Maven 3.8+
- Git 2.x
- IDE (recomendamos uma das opções abaixo):
  - IntelliJ IDEA (recomendado)
  - Eclipse com plugin e(fx)clipse

### Requisitos de Hardware
- Mínimo de 8GB de RAM
- 10GB de espaço em disco
- Processador dual-core ou superior

## Configuração do Ambiente

### 1. Preparação do Ambiente

#### Windows
```bash
# Verifique as instalações
java --version
docker --version
mvn --version
git --version
```

#### Linux/MacOS
```bash
# Instale as dependências (Ubuntu/Debian)
sudo apt update
sudo apt install openjdk-17-jdk maven git docker.io docker-compose

# Adicione seu usuário ao grupo docker
sudo usermod -aG docker $USER
```

### 2. Clonando o Repositório

```bash
# Clone o repositório
git clone <url-do-repositório>
cd transaction-module

# Instale as dependências Maven
mvn clean install
```

### 3. Configuração do Banco de Dados

#### Usando Docker Compose
```bash
# Inicie os serviços
docker-compose up -d

# Verifique os logs
docker-compose logs -f mysql
```

#### Configurações do MySQL
```yaml
Host: localhost
Porta: 3306
Banco: transaction_db
Usuário: transaction_user
Senha: password123
```

#### Verificação do Banco
```bash
# Teste a conexão
docker exec -it mysql-container mysql -utransaction_user -ppassword123 transaction_db
```

### 4. Configuração da IDE

#### IntelliJ IDEA
1. Abra o projeto: File → Open → Selecione a pasta do projeto
2. Configure o JDK: File → Project Structure → Project SDK → Add SDK → JDK
3. Importe as dependências Maven: Clique no ícone "Load Maven Changes"
4. Configure o Run Configuration:
   - Main class: `org.jala.university.presentation.MainApplication`
   - JRE: Java 17
   - VM options: `--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml`
5. Configure o Checkstyle:
   - Instale o plugin "CheckStyle-IDEA" através do marketplace de plugins
   - Vá para Settings → Tools → Checkstyle
   - Adicione o arquivo `checkstyle.xml` como configuração
   - Ative a verificação em tempo real

#### Eclipse
1. Import → Maven → Existing Maven Projects
2. Configure o Java Build Path
3. Adicione as dependências do JavaFX
4. Configure o Checkstyle:
   - Instale o plugin "Checkstyle" através do Eclipse Marketplace
   - Vá para Window → Preferences → Checkstyle
   - Adicione o arquivo `checkstyle.xml` como configuração
   - Ative a verificação em tempo real

### 5. Executando o Projeto

#### Via IDE
1. Localize a classe principal:
```java
src/main/java/org/jala/university/presentation/controller/MainApplication.java
```
2. Execute como Java Application

#### Via Maven
```bash
# Execução padrão
./mvnw clean javafx:run

# Execução com perfil de desenvolvimento
./mvnw clean javafx:run -Pdev

# Execução com debug
./mvnw clean javafx:run -X
```


## Estrutura do Projeto

O projeto segue uma arquitetura em camadas. Abaixo está a estrutura principal:

### Estrutura de Diretórios
```plaintext
src/
├── main/
│   ├── java/
│   │   └── org/jala/university/
│   │       ├── application/     # Casos de uso
│   │       ├── domain/         # Entidades e regras de negócio
│   │       ├── infrastructure/ # Implementações técnicas
│   │       └── presentation/   # Controllers e Views
│   └── resources/
│       ├── css/               # Arquivos de estilo
│       └── fxml/             # Layouts das telas
```

## Solução de Problemas

### Problemas Comuns

#### 1. Erro de Conexão com o Banco
```bash
# Verifique o status do container
docker ps
docker logs mysql-container

# Teste a conexão
telnet localhost 3306
```

#### 2. Erro de Compilação
```bash
# Limpe o cache Maven
mvn clean

# Atualize as dependências
mvn dependency:purge-local-repository
mvn clean install -U
```

#### 3. Problemas com JavaFX
- Verifique o PATH do JavaFX SDK
- Confirme as dependências no pom.xml
- Teste com diferentes versões do JDK

#### 4. Problemas com Checkstyle
- Verifique se o arquivo `checkstyle.xml` está na raiz do projeto
- Execute o comando `mvn checkstyle:check` para ver erros detalhados
- Configure seu IDE para usar o mesmo arquivo de configuração
- Formate seu código automaticamente antes de verificar com Checkstyle

### Logs e Debugging
```bash
# Ative logs detalhados
mvn clean javafx:run -X

# Verifique erros de Checkstyle
mvn checkstyle:check -X
```

## Próximos Passos

1. [Interface do Usuário](user-interface.md)
   - Fluxo de navegação
   - Componentes principais
   - Personalização de temas

2. [Arquitetura](architecture.md)
   - Clean Architecture
   - Padrões de projeto
   - Boas práticas

3. [Guia de Desenvolvimento](development-guide.md)
   - Convenções de código
   - Processo de build
   - Testes

4. [Guia de Checkstyle](checkstyle-guide.md)
   - Regras de estilo de código
   - Configuração do Checkstyle
   - Resolução de problemas comuns

## Suporte e Recursos


### Recursos Adicionais
- [JavaFX Documentation](https://openjfx.io/)
- [Maven Guide](https://maven.apache.org/guides/)
- [Docker Documentation](https://docs.docker.com/)
- [Checkstyle Documentation](https://checkstyle.org/)
- [Maven Checkstyle Plugin](https://maven.apache.org/plugins/maven-checkstyle-plugin/)