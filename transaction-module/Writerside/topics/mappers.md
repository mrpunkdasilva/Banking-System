# Mappers

Mappers são responsáveis pela conversão entre DTOs e entidades do domínio, mantendo a separação entre as camadas da aplicação.

## Interface Base

```java
public interface Mapper<T, I> {
    I mapTo(T input);
    T mapFrom(I input);
}
```

## Implementações

### UserMapper
Converte entre `User` e `UserDTO`:
```plaintext
UserMapper
├── mapTo: User -> UserDTO
└── mapFrom: UserDTO -> User
```

## Responsabilidades

1. **Conversão**
   - Transformação entre objetos
   - Mapeamento bidirecional
   - Tratamento de tipos

2. **Isolamento**
   - Centraliza lógica de conversão
   - Evita duplicação de código
   - Facilita manutenção

## Boas Práticas

- Implementar interface `Mapper`
- Manter mapeamentos simples
- Tratar casos nulos
- Validar dados convertidos