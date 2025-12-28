package org.jala.university.application.service.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.jala.university.application.dto.AccountDTO;
import org.jala.university.application.map.AccountMapper;
import org.jala.university.application.service.interfaces.AccountService;
import org.jala.university.domain.entity.Account;
import org.jala.university.domain.entity.enums.AccountType;
import org.jala.university.domain.repository.AccountRepository;
import org.jala.university.domain.repository.UserRepository;
import org.jala.university.infrastructure.config.JPAConfig;
import org.jala.university.infrastructure.persistence.AccountRepositoryImp;
import org.jala.university.infrastructure.persistence.UserRepositoryImp;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Service responsible for creating and managing accounts.
 *
 * @since 1.0
 */
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private static final Random RANDOM = new Random();
    private final EntityManager entityManager;
    private final AccountMapper mapper;

    /**
     * Constructor.
     */
    public AccountServiceImpl() {
        this.mapper = new AccountMapper();
        this.entityManager = JPAConfig.getEntityManagerFactory().createEntityManager();
        this.userRepository = new UserRepositoryImp(entityManager);
        this.accountRepository = new AccountRepositoryImp(entityManager);
    }

    /**
     * Creates a new account for the given user.
     *
     * @param cpf user CPF
     * @param accountType account type
     * @return created account DTO
     */
    @Override
    public AccountDTO createAccount(String cpf, AccountType accountType) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            
            var user = userRepository.findByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

            var account = Account.builder()
                    .user(user)
                    .agency(generateAgency())
                    .accountNumber(generateAccountNumber())
                    .accountType(accountType)
                    .build();

            entityManager.persist(account);
            transaction.commit();
            
            return AccountDTO.builder()
                    .id(account.getId())
                    .userId(user.getId())
                    .agency(account.getAgency())
                    .accountNumber(account.getAccountNumber())
                    .accountType(account.getAccountType())
                    .build();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error creating account", e);
        }
    }

    /**
     * Updates the balance of a user's account.
     *
     * @param userId the ID of the user
     * @param balance the new balance to set
     * @throws RuntimeException if the user account doesn't have enough balance
     */
    @Override
    public void updateUserAccountBalance(Long userId, BigDecimal balance) {
        Account accountByUserId = this.accountRepository.findAccountByUserId(userId);
        accountByUserId.setBalance(balance);
        accountRepository.save(accountByUserId);
    }

    /**
     * Retrieves account information for a specific user.
     *
     * @param userId the ID of the user
     * @return the account DTO associated with the user
     */
    @Override
    public AccountDTO getAccountByUserId(Long userId) {
        Account accountFound = this.accountRepository.findAccountByUserId(userId);
        return this.mapper.mapTo(accountFound);
    }

    @Override
    public AccountDTO getAccountById(Long accountId) {
        Account accountFound = this.accountRepository.findByAccountId(accountId);
        return this.mapper.mapTo(accountFound);
    }

    @Override
    public AccountDTO getAccountByAccountNumber(String accountNumber) {
        Account accountByAccountNumber = this.accountRepository.findByAccountNumber(accountNumber);
        return this.mapper.mapTo(accountByAccountNumber);
    }

    /**
     * Generates a random agency number.
     *
     * @return agency number
     */
    private String generateAgency() {
        return String.format("%04d", RANDOM.nextInt(10000));
    }

    /**
     * Generates a random account number.
     *
     * @return account number
     */
    private String generateAccountNumber() {
        return String.format("%08d", RANDOM.nextInt(100000000));
    }

    /**
     * Retrieves the current balance for an account.
     *
     * @param accountId the ID of the account
     * @return the current balance of the account
     * @throws IllegalArgumentException if the account is not found
     */
    @Override
    public BigDecimal getAccountBalance(Long accountId) {
        Account account = accountRepository.findById(accountId);

        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        return account.getBalance();
    }

       private boolean checkSenderBalance(BigDecimal amount, BigDecimal balance) {
        return balance.compareTo(amount) >= 0;
    }
}
