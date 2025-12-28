package org.jala.university.application.map;

import org.jala.university.application.dto.UserDTO;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.User;

/**
 * Mapper class for converting between User entity and UserDTO.
 */
public class UserMapper implements Mapper<User, UserDTO> {

    /**
     * Converts a User entity to a UserDTO.
     *
     * @param input the User entity to convert
     * @return the converted UserDTO
     */
    @Override
    public UserDTO mapTo(User input) {
        if (input == null) {
            return null;
        }

        return UserDTO.builder()
                .id(input.getId())
                .name(input.getName())
                .email(input.getEmail())
                .password(input.getPassword())
                .cpf(input.getCpf())
                .phoneNumber(input.getPhoneNumber())
                .roles(input.getRoles())
                .createdAt(input.getCreatedAt())
                .updatedAt(input.getUpdatedAt())
                .accounts(input.getAccounts())
                .twoFactorEnabled(input.isTwoFactorEnabled())
                .build();
    }

    /**
     * Converts a UserDTO to a User entity.
     *
     * @param input the UserDTO to convert
     * @return the converted User entity
     */
    @Override
    public User mapFrom(UserDTO input) {
        if (input == null) {
            return null;
        }

        return User.builder()
                .id(input.getId())
                .name(input.getName())
                .email(input.getEmail())
                .password(input.getPassword())
                .cpf(input.getCpf())
                .phoneNumber(input.getPhoneNumber())
                .roles(input.getRoles())
                .createdAt(input.getCreatedAt())
                .updatedAt(input.getUpdatedAt())
                .accounts(input.getAccounts())
                .twoFactorEnabled(input.isTwoFactorEnabled())
                .build();
    }
}
