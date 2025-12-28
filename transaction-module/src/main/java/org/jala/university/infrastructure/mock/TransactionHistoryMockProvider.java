package org.jala.university.infrastructure.mock;

import org.jala.university.application.dto.TransactionHistoryDTO;
import org.jala.university.domain.entity.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Provider for mock transaction history data.
 * Generates realistic transaction data for testing and demonstration purposes.
 */
public class TransactionHistoryMockProvider implements MockDataProvider<TransactionHistoryDTO> {

    @Override
    public List<TransactionHistoryDTO> provideMockData() {
        List<TransactionHistoryDTO> mockTransactions = new ArrayList<>();
        
        // Transação 1: Transferência enviada
        mockTransactions.add(TransactionHistoryDTO.builder()
            .id(1L)
            .status(TransactionStatus.COMPLETED)
            .amount(new BigDecimal("150.00"))
            .date(LocalDateTime.now().minusDays(1))
            .description("Transferência para João")
            .counterpartyName("João Silva")
            .debit(true)
            .build());
        
        // Transação 2: Transferência recebida
        mockTransactions.add(TransactionHistoryDTO.builder()
            .id(2L)
            .status(TransactionStatus.COMPLETED)
            .amount(new BigDecimal("500.00"))
            .date(LocalDateTime.now().minusDays(2))
            .description("Pagamento de salário")
            .counterpartyName("Empresa XYZ")
            .debit(false)
            .build());
        
        // Transação 3: Transferência pendente
        mockTransactions.add(TransactionHistoryDTO.builder()
            .id(3L)
            .status(TransactionStatus.PENDING)
            .amount(new BigDecimal("75.50"))
            .date(LocalDateTime.now().minusHours(5))
            .description("Pagamento de conta")
            .counterpartyName("Companhia de Água")
            .debit(true)
            .build());
        
        // Transação 4: Transferência falha
        mockTransactions.add(TransactionHistoryDTO.builder()
            .id(4L)
            .status(TransactionStatus.FAILED)
            .amount(new BigDecimal("1200.00"))
            .date(LocalDateTime.now().minusDays(3))
            .description("Transferência internacional")
            .counterpartyName("Maria Gonzalez")
            .debit(true)
            .build());
        
        // Transação 5: Transferência cancelada
        mockTransactions.add(TransactionHistoryDTO.builder()
            .id(5L)
            .status(TransactionStatus.CANCELLED)
            .amount(new BigDecimal("250.00"))
            .date(LocalDateTime.now().minusDays(4))
            .description("Assinatura anual")
            .counterpartyName("Streaming Plus")
            .debit(true)
            .build());
            
        return mockTransactions;
    }
    
    /**
     * Filtra as transações mockadas com base nos critérios fornecidos.
     * 
     * @param startDate Data inicial
     * @param endDate Data final
     * @param status Status da transação (ou null para todos)
     * @return Lista de transações mockadas filtradas
     */
    public List<TransactionHistoryDTO> filterMockData(LocalDateTime startDate, LocalDateTime endDate, TransactionStatus status) {
        List<TransactionHistoryDTO> allMockTransactions = provideMockData();
        
        return allMockTransactions.stream()
            .filter(t -> {
                // Filtro de data
                boolean dateMatch = (t.getDate().isEqual(startDate) || t.getDate().isAfter(startDate)) 
                                 && (t.getDate().isEqual(endDate) || t.getDate().isBefore(endDate));
                
                // Filtro de status
                boolean statusMatch = status == null || t.getStatus() == status;
                
                return dateMatch && statusMatch;
            })
            .toList();
    }
}