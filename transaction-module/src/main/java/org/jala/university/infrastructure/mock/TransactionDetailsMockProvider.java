package org.jala.university.infrastructure.mock;

import org.jala.university.application.dto.TransactionDetailsDTO;
import org.jala.university.domain.entity.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Provider for mock transaction details data.
 * Used for demonstration and testing purposes.
 */
public class TransactionDetailsMockProvider implements MockDataProvider<TransactionDetailsDTO> {
    
    private static final Logger LOGGER = Logger.getLogger(TransactionDetailsMockProvider.class.getName());
    private final Map<Long, TransactionDetailsDTO> mockTransactions = new HashMap<>();
    private final Random random = new Random();
    
    public TransactionDetailsMockProvider() {
        // Pré-carregar alguns dados mockados
        initializeMockData();
    }
    
    /**
     * Implements the abstract method from MockDataProvider interface.
     * Provides a list of all mock transaction details.
     *
     * @return a list of mock transaction details
     */
    @Override
    public List<TransactionDetailsDTO> provideMockData() {
        return new ArrayList<>(mockTransactions.values());
    }
    
    /**
     * Initializes mock transaction data.
     */
    private void initializeMockData() {
        // Transação 1: Transferência enviada (débito)
        mockTransactions.put(1L, TransactionDetailsDTO.builder()
            .id(1L)
            .amount(new BigDecimal("150.00"))
            .createdAt(LocalDateTime.now().minusDays(1))
            .status(TransactionStatus.COMPLETED)
            .description("Transferência para João")
            .senderAccountNumber("123456-7")
            .senderName("Seu Nome")
            .receiverAccountNumber("765432-1")
            .receiverName("João Silva")
            .isDebit(true)
            .build());
        
        // Transação 2: Transferência recebida (crédito)
        mockTransactions.put(2L, TransactionDetailsDTO.builder()
            .id(2L)
            .amount(new BigDecimal("500.00"))
            .createdAt(LocalDateTime.now().minusDays(2))
            .status(TransactionStatus.COMPLETED)
            .description("Pagamento de salário")
            .senderAccountNumber("999888-7")
            .senderName("Empresa XYZ")
            .receiverAccountNumber("123456-7")
            .receiverName("Seu Nome")
            .isDebit(false)
            .build());
        
        // Transação 3: Transferência pendente
        mockTransactions.put(3L, TransactionDetailsDTO.builder()
            .id(3L)
            .amount(new BigDecimal("75.50"))
            .createdAt(LocalDateTime.now().minusHours(5))
            .status(TransactionStatus.PENDING)
            .description("Pagamento de conta")
            .senderAccountNumber("123456-7")
            .senderName("Seu Nome")
            .receiverAccountNumber("111222-3")
            .receiverName("Empresa de Água")
            .isDebit(true)
            .build());
        
        // Transação 4: Transferência falha
        mockTransactions.put(4L, TransactionDetailsDTO.builder()
            .id(4L)
            .amount(new BigDecimal("1200.00"))
            .createdAt(LocalDateTime.now().minusDays(3))
            .status(TransactionStatus.FAILED)
            .description("Compra online")
            .senderAccountNumber("123456-7")
            .senderName("Seu Nome")
            .receiverAccountNumber("444555-6")
            .receiverName("Loja Virtual")
            .isDebit(true)
            .build());
        
        // Transação 5: Transferência cancelada
        mockTransactions.put(5L, TransactionDetailsDTO.builder()
            .id(5L)
            .amount(new BigDecimal("350.00"))
            .createdAt(LocalDateTime.now().minusDays(4))
            .status(TransactionStatus.CANCELLED)
            .description("Assinatura anual")
            .senderAccountNumber("123456-7")
            .senderName("Seu Nome")
            .receiverAccountNumber("777888-9")
            .receiverName("Serviço de Streaming")
            .isDebit(true)
            .build());
    }
    
