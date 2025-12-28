# Views

## Estrutura FXML

### Login View
```xml
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.scene.layout.VBox?>
<?import javafx.scene.control.*?>

<VBox spacing="10" alignment="CENTER" styleClass="login-container">
    <TextField fx:id="emailField" 
               promptText="Email"/>
    <PasswordField fx:id="passwordField" 
                  promptText="Senha"/>
    <Button fx:id="loginButton" 
            text="Entrar"
            onAction="#handleLogin"/>
</VBox>
```

## Estilos CSS

```css
.login-container {
    -fx-padding: 20;
    -fx-background-color: white;
}

.text-field, .password-field {
    -fx-pref-width: 250px;
}

.button {
    -fx-background-color: #2196F3;
    -fx-text-fill: white;
}
```

## Boas Práticas

1. **Layout**
   - Responsivo
   - Acessível
   - Consistente

2. **Estilos**
   - Reutilizáveis
   - Temáveis
   - Manuteníveis

3. **Usabilidade**
   - Feedback visual
   - Tab navigation
   - Atalhos de teclado

## Componentes Comuns

1. **Dialogs**
   - Confirmação
   - Erro
   - Progresso

2. **Forms**
   - Validação visual
   - Campos obrigatórios
   - Auto-complete

3. **Tables**
   - Paginação
   - Ordenação
   - Filtros