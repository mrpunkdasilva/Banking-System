package org.jala.university.application.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.jala.university.application.dto.TransactionDetailsDTO;
import org.jala.university.application.service.impl.TransactionDetailsServiceImpl;
import org.jala.university.application.service.impl.mocks.transactions.TransactionMock;
import org.jala.university.domain.entity.Transaction;
import org.jala.university.domain.repository.TransactionRepository;
import org.jala.university.infrastructure.config.JPAConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Transaction details service")
class TransactionDetailsServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Mock
    private NumberFormat currencyFormatter;

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;


    private TransactionDetailsServiceImpl service;

    private static MockedStatic<JPAConfig> mockedJPAConfig;


    @BeforeEach
    void setUp() {
        mockedJPAConfig = Mockito.mockStatic(JPAConfig.class);

        MockitoAnnotations.openMocks(this);

        when(entityManager.getTransaction()).thenReturn(transaction);

        // Mock JPAConfig
        EntityManagerFactory mockFactory = mock(EntityManagerFactory.class);
        when(mockFactory.createEntityManager()).thenReturn(entityManager);
        mockedJPAConfig.when(JPAConfig::getEntityManagerFactory).thenReturn(mockFactory);

        // Create an instance of TwoFactorAuthServiceImpl
        this.service = Mockito.spy(new TransactionDetailsServiceImpl());
        setField(service, "transactionRepository", transactionRepository);

    }


    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = TransactionDetailsServiceImpl.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }



    @AfterEach
    void tearDown() {
        if (mockedJPAConfig != null) {
            mockedJPAConfig.close();
        }
    }

    @Test
    @DisplayName("Should get transaction details of a transaction")
    void getTransactionDetails() {
        Transaction expectedResponse = TransactionMock.createAValidTransaction();
        when(this.transactionRepository.findById(anyLong()))
                .thenReturn(expectedResponse);

        TransactionDetailsDTO response = this.service.getTransactionDetails(1L);

        assertNotNull(response);
        assertEquals(response.getAmount(), expectedResponse.getAmount());
    }

    @Test
    @DisplayName("Should get transaction detail by account id successfully")
    void testGetTransactionDetails() {
        Transaction expectedResponse = TransactionMock.createAValidTransaction();
        when(this.transactionRepository.findById(anyLong()))
                .thenReturn(expectedResponse);

        TransactionDetailsDTO response = this.service.getTransactionDetails(1L, 1L);

        assertNotNull(response);
        assertEquals(response.getAmount(), expectedResponse.getAmount());
    }
}