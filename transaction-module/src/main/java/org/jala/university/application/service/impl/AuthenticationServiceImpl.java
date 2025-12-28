package org.jala.university.application.service.impl;

import jakarta.persistence.EntityManager;
import org.jala.university.application.dto.TwoFactorVerificationDTO;
import org.jala.university.application.dto.UserDTO;
import org.jala.university.application.map.UserMapper;
import org.jala.university.application.service.AuthenticationService;
import org.jala.university.application.service.TwoFactorAuthService;
import org.jala.university.domain.entity.User;
import org.jala.university.domain.repository.UserRepository;
import org.jala.university.infrastructure.config.JPAConfig;
import org.jala.university.infrastructure.persistence.UserRepositoryImp;
import org.jala.university.presentation.util.SessionManager;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the AuthenticationService.
 */
public class AuthenticationServiceImpl implements AuthenticationService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TwoFactorAuthService twoFactorAuthService;
    private final EntityManager entityManager;
    
    /**
     * Constructor initializing the required dependencies.
     */
    public AuthenticationServiceImpl() {
        this.entityManager = JPAConfig.getEntityManagerFactory().createEntityManager();
        this.userRepository = new UserRepositoryImp(entityManager);
        this.userMapper = new UserMapper();
        this.twoFactorAuthService = new TwoFactorAuthServiceImpl();
    }
    
    /**
     * Authenticates a user with email and password.
     * 
     * @param email the user's email
     * @param password the user's password
     * @return authentication token or null if 2FA is required
     */
    @Override
    public String authenticate(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        User user = userOptional.get();
        
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new RuntimeException("Senha incorreta");
        }
        
        UserDTO userDTO = userMapper.mapTo(user);
        
        // Atualizar ambos os SessionManagers
        // SessionManager da presentation
        SessionManager.getInstance().setCurrentUser(userDTO);
        
        // SessionManager da infrastructure (usado pelo DashboardController)
        org.jala.university.infrastructure.utils.SessionManager.setCurrentUser(userDTO);
        
        // Obter o ID da conta do usuário (assumindo que ele tem pelo menos uma conta)
        if (user.getAccounts() != null && !user.getAccounts().isEmpty()) {
            Long accountId = user.getAccounts().get(0).getId();
            org.jala.university.infrastructure.utils.SessionManager.setCurrentAccountId(accountId);
            System.out.println("Definindo ID da conta: " + accountId);
        } else {
            System.out.println("Usuário não possui contas associadas");
        }
        
        // Verificar se 2FA está habilitado
        if (twoFactorAuthService.isTwoFactorEnabled(user.getId())) {
            // Gerar e enviar código
            twoFactorAuthService.generateAndSendCode(user.getId());
            return null; // Indica que 2FA é necessário
        }
        
        // 2FA não está habilitado, autenticar diretamente
        String token = generateToken();
        SessionManager.getInstance().setToken(token);
        org.jala.university.infrastructure.utils.SessionManager.setAuthToken(token);
        return token;
    }
    
    /**
     * Verifies a two-factor authentication code.
     * 
     * @param verificationDTO the verification data
     * @return authentication token or null if verification fails
     */
    @Override
    public String verifyTwoFactor(TwoFactorVerificationDTO verificationDTO) {
        boolean isValid = twoFactorAuthService.verifyCode(
                verificationDTO.getUserId(), 
                verificationDTO.getCode()
        );
        
        if (!isValid) {
            return null;
        }
        
        // Obter o usuário e suas contas
        User user = userRepository.findById(verificationDTO.getUserId());
        if (user != null && user.getAccounts() != null && !user.getAccounts().isEmpty()) {
            Long accountId = user.getAccounts().get(0).getId();
            org.jala.university.infrastructure.utils.SessionManager.setCurrentAccountId(accountId);
            System.out.println("2FA: Definindo ID da conta: " + accountId);
        } else {
            System.out.println("2FA: Usuário não possui contas associadas");
        }
        
        String token = generateToken();
        SessionManager.getInstance().setToken(token);
        org.jala.university.infrastructure.utils.SessionManager.setAuthToken(token);
        return token;
    }
    
    /**
     * Logs out the current user.
     */
    @Override
    public void logout() {
        SessionManager.getInstance().clearSession();
    }
    
    /**
     * Generates a random authentication token.
     * 
     * @return the generated token
     */
    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
