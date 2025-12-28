# Services

Services implementam a lógica de negócio da aplicação, orquestrando operações entre diferentes componentes do sistema.

## Tipos de Serviços

### AuthenticationService
Gerencia autenticação e autorização:
```plaintext
AuthenticationService
├── signup(SignupDTO)
├── login(LoginDTO)
└── validateToken(String)
```

### UserService
Gerencia operações de usuários:
```plaintext
UserService
├── create(UserDTO)
├── update(UserDTO)
├── delete(Long)
└── findById(Long)
```

## Responsabilidades

1. **Regras de Negócio**
   - Validações complexas
   - Orquestração de operações
   - Controle transacional

2. **Integração**
   - Comunicação com repositórios
   - Uso de mappers
   - Chamadas externas

## Boas Práticas

1. **Estrutura**
   - Interface definindo contrato
   - Implementação separada
   - Injeção de dependências

2. **Operações**
   - Métodos claros e coesos
   - Tratamento de erros
   - Logging apropriado