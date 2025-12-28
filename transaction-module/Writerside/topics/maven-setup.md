# Configuração do Maven

## POM.xml

Principais dependências e plugins configurados no `pom.xml`:

```xml
<dependencies>
    <!-- Local Dependency -->
    <dependency>
        <groupId>org.jala.university</groupId>
        <artifactId>commons-module</artifactId>
        <version>${commons.module.version}</version>
    </dependency>

    <!-- Spring Boot -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
        <version>${spring.boot.version}</version>
    </dependency>

    <!-- Jakarta Persistence -->
    <dependency>
        <groupId>jakarta.persistence</groupId>
        <artifactId>jakarta.persistence-api</artifactId>
        <version>${jakarta.persistence.version}</version>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${lombok.version}</version>
        <optional>true</optional>
        <scope>provided</scope>
    </dependency>

    <!-- JavaFX -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>${javafx.version}</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>${javafx.version}</version>
    </dependency>

    <!-- Testing -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.0-M1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>5.5.0</version>
        <scope>test</scope>
    </dependency>

    <!-- Database -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>9.1.0</version>
        <scope>runtime</scope>
    </dependency>

    <!-- Security -->
    <dependency>
        <groupId>com.auth0</groupId>
        <artifactId>java-jwt</artifactId>
        <version>4.5.0</version>
    </dependency>
</dependencies>
```

## Versões das Dependências

```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <commons.module.version>1.0-SNAPSHOT</commons.module.version>
    <spring.boot.version>3.2.4</spring.boot.version>
    <jakarta.persistence.version>3.1.0</jakarta.persistence.version>
    <lombok.version>1.18.38</lombok.version>
    <javafx.version>22</javafx.version>
    <javafx.maven.plugin.version>0.0.8</javafx.maven.plugin.version>
</properties>
```

## Plugins

```xml

<build>
    <plugins>
        <!-- JavaFX -->
        <plugin>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-maven-plugin</artifactId>
            <version>${javafx.maven.plugin.version}</version>
            <configuration>
                <mainClass>org.jala.university.presentation.LoginApplicationorg.jala.university.presentation.LoginApplication</mainClass>
            </configuration>
        </plugin>

        <!-- Lombok -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.11.0</version>
            <configuration>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>${lombok.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## Comandos Maven

```bash
# Limpar e compilar
mvn clean compile

# Executar testes
mvn test

# Executar aplicação
mvn javafx:run

# Gerar pacote
mvn package
```

## Repositórios

Para dependências do GitLab, adicione:

```xml
<repositories>
    <repository>
        <id>gitlab-maven</id>
        <url>https://gitlab.com/api/v4/projects/{project_code}/packages/maven</url>
    </repository>
</repositories>
```

## Boas Práticas

1. Mantenha as versões das dependências atualizadas
2. Use variáveis para versões no `properties`
3. Configure corretamente os escopos das dependências
4. Mantenha os plugins necessários para o build
5. Documente alterações significativas no `pom.xml`