    /**
     * Gets mock transaction details for a specific transaction ID.
     *
     * @param transactionId the ID of the transaction
     * @param currentAccountId the ID of the current account
     * @return a DTO with mock transaction details
     */
    public TransactionDetailsDTO getMockTransactionDetails(Long transactionId, Long currentAccountId) {
        // Verificar se temos dados pré-carregados para este ID
        if (mockTransactions.containsKey(transactionId)) {
            LOGGER.info("Retornando transação mockada pré-carregada: " + transactionId);
            return mockTransactions.get(transactionId);
        }
        
        // Caso contrário, gerar dados aleatórios
        LOGGER.info("Gerando transação mockada aleatória para ID: " + transactionId);
        boolean isDebit = random.nextBoolean();
        TransactionStatus status = getRandomStatus();
        
        String senderName, receiverName;
        String senderAccountNumber, receiverAccountNumber;
        
        if (isDebit) {
            // Se for débito, o usuário atual é o remetente
            senderName = "Seu Nome";
            senderAccountNumber = "123456-7";
            receiverName = getRandomReceiverName();
            receiverAccountNumber = generateRandomAccountNumber();
        } else {
            // Se for crédito, o usuário atual é o destinatário
            receiverName = "Seu Nome";
            receiverAccountNumber = "123456-7";
            senderName = getRandomSenderName();
            senderAccountNumber = generateRandomAccountNumber();
        }
        
        return TransactionDetailsDTO.builder()
                .id(transactionId)
                .amount(generateRandomAmount())
                .createdAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                .status(status)
                .description(generateRandomDescription(isDebit))
                .senderAccountNumber(senderAccountNumber)
                .senderName(senderName)
                .receiverAccountNumber(receiverAccountNumber)
                .receiverName(receiverName)
                .isDebit(isDebit)
                .build();
    }
    
    /**
     * Gets a random transaction status.
     *
     * @return a random transaction status
     */
    private TransactionStatus getRandomStatus() {
        TransactionStatus[] statuses = TransactionStatus.values();
        return statuses[random.nextInt(statuses.length)];
    }
    
    /**
     * Generates a random amount for a transaction.
     *
     * @return a random amount
     */
    private BigDecimal generateRandomAmount() {
        // Gerar um valor entre 10 e 2000
        double amount = 10 + (random.nextDouble() * 1990);
        // Arredondar para 2 casas decimais
        return new BigDecimal(String.format("%.2f", amount));
    }
    
    /**
     * Generates a random description for a transaction.
     *
     * @param isDebit whether the transaction is a debit
     * @return a random description
     */
    private String generateRandomDescription(boolean isDebit) {
        if (isDebit) {
            String[] descriptions = {
                "Pagamento de conta",
                "Transferência para amigo",
                "Compra online",
                "Assinatura mensal",
                "Pagamento de serviço",
                "Transferência bancária",
                "Pagamento de aluguel"
            };
            return descriptions[random.nextInt(descriptions.length)];
        } else {
            String[] descriptions = {
                "Transferência recebida",
                "Pagamento de salário",
                "Reembolso",
                "Depósito",
                "Pagamento de cliente",
                "Transferência bancária",
                "Devolução"
            };
            return descriptions[random.nextInt(descriptions.length)];
        }
    }
    
    /**
     * Gets a random receiver name.
     *
     * @return a random receiver name
     */
    private String getRandomReceiverName() {
        String[] names = {
            "João Silva",
            "Maria Oliveira",
            "Carlos Santos",
            "Ana Pereira",
            "Empresa de Água",
            "Supermercado ABC",
            "Loja Virtual",
            "Serviço de Streaming",
            "Imobiliária XYZ"
        };
        return names[random.nextInt(names.length)];
    }
    
    /**
     * Gets a random sender name.
     *
     * @return a random sender name
     */
    private String getRandomSenderName() {
        String[] names = {
            "Pedro Almeida",
            "Empresa XYZ",
            "Departamento Financeiro",
            "RH Corporativo",
            "Consultoria ABC",
            "Loja Online",
            "Seguradora",
            "Investimentos S.A."
        };
        return names[random.nextInt(names.length)];
    }
    
    /**
     * Generates a random account number.
     *
     * @return a random account number
     */
    private String generateRandomAccountNumber() {
        // Formato: XXXXXX-Y
        int accountBase = 100000 + random.nextInt(900000);
        int digit = 1 + random.nextInt(9);
        return accountBase + "-" + digit;
    }
}