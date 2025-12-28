package org.jala.university.application.service.impl;

import jakarta.persistence.EntityManager;
import org.jala.university.application.map.TwoFactorCodeMapper;
import org.jala.university.application.map.UserMapper;
import org.jala.university.application.service.EmailService;
import org.jala.university.application.service.TwoFactorAuthService;
import org.jala.university.domain.entity.TwoFactorCode;
import org.jala.university.domain.entity.User;
import org.jala.university.domain.repository.TwoFactorCodeRepository;
import org.jala.university.domain.repository.UserRepository;
import org.jala.university.infrastructure.config.JPAConfig;
import org.jala.university.infrastructure.persistence.TwoFactorCodeRepositoryImpl;
import org.jala.university.infrastructure.persistence.UserRepositoryImp;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the TwoFactorAuthService.
 */
public class TwoFactorAuthServiceImpl implements TwoFactorAuthService {

    private static final Logger LOGGER = Logger.getLogger(TwoFactorAuthServiceImpl.class.getName());

    private final UserRepository userRepository;
    private final TwoFactorCodeRepository twoFactorCodeRepository;
    private final UserMapper userMapper;
    private final TwoFactorCodeMapper twoFactorCodeMapper;
    private final EmailService emailService;
    private final EntityManager entityManager;
    private final SecureRandom random;

    /**
     * Constructor initializing the required dependencies.
     */
    public TwoFactorAuthServiceImpl() {
        this.entityManager = JPAConfig.getEntityManagerFactory().createEntityManager();
        this.userRepository = new UserRepositoryImp(entityManager);
        this.twoFactorCodeRepository = new TwoFactorCodeRepositoryImpl(entityManager);
        this.userMapper = new UserMapper();
        this.twoFactorCodeMapper = new TwoFactorCodeMapper();

        // Configurar o serviço de email real
        Properties emailProps = loadEmailProperties();
        String smtpHost = emailProps.getProperty("mail.smtp.host", "smtp.gmail.com");
        int smtpPort = Integer.parseInt(emailProps.getProperty("mail.smtp.port", "587"));
        String username = emailProps.getProperty("mail.username", "");
        String password = emailProps.getProperty("mail.password", "");
        boolean debug = Boolean.parseBoolean(emailProps.getProperty("mail.debug", "true"));

        this.emailService = new EmailServiceImpl(username, password, smtpHost, smtpPort, debug);
        this.random = new SecureRandom();
    }

    /**
     * Carrega as propriedades de email do arquivo de configuração.
     *
     * @return Properties com as configurações de email
     */
    private Properties loadEmailProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("properties/email.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                LOGGER.warning("Arquivo email.properties não encontrado. Usando configurações padrão.");
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar configurações de email", ex);
        }
        return props;
    }

    /**
     * Checks if two-factor authentication is enabled for a user.
     *
     * @param userId the user ID
     * @return true if 2FA is enabled, false otherwise
     */
    @Override
    public boolean isTwoFactorEnabled(Long userId) {
        User user = userRepository.findById(userId);
        return user != null && user.isTwoFactorEnabled();
    }

    /**
     * Enables or disables two-factor authentication for a user.
     *
     * @param userId  the user ID
     * @param enabled whether 2FA should be enabled
     * @return true if the operation was successful, false otherwise
     */
    @Override
    public boolean setTwoFactorEnabled(Long userId, boolean enabled) {
        try {
            entityManager.getTransaction().begin();

            User user = userRepository.findById(userId);
            if (user == null) {
                entityManager.getTransaction().rollback();
                return false;
            }

            user.setTwoFactorEnabled(enabled);

            try {
                entityManager.merge(user);
            } catch (Exception mergeException) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                LOGGER.log(Level.SEVERE, "Error merging user entity: " + user.getId() + ", Error: " + mergeException.getMessage(), mergeException);
                return false;
            }

            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            LOGGER.log(Level.SEVERE, "Error setting two-factor authentication", e);
            return false;
        }
    }


    /**
     * Generates and sends a verification code to the user.
     *
     * @param userId the user ID
     * @return true if the code was generated and sent successfully, false otherwise
     */
    @Override
    public boolean generateAndSendCode(Long userId) {
        try {
            entityManager.getTransaction().begin();

            User user = userRepository.findById(userId);
            if (user == null) {
                entityManager.getTransaction().rollback();
                return false;
            }

            String code = generateRandomCode();

            TwoFactorCode twoFactorCode = new TwoFactorCode();
            twoFactorCode.setUser(user);
            twoFactorCode.setCode(code);
            twoFactorCode.setExpiresAt(LocalDateTime.now().plusMinutes(10));

            entityManager.persist(twoFactorCode);
            entityManager.getTransaction().commit();

            String subject = "Código de Verificação - Sistema Bancário";
            String body = "Seu código de verificação é: " + code + "\n\n" +
                    "Este código expira em 10 minutos.";

            return emailService.sendEmail(user.getEmail(), subject, body);
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            LOGGER.log(Level.SEVERE, "Error generating and sending verification code", e);
            return false;
        }
    }

    /**
     * Verifies a two-factor authentication code.
     *
     * @param userId the user ID
     * @param code   the verification code
     * @return true if the code is valid, false otherwise
     */
    @Override
    public boolean verifyCode(Long userId, String code) {
        try {
            User user = userRepository.findById(userId);
            if (user == null) {
                return false;
            }

            TwoFactorCode twoFactorCode = twoFactorCodeRepository.findLatestValidCode(userId);

            if (twoFactorCode == null) {
                return false;
            }

            if (twoFactorCode.getExpiresAt().isBefore(LocalDateTime.now())) {
                return false;
            }

            return twoFactorCode.getCode().equals(code);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error verifying code", e);
            return false;
        }
    }

    /**
     * Resends the verification code to the user.
     *
     * @param userId the user ID
     * @return true if the code was resent successfully, false otherwise
     */
    @Override
    public boolean resendCode(Long userId) {
        try {
            User user = userRepository.findById(userId);
            if (user == null) {
                return false;
            }

            TwoFactorCode twoFactorCode = twoFactorCodeRepository.findLatestValidCode(userId);

            if (twoFactorCode == null || twoFactorCode.getExpiresAt().isBefore(LocalDateTime.now())) {
                generateAndSendCode(userId);
            }

            String subject = "Código de Verificação - Sistema Bancário";
            String body = "Seu código de verificação é: " + twoFactorCode.getCode() + "\n\n" +
                    "Este código expira em " +
                    java.time.Duration.between(LocalDateTime.now(), twoFactorCode.getExpiresAt()).toMinutes() +
                    " minutos.";

            return emailService.sendEmail(user.getEmail(), subject, body);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error resending verification code", e);
            return false;
        }
    }

    /**
     * Generates a random 6-digit code.
     *
     * This method uses a SecureRandom instance to generate a random integer
     * between 100000 and 999999, inclusive. The generated integer is then
     * converted to a string and returned.
     *
     * @return the generated 6-digit code as a string
     */
    private String generateRandomCode() {
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
