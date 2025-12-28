# Tela de Cadastro

## Layout

- Tela de cadastro:

![Cadastro](Cadastro)

- Mensagem depois de se cadastrar e os dados serem válidos:

![Mensagem depois de se cadastrar](Mensagem depois de se cadastrar)

- Tela de Autenticação:

![Autenticação](Autenticação)

- Tela do serviço de email:

![Tela do serviço de email](image_2.png)

- Ao ser autenticado ele é levado para a dashboard do usuário:

![Dashboard](Dashboard)


## Componentes FXML

```xml
<!-- Campos do formulário -->
<TextField fx:id="fullNameField" promptText="Digite seu nome completo"/>
<TextField fx:id="cpfField" promptText="000.000.000-00"/>
<TextField fx:id="emailField" promptText="seu@email.com"/>
<TextField fx:id="phoneField" promptText="(00) 00000-0000"/>
<ComboBox fx:id="accountTypeCombo"/>
<PasswordField fx:id="passwordField" promptText="********"/>
```

## Controller

```java
public class SignUpController {
    @FXML private TextField fullNameField;
    @FXML private TextField cpfField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> accountTypeCombo;
    
    @FXML
    private void initialize() {
        // Inicialização do ComboBox
        accountTypeCombo.getItems().addAll(
            "Conta Corrente", 
            "Conta Poupança", 
            "Conta Salário"
        );
        
        // Máscaras para campos
        TextFieldFormatter.applyMask(cpfField, "###.###.###-##");
        TextFieldFormatter.applyMask(phoneField, "(##) #####-####");
    }
    
    @FXML
    private void handleRegister() {
        // Lógica de registro
    }
}
```

## Funcionalidades

1. **Validação de Dados**
   - Máscaras para CPF e telefone
   - Validação de campos obrigatórios
   - Verificação de formato de email

2. **Tipos de Conta**
   - Seleção via ComboBox
   - Opções pré-definidas

3. **Navegação**
   - Botão "Voltar" para tela de login
   - Botão "Cadastrar" para finalizar registro