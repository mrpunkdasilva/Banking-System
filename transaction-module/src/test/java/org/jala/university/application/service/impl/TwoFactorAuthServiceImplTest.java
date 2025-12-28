package org.jala.university.application.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.jala.university.application.map.TwoFactorCodeMapper;
import org.jala.university.application.map.UserMapper;
import org.jala.university.application.service.EmailService;
import org.jala.university.domain.entity.TwoFactorCode;
import org.jala.university.domain.entity.User;
import org.jala.university.domain.repository.TwoFactorCodeRepository;
import org.jala.university.domain.repository.UserRepository;
import org.jala.university.infrastructure.config.JPAConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TwoFactorAuthServiceImpl class.
 * Tests two-factor authentication operations and related functionality.
 */
class TwoFactorAuthServiceImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TwoFactorCodeRepository twoFactorCodeRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private TwoFactorCodeMapper twoFactorCodeMapper;

    @Mock
    private EmailService emailService;

    @Mock
    private SecureRandom secureRandom;

    private TwoFactorAuthServiceImpl twoFactorAuthService;
    
    private static MockedStatic<JPAConfig> mockedJPAConfig;

    @BeforeAll
    static void setUpClass() {
        // Mock the static methods of JPAConfig to avoid database connection
        mockedJPAConfig = Mockito.mockStatic(JPAConfig.class);
    }
    
    @AfterAll
    static void tearDownClass() {
        // Close the static mock to prevent memory leaks
        if (mockedJPAConfig != null) {
            mockedJPAConfig.close();
        }
    }
    
    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(entityManager.getTransaction()).thenReturn(transaction);
        
        // Mock JPAConfig
        EntityManagerFactory mockFactory = mock(EntityManagerFactory.class);
        when(mockFactory.createEntityManager()).thenReturn(entityManager);
        mockedJPAConfig.when(JPAConfig::getEntityManagerFactory).thenReturn(mockFactory);

        // Create an instance of TwoFactorAuthServiceImpl
        twoFactorAuthService = Mockito.spy(new TwoFactorAuthServiceImpl());

        // Use reflection to set the mocked dependencies
        setField(twoFactorAuthService, "entityManager", entityManager);
        setField(twoFactorAuthService, "userRepository", userRepository);
        setField(twoFactorAuthService, "twoFactorCodeRepository", twoFactorCodeRepository);
        setField(twoFactorAuthService, "userMapper", userMapper);
        setField(twoFactorAuthService, "twoFactorCodeMapper", twoFactorCodeMapper);
        setField(twoFactorAuthService, "emailService", emailService);
        setField(twoFactorAuthService, "random", secureRandom);
    }
    
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = TwoFactorAuthServiceImpl.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    /**
     * Test checking if two-factor authentication is enabled for a user.
     * Verifies that the method returns true when 2FA is enabled.
     */
    @Test
    void shouldReturnTrueWhenTwoFactorIsEnabled() {
        // Arrange
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .name("John Doe")
                .email("john@example.com")
                .twoFactorEnabled(true)
                .build();
        
        when(userRepository.findById(userId)).thenReturn(user);

        // Act
        boolean result = twoFactorAuthService.isTwoFactorEnabled(userId);

        // Assert
        assertTrue(result);
        
        // Verify
        verify(userRepository).findById(userId);
    }

    /**
     * Test checking if two-factor authentication is disabled for a user.
     * Verifies that the method returns false when 2FA is disabled.
     */
    @Test
    void shouldReturnFalseWhenTwoFactorIsDisabled() {
        // Arrange
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .name("John Doe")
                .email("john@example.com")
                .twoFactorEnabled(false)
                .build();
        
        when(userRepository.findById(userId)).thenReturn(user);

        // Act
        boolean result = twoFactorAuthService.isTwoFactorEnabled(userId);

        // Assert
        assertFalse(result);
        
        // Verify
        verify(userRepository).findById(userId);
    }

    /**
     * Test checking if two-factor authentication returns false when user is not found.
     * Verifies that the method returns false when the user doesn't exist.
     */
    @Test
    void shouldReturnFalseWhenUserNotFound() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(null);

        // Act
        boolean result = twoFactorAuthService.isTwoFactorEnabled(userId);

        // Assert
        assertFalse(result);
        
        // Verify
        verify(userRepository).findById(userId);
    }

    /**
     * Test enabling two-factor authentication for a user.
     * Verifies that the method returns true when 2FA is successfully enabled.
     */
    @Test
    void shouldEnableTwoFactorAuthentication() {
        // Arrange
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .name("John Doe")
                .email("john@example.com")
                .twoFactorEnabled(false)
                .build();
        
        when(userRepository.findById(userId)).thenReturn(user);

        // Act
        boolean result = twoFactorAuthService.setTwoFactorEnabled(userId, true);

        // Assert
        assertTrue(result);
        assertTrue(user.isTwoFactorEnabled());
        
        // Verify
        verify(userRepository).findById(userId);
        verify(transaction).begin();
        verify(entityManager).merge(user);
        verify(transaction).commit();
    }

    /**
     * Test disabling two-factor authentication for a user.
     * Verifies that the method returns true when 2FA is successfully disabled.
     */
    @Test
    void shouldDisableTwoFactorAuthentication() {
        // Arrange
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .name("John Doe")
                .email("john@example.com")
                .twoFactorEnabled(true)
                .build();
        
        when(userRepository.findById(userId)).thenReturn(user);

        // Act
        boolean result = twoFactorAuthService.setTwoFactorEnabled(userId, false);

        // Assert
        assertTrue(result);
        assertFalse(user.isTwoFactorEnabled());
        
        // Verify
        verify(userRepository).findById(userId);
        verify(transaction).begin();
        verify(entityManager).merge(user);
        verify(transaction).commit();
    }

    /**
     * Test setting two-factor authentication when user is not found.
     * Verifies that the method returns false when the user doesn't exist.
     */
    @Test
    void shouldReturnFalseWhenSettingTwoFactorForNonExistentUser() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(null);
        
        // Act
        boolean result = twoFactorAuthService.setTwoFactorEnabled(userId, true);

        // Assert
        assertFalse(result);
        
        // Verify
        verify(userRepository).findById(userId);
        verify(transaction).begin();
        verify(entityManager, never()).merge(any(User.class));
    }

    /**
     * Test handling exception when setting two-factor authentication.
     * Verifies that the method returns false and rolls back the transaction when an exception occurs.
     */
    @Test
    void shouldHandleExceptionWhenSettingTwoFactorAuthentication() {
        // Arrange
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .name("John Doe")
                .email("john@example.com")
                .twoFactorEnabled(false)
                .build();
        
        when(userRepository.findById(userId)).thenReturn(user);
        doThrow(new RuntimeException("Database error")).when(entityManager).merge(user);
        when(transaction.isActive()).thenReturn(true);

        // Act
        boolean result = twoFactorAuthService.setTwoFactorEnabled(userId, true);

        // Assert
        assertFalse(result);
        
        // Verify
        verify(userRepository).findById(userId);
        verify(transaction).begin();
        verify(entityManager).merge(user);
        verify(transaction).isActive();
        verify(transaction).rollback();
    }

    /**
     * Test generating and sending a verification code.
     * Verifies that the method returns true when the code is successfully generated and sent.
     */
    @Test
    void shouldGenerateAndSendCode() {
        // Arrange
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .name("John Doe")
                .email("john@example.com")
                .twoFactorEnabled(true)
                .build();
        
        String generatedCode = "123456";
        
        when(userRepository.findById(userId)).thenReturn(user);
        when(secureRandom.nextInt(900000)).thenReturn(23456);
        when(emailService.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        // Act
        boolean result = twoFactorAuthService.generateAndSendCode(userId);

        // Assert
        assertTrue(result);
        
        // Verify
        verify(userRepository).findById(userId);
        verify(transaction).begin();
        verify(entityManager).persist(any(TwoFactorCode.class));
        verify(transaction).commit();
        verify(emailService).sendEmail(eq("john@example.com"), anyString(), contains("123456"));
    }

    /**
     * Test generating and sending a code when user is not found.
     * Verifies that the method returns false when the user doesn't exist.
     */
    @Test
    void shouldReturnFalseWhenGeneratingCodeForNonExistentUser() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(null);

        // Act
        boolean result = twoFactorAuthService.generateAndSendCode(userId);

        // Assert
        assertFalse(result);
        
        // Verify
        verify(userRepository).findById(userId);
        verify(transaction).begin();
        verify(entityManager, never()).persist(any(TwoFactorCode.class));

        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }
}