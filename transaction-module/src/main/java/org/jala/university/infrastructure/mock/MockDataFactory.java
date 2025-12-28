package org.jala.university.infrastructure.mock;

import org.jala.university.application.dto.TransactionHistoryDTO;

/**
 * Factory for creating mock data providers.
 * Centralizes the creation of different mock data providers.
 */
public class MockDataFactory {
    
    private static final TransactionHistoryMockProvider transactionHistoryMockProvider = new TransactionHistoryMockProvider();
    private static final TransactionDetailsMockProvider transactionDetailsMockProvider = new TransactionDetailsMockProvider();
    
    /**
     * Gets the transaction history mock data provider.
     *
     * @return the transaction history mock data provider
     */
    public static TransactionHistoryMockProvider getTransactionHistoryMockProvider() {
        return transactionHistoryMockProvider;
    }
    
    /**
     * Gets the transaction details mock data provider.
     *
     * @return the transaction details mock data provider
     */
    public static TransactionDetailsMockProvider getTransactionDetailsMockProvider() {
        return transactionDetailsMockProvider;
    }
    
    /**
     * Utility method to check if mock data should be used.
     * This can be controlled by configuration or environment variables.
     *
     * @return true if mock data should be used, false otherwise
     */
    public static boolean shouldUseMockData() {
        // Aqui você pode implementar lógica para determinar se deve usar dados mockados
        // Por exemplo, baseado em variáveis de ambiente, configurações, etc.
        
        // Por enquanto, vamos sempre retornar true para fins de demonstração
        return true;
    }
}