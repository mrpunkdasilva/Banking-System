# Guia de Checkstyle

## Visão Geral

O Checkstyle é uma ferramenta de análise de código para Java que ajuda a garantir que o código siga determinados padrões de codificação. Este projeto utiliza o Checkstyle para manter a consistência e qualidade do código em todo o repositório.

## Configuração

O arquivo `checkstyle.xml` na raiz do projeto define as regras que o código deve seguir. Estas regras estão configuradas com severidade "error", o que significa que as violações impedirão a compilação do projeto.

## Categorias de Regras

### Espaços em Branco

Estas regras controlam a formatação do código em termos de espaços, indentação e posicionamento de chaves:

```xml
<module name="FileTabCharacter">
    <property name="eachLine" value="true"/>
</module>
<module name="WhitespaceAround"/>
<module name="WhitespaceAfter"/>
<module name="EmptyForIteratorPad"/>
<module name="GenericWhitespace"/>
<module name="MethodParamPad"/>
<module name="NoWhitespaceAfter"/>
<module name="NoWhitespaceBefore"/>
<module name="OperatorWrap"/>
<module name="ParenPad"/>
<module name="TypecastParenPad"/>
```

### Nomenclatura

Estas regras garantem que os nomes de classes, métodos, variáveis e constantes sigam as convenções Java padrão:

```xml
<module name="ConstantName"/>
<module name="LocalFinalVariableName"/>
<module name="LocalVariableName"/>
<module name="MemberName"/>
<module name="MethodName"/>
<module name="PackageName"/>
<module name="ParameterName"/>
<module name="StaticVariableName"/>
<module name="TypeName"/>
```

### Javadoc

Estas regras exigem documentação Javadoc para métodos, classes e variáveis públicas:

```xml
<module name="JavadocMethod">
    <property name="accessModifiers" value="public"/>
</module>
<module name="JavadocType">
    <property name="scope" value="public"/>
</module>
<module name="MissingJavadocType">
    <property name="scope" value="public"/>
</module>
<module name="JavadocVariable">
    <property name="scope" value="public"/>
</module>
<module name="JavadocStyle"/>
```

### Tamanho de Código

Estas regras limitam o tamanho do código para manter a legibilidade:

```xml
<!-- Fora do TreeWalker -->
<module name="LineLength">
    <property name="max" value="120"/>
</module>
<module name="FileLength">
    <property name="max" value="2000"/>
</module>

<!-- Dentro do TreeWalker -->
<module name="MethodLength">
    <property name="max" value="150"/>
</module>
<module name="ParameterNumber">
    <property name="max" value="7"/>
</module>
```

## Como Usar o Checkstyle

### Com Maven

Para verificar o código com o Checkstyle usando Maven:

```bash
mvn checkstyle:check
```

O Checkstyle também é executado automaticamente durante a fase de validação do ciclo de vida do Maven:

```bash
mvn validate
```

> **Nota**: Atualmente, o plugin está configurado para não falhar a compilação em caso de erros de Checkstyle (`failsOnError: false` e `failOnViolation: false`). Isso permite uma adoção gradual das regras. No futuro, quando a maioria dos erros for corrigida, essas configurações serão alteradas para `true` para garantir que novos erros não sejam introduzidos.

### Com IDE

#### IntelliJ IDEA

1. Instale o plugin "CheckStyle-IDEA" através do marketplace de plugins
2. Vá para Settings > Tools > Checkstyle
3. Adicione o arquivo `checkstyle.xml` como configuração
4. Ative a verificação em tempo real

#### Eclipse

1. Instale o plugin "Checkstyle" através do Eclipse Marketplace
2. Vá para Window > Preferences > Checkstyle
3. Adicione o arquivo `checkstyle.xml` como configuração
4. Ative a verificação em tempo real

## Resolução de Problemas Comuns

### Linhas Muito Longas

```java
// Incorreto: Linha muito longa
String message = "Este é um exemplo de uma linha muito longa que excede o limite de 120 caracteres e causará uma violação do Checkstyle";

// Correto: Quebra de linha
String message = "Este é um exemplo de uma linha muito longa que foi quebrada " +
                "para respeitar o limite de 120 caracteres";
```

### Falta de Javadoc

```java
// Incorreto: Método público sem Javadoc
public void processTransaction(Transaction transaction) {
    // implementação
}

// Correto: Método público com Javadoc
/**
 * Processa uma transação financeira.
 *
 * @param transaction A transação a ser processada
 */
public void processTransaction(Transaction transaction) {
    // implementação
}
```

### Nomenclatura Incorreta

```java
// Incorreto: Nome de constante em camelCase
private static final int maxAmount = 1000;

// Correto: Nome de constante em SNAKE_CASE
private static final int MAX_AMOUNT = 1000;
```

## Estratégia de Adoção Gradual

Como o projeto já possui uma base de código existente, adotamos uma estratégia de implementação gradual do Checkstyle:

1. **Fase 1: Monitoramento** (Atual)
   - O Checkstyle está configurado para não falhar a compilação (`failsOnError: false`)
   - Os desenvolvedores são encorajados a corrigir os erros em arquivos que modificam
   - Relatórios regulares de progresso são gerados

2. **Fase 2: Correção por Módulo**
   - Cada módulo ou pacote será abordado sistematicamente
   - Sprints dedicados para correção de erros de Checkstyle
   - Prioridade para classes públicas e APIs

3. **Fase 3: Aplicação Rigorosa**
   - Após a maioria dos erros ser corrigida, `failsOnError` será alterado para `true`
   - Novos erros não serão permitidos
   - Integração com CI/CD para verificação automática

### Relatório de Erros

Para gerar um relatório HTML dos erros de Checkstyle:

```bash
mvn checkstyle:checkstyle
```

O relatório será gerado em `target/site/checkstyle.html`.

## Considerações Importantes

1. **Não desative as regras**: As regras foram cuidadosamente selecionadas para manter a qualidade do código. Não desative-as sem discussão prévia com a equipe.

2. **Formatação automática**: Configure seu IDE para formatar o código automaticamente de acordo com as regras do Checkstyle.

3. **Verificação antes do commit**: Execute o Checkstyle antes de fazer commit das alterações para evitar problemas na integração contínua.

4. **Atualizações**: A configuração do Checkstyle pode ser atualizada conforme o projeto evolui. Fique atento às comunicações da equipe sobre mudanças nas regras.

5. **Correção gradual**: Ao modificar um arquivo existente, aproveite para corrigir os erros de Checkstyle nesse arquivo.