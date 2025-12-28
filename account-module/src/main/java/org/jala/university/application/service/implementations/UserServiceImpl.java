package org.jala.university.application.service.implementations;

import jakarta.persistence.EntityManager;
import org.jala.university.application.dto.UserDTO;
import org.jala.university.application.map.UserMapper;
import org.jala.university.application.service.interfaces.UserService;
import org.jala.university.domain.entity.User;
import org.jala.university.domain.repository.UserRepository;
import org.jala.university.infrastructure.config.JPAConfig;
import org.jala.university.infrastructure.persistence.UserRepositoryImp;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * Service implementation for user management operations.
 * Provides business logic for user-related functionality including retrieval,
 * updates, and account information lookup.
 *
 * <p>This service handles the conversion between domain entities and DTOs,
 * and delegates persistence operations to the repository layer.</p>
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * Repository instance for performing CRUD operations on user data.
     * Handles all database interactions related to User entities.
     */
    private final UserRepository userRepository;

    /**
     * Mapper component responsible for converting between User entities
     * and their corresponding Data Transfer Objects (DTOs).
     * Provides bi-directional mapping capabilities.
     */
    private final UserMapper userMapper;

    /**
     * JPA EntityManager instance for managing persistence operations.
     * Handles entity lifecycle management and provides the interface
     * for all JPA-related database operations.
     */
    private final EntityManager entityManager;

    /**
     * Constructs a new UserServiceImpl instance.
     * Initializes the EntityManager from JPA configuration, creates
     * the UserRepository implementation, and instantiates the UserMapper.
     */
    public UserServiceImpl() {
        this.entityManager = JPAConfig.getEntityManagerFactory().createEntityManager();
        this.userRepository = new UserRepositoryImp(entityManager);
        this.userMapper = new UserMapper();
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user to retrieve
     * @return UserDTO containing the user's data
     */
    @Override
    public UserDTO findById(Long id) {
        return this.userMapper.mapTo(this.userRepository.findById(id));
    }

    /**
     * Updates an existing user's information.
     *
     * @param id the unique identifier of the user to update
     * @param userDTO DTO containing the updated user information
     */
    @Override
    public void update(Long id, UserDTO userDTO) {
        UserDTO userFound = this.findById(id);
        userDTO.setId(userFound.getId());
        this.userRepository.save(this.userMapper.mapFrom(userFound));
    }

    /**
     * Retrieves a user's name by their associated account ID.
     *
     * @param accountId the account identifier to lookup
     * @return the user's name if found, null otherwise
     */
    @Override
    public String getUserNameByAccountId(Long accountId) {
        Optional<User> userOptional = this.userRepository.findByAccountId(accountId);
        return userOptional.map(User::getName).orElse(null);
    }
}
