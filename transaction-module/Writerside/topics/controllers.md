# Controllers

## Estrutura Base

```java
public abstract class BaseController {
    @FXML
    private Parent root;
    
    protected final ValidationSupport validationSupport;
    protected final ErrorHandler errorHandler;
    
    // Métodos comuns
}
```

## Implementações

### LoginController
```java
public class LoginController extends BaseController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    
    private final LoginViewModel viewModel;
    
    @Override
    public void initialize() {
        setupValidation();
        setupBindings();
    }
}
```

## Boas Práticas

1. **Inicialização**
   - `@FXML` para injeção
   - Setup no `initialize()`
   - Validação inicial

2. **Tratamento de Erros**
   - Mensagens amigáveis
   - Logging apropriado
   - Recuperação graceful

3. **Navegação**
   - Transições suaves
   - Estado preservado
   - Loading indicators

## Exemplo de Uso

```java
@FXML
private void handleLogin() {
    if (!validationSupport.isValid()) {
        return;
    }
    
    viewModel.loginCommand()
        .thenAccept(this::navigateToHome)
        .exceptionally(this::handleError);
}
```