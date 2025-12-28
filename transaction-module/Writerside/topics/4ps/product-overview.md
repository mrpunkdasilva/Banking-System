# Visão Geral do Produto

## Especificações

### Descrição do problema
É necessário ter um módulo específico para gerenciar as transações a fim de garantir segurança, consistência e confiabilidade na hora de realizar transações dentro da rede bancária.

### Diagrama de casos de uso
O diagrama de casos de uso do módulo de transações ilustra as interações entre os usuários (clientes) e o sistema, destacando as principais funcionalidades oferecidas. Abaixo estão os casos de uso previstos:

- Realizar transação via QR Code
- Realizar transação via NFC
- Consultar histórico de transações
- Gerenciar beneficiários (adicionar, editar, remover)
- Agendar transferência
- Validar dados do beneficiário
- Realizar transação entre contas
- Autenticar transferência com senha
- Autenticação multifator (MFA) para transações de alto valor

## Tecnologias
- Java
- JFX
- Spring Boot
- MySQL
- Docker

## Alcance
O módulo em desenvolvimento será uma extensão de um sistema financeiro digital voltado para a realização de transações seguras e práticas por meio de tecnologias modernas como QR Code e NFC. Ele visa oferecer ao cliente uma experiência de transferência simplificada, segura e personalizada, com funcionalidades essenciais de autenticação, gestão e histórico.

## Funcionalidades Principais
- **Transações via QR Code e NFC:**  
  O módulo permitirá que os clientes realizem pagamentos e transferências de forma rápida, usando leitura de QR Codes e aproximação por tecnologia NFC.

- **Gestão de Beneficiários:**  
  Os usuários poderão adicionar, editar e remover beneficiários, com validação automática dos dados para maior segurança.

- **Agendamento de Transferências:**  
  O cliente poderá programar transferências para datas futuras, com possibilidade de visualização e cancelamento.

- **Histórico de Transações:**  
  O sistema manterá um histórico completo de todas as transações realizadas, acessível ao cliente a qualquer momento.

- **Segurança Avançada:**  
  - Requisição de senha para efetivação de transferências.  
  - Autenticação multifator (MFA) para transações de alto valor, como medida de proteção adicional.  
  - Validação dos dados do beneficiário antes da conclusão da transação.

- **Operações em Contas:**  
  Os clientes poderão realizar transações entre contas cadastradas, com suporte a diferentes instituições bancárias.

## Limitações
- O módulo não cobre funcionalidades de investimento, crédito ou suporte a moedas estrangeiras.
- A autenticação multifator será aplicada apenas em transações acima de um valor pré-configurado (a definir pelo negócio).