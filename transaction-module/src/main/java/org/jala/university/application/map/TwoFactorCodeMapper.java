package org.jala.university.application.map;

import org.jala.university.application.dto.TwoFactorCodeDTO;
import org.jala.university.domain.entity.TwoFactorCode;
import org.jala.university.domain.entity.User;
import org.jala.university.domain.repository.UserRepository;
import org.jala.university.infrastructure.config.JPAConfig;
import org.jala.university.infrastructure.persistence.UserRepositoryImp;

/**
 * Mapper for converting between TwoFactorCode entities and DTOs.
 */
public class TwoFactorCodeMapper {

    private final UserRepository userRepository;

    /**
     * Constructor with UserRepository parameter.
     * 
     * @param userRepository the user repository
     */
    public TwoFactorCodeMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Default constructor that initializes a UserRepository.
     */
    public TwoFactorCodeMapper() {
        this.userRepository = new UserRepositoryImp(JPAConfig.getEntityManagerFactory().createEntityManager());
    }

    /**
     * Maps a TwoFactorCode entity to a DTO.
     * 
     * @param input the entity to map
     * @return the mapped DTO
     */
    public TwoFactorCodeDTO mapTo(TwoFactorCode input) {
        if (input == null) {
            return null;
        }

        return TwoFactorCodeDTO.builder()
                .id(input.getId())
                .userId(input.getUser().getId())
                .code(input.getCode())
                .expiresAt(input.getExpiresAt())
                .createdAt(input.getCreatedAt())
                .build();
    }

    /**
     * Maps a TwoFactorCodeDTO to an entity.
     * 
     * @param input the DTO to map
     * @return the mapped entity
     */
    public TwoFactorCode mapFrom(TwoFactorCodeDTO input) {
        if (input == null) {
            return null;
        }

        User user = userRepository.findById(input.getUserId());
        
        TwoFactorCode twoFactorCode = new TwoFactorCode();
        twoFactorCode.setId(input.getId());
        twoFactorCode.setUser(user);
        twoFactorCode.setCode(input.getCode());
        twoFactorCode.setExpiresAt(input.getExpiresAt());
        twoFactorCode.setCreatedAt(input.getCreatedAt());
        
        return twoFactorCode;
    }
}