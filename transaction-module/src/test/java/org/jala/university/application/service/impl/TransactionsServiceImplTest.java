package org.jala.university.application.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.jala.university.application.dto.TransactionDTO;
import org.jala.university.application.map.TransactionMapper;
import org.jala.university.application.service.interfaces.AccountService;
import org.jala.university.application.service.interfaces.UserService;
import org.jala.university.application.service.impl.mocks.account.AccountMock;
import org.jala.university.application.service.impl.mocks.transactions.TransactionMock;
import org.jala.university.domain.entity.Transaction;
import org.jala.university.domain.repository.TransactionsRepository;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@DisplayName("Transaction service")
class TransactionsServiceImplTest {


    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private TransactionsRepository transactionsRepository;
    @Mock
    private TransactionMapper mapper;
    @Mock
    private UserService userService;
    @Mock
    private AccountService accountService;

    private static MockedStatic<JPAConfig> mockedJPAConfig;

    private TransactionsServiceImpl transactionsService;


    @BeforeEach
    void setUp() {
        mockedJPAConfig = Mockito.mockStatic(JPAConfig.class);
        MockitoAnnotations.openMocks(this);

        when(entityManager.getTransaction()).thenReturn(transaction);

        EntityManagerFactory mockFactory = mock(EntityManagerFactory.class);
        when(mockFactory.createEntityManager()).thenReturn(entityManager);
        mockedJPAConfig.when(JPAConfig::getEntityManagerFactory).thenReturn(mockFactory);
        this.transactionsService = Mockito.spy(new TransactionsServiceImpl());


        setField(transactionsService, "entityManager", entityManager);
        setField(transactionsService, "transactionsRepository", transactionsRepository);
        setField(transactionsService, "accountService", accountService);
        setField(transactionsService, "map", mapper);
        setField(transactionsService, "userService", userService);
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = TransactionsServiceImpl.class.getDeclaredField(fieldName);
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
    @DisplayName("Should return all transactions from a user successfully")
    void findAll() {
        Long userId = 1L;

        Transaction expectedResponse = TransactionMock.createAValidTransaction();

        when(transactionsRepository.findAllByUserId(anyLong())).thenReturn(
                List.of(expectedResponse)
        );
        when(mapper.mapTo(any(Transaction.class)))
                .thenReturn(TransactionMock.createAValidTransactionDTO());

        List<TransactionDTO> response = this.transactionsService.findAll(userId);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals(response.get(0).getAmount(), expectedResponse.getAmount());
        assertEquals(response.get(0).getStatus(), expectedResponse.getStatus());
    }

    @Test
    @DisplayName("should find a transaction by id successfully")
    void findById() {
        Long userId = 1L;

        Transaction expectedResponse = TransactionMock.createAValidTransaction();

        when(transactionsRepository.findById(anyLong())).thenReturn(
                expectedResponse
        );
        when(mapper.mapTo(any(Transaction.class)))
                .thenReturn(TransactionMock.createAValidTransactionDTO());

        TransactionDTO response = this.transactionsService.findById(userId);

        assertNotNull(response);
        assertEquals(response.getAmount(), expectedResponse.getAmount());
        assertEquals(response.getStatus(), expectedResponse.getStatus());
    }

    @Test
    @DisplayName("Should save a transaction successfully")
    void save() {
        when(transactionsRepository.save(any(Transaction.class)))
                .thenReturn(TransactionMock.createAValidTransaction());

        when(accountService.getAccountByUserId(anyLong())).thenReturn(
                AccountMock.createAValidAccountDTO()
        );

        assertDoesNotThrow(() -> this.transactionsService.save(TransactionMock.createAValidTransactionDTO()));
    }

    @Test
    @DisplayName("Should delete a transaction successfully")
    void delete() {
        Long userId = 1L;

        when(transactionsRepository.findById(anyLong())).thenReturn(TransactionMock.createAValidTransaction());

        assertDoesNotThrow(() -> this.transactionsService.delete(userId));
    }
}