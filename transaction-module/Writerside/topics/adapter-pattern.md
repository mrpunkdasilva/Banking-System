# Adapter Pattern

## Visão Geral

O padrão **Adapter** é um padrão de projeto estrutural que permite que classes com interfaces incompatíveis trabalhem juntas, convertendo a interface de uma classe em outra esperada por um cliente. No contexto da arquitetura hexagonal, ele é comumente aplicado para adaptar implementações técnicas (como UI e persistência) às interfaces de domínio (ports).

---

## Implementação no Projeto

### BeneficiaryController como Adapter

A classe `BeneficiaryController` atua como um **adaptador da interface do usuário (JavaFX)** para os casos de uso da aplicação (`BeneficiaryUseCases`). Ela converte ações do usuário (eventos de botão, preenchimento de formulário) em chamadas aos métodos da camada de aplicação.

```java
public class BeneficiaryController {
    private final BeneficiaryUseCases beneficiaryUseCases;

    public BeneficiaryController() {
        this.beneficiaryUseCases = new BeneficiaryUseCases(
            new BeneficiaryRepositoryImp(entityManager),
            new AccountRepositoryImp(entityManager),
            new AccountBeneficiaryRepositoryImp(entityManager),
            new ValidationService()
        );
    }

    public void saveBeneficiary() {
        BeneficiaryDTO dto = ...
        beneficiaryUseCases.addBeneficiary(dto, currentAccountId);
    }
}
```

---

### Repositórios como Adapters

As classes `BeneficiaryRepositoryImp`, `AccountRepositoryImp` e `AccountBeneficiaryRepositoryImp` são **adapters da infraestrutura (JPA)** para o domínio. Elas implementam interfaces (`BeneficiaryRepository`, etc.) que definem operações esperadas no domínio, mas usando tecnologia específica (JPA).

```java
public class BeneficiaryRepositoryImp implements BeneficiaryRepository {
    private final EntityManager em;

    public void addBeneficiary(Beneficiary b) {
        em.persist(b);
    }
}
```

---

### Estrutura do Adapter no Projeto

| Papel do Adapter Pattern         | Elemento no Projeto                                    | Descrição                                                                 |
|----------------------------------|---------------------------------------------------------|---------------------------------------------------------------------------|
| **Target (Interface esperada)**  | `BeneficiaryRepository`, `AccountRepository`, etc.      | Interfaces do domínio que definem o contrato esperado.                    |
| **Adapter (classe adaptadora)**  | `BeneficiaryRepositoryImp`, `AccountRepositoryImp`      | Implementam as interfaces usando JPA.                                     |
| **Adaptee (API/tecnologia real)**| `EntityManager`, `JPA`, `JavaFX`, etc.                  | Ferramentas externas que estão sendo adaptadas.                          |
| **Cliente**                      | `BeneficiaryUseCases`, `BeneficiaryController`          | Usam as interfaces e não conhecem as implementações técnicas.            |

---

## Diagrama Estrutural
![diagrama_estrutural](uploads/c59da96f378a5d9f3fc5ba0d2f74698d/diagrama_estrutural.png)

---

## Benefícios no Projeto

1. **Desacoplamento de Tecnologia**
    - As camadas superiores (casos de uso e controladores) usam apenas interfaces, sem conhecimento de JPA, JavaFX, etc.

2. **Testabilidade**
    - Pode-se substituir facilmente os adapters por mocks em testes, já que dependem apenas de interfaces.

3. **Facilidade de Substituição**
    - É possível trocar o mecanismo de persistência ou interface gráfica sem afetar a lógica de negócios.

4. **Organização por Camadas**
    - Fica claro que os adapters pertencem à camada de infraestrutura (persistência) ou apresentação (UI), mantendo o domínio isolado.

---

## Uso no Código

### Interface (Target)

```java
public interface BeneficiaryRepository {
    void addBeneficiary(Beneficiary beneficiary);
    void removeBeneficiary(Long id);
}
```

### Adapter de Persistência (Implementação JPA)

```java
public class BeneficiaryRepositoryImp implements BeneficiaryRepository {
    private final EntityManager em;

    public void addBeneficiary(Beneficiary beneficiary) {
        em.persist(beneficiary);
    }
}
```

### Adapter de Apresentação (Controller)

```java
public class BeneficiaryController {
    private final BeneficiaryUseCases useCases;

    public void saveBeneficiary() {
        BeneficiaryDTO dto = ...
        useCases.addBeneficiary(dto, currentAccountId);
    }
}
```

---

## Considerações de Design

1. **Quando Usar**
    - Quando há necessidade de **conectar uma camada desacoplada** (domínio) com implementações concretas (JPA, UI, APIs).
    - Quando você quer **separar lógica de negócio da infraestrutura técnica**.

2. **Vantagens**
    - Código mais limpo, modular, substituível e testável.
    - Redução do acoplamento entre camadas do sistema.

3. **Complementaridade com outros padrões**
    - O padrão Adapter se integra bem com os padrões **Hexagonal Architecture**, **Ports and Adapters**, e **Facade**, todos presentes nesse projeto.