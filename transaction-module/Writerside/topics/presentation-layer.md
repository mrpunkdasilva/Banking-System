# Camada de Apresentação

A camada de apresentação implementa a interface gráfica usando JavaFX e segue o padrão MVVM (Model-View-ViewModel) para separação de responsabilidades.

## Estrutura

```plaintext
presentation/
├── controllers/     # Controladores JavaFX
├── viewmodels/     # ViewModels para binding
└── views/          # Arquivos FXML
```

## Padrão MVVM

1. **View (FXML)**
   - Layout da interface
   - Estilos CSS
   - Bindings declarativos

2. **ViewModel**
   - Estado da tela
   - Lógica de apresentação
   - Comandos e bindings

3. **Controller**
   - Inicialização da View
   - Injeção de dependências
   - Configuração de bindings

## Boas Práticas

1. **Separação de Responsabilidades**
   - View para apresentação
   - ViewModel para estado
   - Controller para coordenação

2. **Reatividade**
   - Properties observáveis
   - Bindings bidirecionais
   - Atualizações automáticas

3. **Validação**
   - Feedback visual
   - Mensagens de erro
   - Estados de loading