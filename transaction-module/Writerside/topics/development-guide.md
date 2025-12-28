# Guia de Desenvolvimento

## Configuração do Ambiente

1. Instale as ferramentas necessárias:
   - JDK 17
   - Maven 3.8+
   - Docker
   - IDE (recomendado: IntelliJ IDEA)

2. Clone o repositório:
```bash
git clone <repository-url>
cd transaction-module
```

## Estrutura do Projeto

```plaintext
src/
├── main/
│   ├── java/
│   │   └── org/jala/university/
│   │       ├── application/     # Casos de uso
│   │       ├── domain/         # Entidades e regras
│   │       ├── infrastructure/ # Implementações
│   │       └── presentation/   # UI e controllers
│   └── resources/
│       ├── META-INF/
│       │   └── persistence.xml
│       └── fxml/               # Layouts JavaFX
└── test/
    └── java/                  # Testes unitários
```

## Padrões de Código

### Nomenclatura

- Classes: PascalCase (ex: `TransactionService`)
- Métodos/Variáveis: camelCase (ex: `getUserBalance`)
- Constantes: SNAKE_CASE (ex: `MAX_AMOUNT`)

### Arquitetura

- Seguir Clean Architecture
- Usar injeção de dependência
- Manter camadas desacopladas

### Documentação

- Javadoc em classes públicas
- README atualizado
- Comentários claros quando necessário

### Checkstyle

O projeto utiliza Checkstyle para garantir a consistência e qualidade do código. O arquivo de configuração `checkstyle.xml` na raiz do projeto define as seguintes regras:

- **Espaços em branco**: Controle de indentação, uso de tabs vs. espaços, posicionamento de chaves
- **Nomenclatura**: Regras para nomes de classes, métodos, variáveis e constantes
- **Javadoc**: Documentação obrigatória para métodos, classes e variáveis públicas
- **Tamanho de código**: Limites para comprimento de linhas (120 caracteres), tamanho de métodos (150 linhas) e número de parâmetros (7)

Todas as violações estão configuradas com severidade "error".

#### Como usar o Checkstyle

**Com Maven:**
```bash
mvn checkstyle:check
```

**Com IDE:**
- IntelliJ IDEA: Instale o plugin "CheckStyle-IDEA" e configure-o para usar o arquivo `checkstyle.xml`
- Eclipse: Instale o plugin "Checkstyle" e configure-o para usar o arquivo `checkstyle.xml`

#### Principais regras

```xml
<!-- Exemplo de regras importantes -->
<module name="LineLength">
    <property name="max" value="120"/>
</module>
<module name="MethodLength">
    <property name="max" value="150"/>
</module>
<module name="JavadocMethod">
    <property name="scope" value="public"/>
</module>
```

> **Nota**: Não desative as regras do Checkstyle sem discussão prévia com a equipe.

## Git Flow

1. Branch principal: `main`
2. Branch de desenvolvimento: `develop`
3. Branches de feature: `feature/*`
4. Branches de correção: `hotfix/*`

### Commits

```bash
# Formato
<tipo>(<escopo>): <descrição>

# Exemplos
feat(transaction): adiciona validação de saldo
fix(auth): corrige verificação de token
```

## Boas Práticas

1. **SOLID**
   - Single Responsibility
   - Open/Closed
   - Liskov Substitution
   - Interface Segregation
   - Dependency Inversion

2. **Clean Code**
   - Métodos pequenos
   - Nomes significativos
   - DRY (Don't Repeat Yourself)
   - KISS (Keep It Simple)

3. **Tratamento de Erros**
   - Usar exceções apropriadas
   - Logging adequado
   - Validações de entrada

## Desenvolvimento Local

```bash
# Build
mvn clean install

# Executar
mvn javafx:run

# Testes
mvn test
```

## Dicas

1. Use Lombok para reduzir boilerplate
2. Mantenha testes atualizados
3. Revise código antes do commit
4. Atualize documentação quando necessário