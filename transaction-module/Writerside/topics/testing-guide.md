# Guia de Testes

## Estrutura de Testes

### Tipos de Testes

1. **Testes Unitários**
   - JUnit 5
   - Mockito
   - Testes isolados

2. **Testes de Integração**
   - Banco de dados
   - APIs externas
   - Componentes integrados

3. **Testes de UI**
   - TestFX para JavaFX
   - Cenários de usuário

## Configuração

```xml
<!-- Dependências de teste -->
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
```

## Padrões de Teste

### Nomenclatura

```java
@Test
void deveRetornarSaldoCorreto_QuandoContaExiste() {
    // ...
}

@Test
void deveLancarExcecao_QuandoContaNaoExiste() {
    // ...
}
```

### Estrutura AAA

```java
@Test
void deveRealizarTransacao() {
    // Arrange
    var conta = new Conta("123", 1000.0);
    var valor = 100.0;

    // Act
    service.realizarTransacao(conta, valor);

    // Assert
    assertEquals(900.0, conta.getSaldo());
}
```

## Mocks e Stubs

```java
@Test
void deveValidarUsuario() {
    // Arrange
    var userRepo = mock(UserRepository.class);
    when(userRepo.findById("123"))
        .thenReturn(Optional.of(new User("123")));
    
    var service = new UserService(userRepo);

    // Act & Assert
    assertTrue(service.validarUsuario("123"));
}
```

## Cobertura de Código

- Meta mínima: 80% de cobertura
- Foco em código de negócio
- Usar JaCoCo para relatórios

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.8</version>
</plugin>
```

## Execução de Testes

```bash
# Todos os testes
mvn test

# Testes específicos
mvn test -Dtest=TransactionServiceTest

# Com cobertura
mvn verify
```

## Boas Práticas

1. **Independência**
   - Testes isolados
   - Sem dependências entre testes
   - Setup e teardown apropriados

2. **Legibilidade**
   - Nomes descritivos
   - Comentários quando necessário
   - Organização clara

3. **Manutenibilidade**
   - DRY nos testes
   - Fixtures reutilizáveis
   - Constantes compartilhadas

4. **Confiabilidade**
   - Testes determinísticos
   - Sem dependências externas
   - Timeouts apropriados

## CI/CD

- Testes automatizados no pipeline
- Verificação de cobertura
- Relatórios de teste