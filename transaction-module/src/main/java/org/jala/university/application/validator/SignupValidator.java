package org.jala.university.application.validator;

import lombok.RequiredArgsConstructor;
import org.jala.university.application.dto.UserDTO;
import org.jala.university.domain.repository.UserRepository;
import org.jala.university.domain.validator.ValidationException;
import org.jala.university.domain.validator.implementation.CPFValidator;
import org.jala.university.domain.validator.implementation.EmailValidator;
import org.jala.university.domain.validator.implementation.PasswordValidator;
import org.jala.university.domain.validator.implementation.PhoneValidator;
import org.jala.university.domain.validator.rules.ValidationRules;

@RequiredArgsConstructor
public class SignupValidator {
    private final UserRepository userRepository;
    private final EmailValidator emailValidator;
    private final PhoneValidator phoneValidator;
    private final CPFValidator cpfValidator;
    private final PasswordValidator passwordValidator;

    public void validate(UserDTO userDTO) throws ValidationException {
        validateBasicFields(userDTO);
        validateUniqueness(userDTO);
    }

      private void validateBasicFields(UserDTO userDTO) {
        if (userDTO.getName() == null || userDTO.getName().trim().isEmpty()) {
            throw new ValidationException(ValidationRules.NAME_REQUIRED.getMessage());
        }

        if (userDTO.getName().length() < Integer.parseInt(ValidationRules.NAME_MIN_LENGTH.getRule())) {
            throw new ValidationException(ValidationRules.NAME_MIN_LENGTH.getMessage());
        }

        if (userDTO.getName().length() > Integer.parseInt(ValidationRules.NAME_MAX_LENGTH.getRule())) {
            throw new ValidationException(ValidationRules.NAME_MAX_LENGTH.getMessage());
        }

        emailValidator.validate(userDTO.getEmail());
        cpfValidator.validate(userDTO.getCpf());
        passwordValidator.validate(userDTO.getPassword());
        phoneValidator.validate(userDTO.getPhoneNumber());
    }

    private void validateUniqueness(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ValidationException(ValidationRules.UNIQUE_EMAIL.getMessage());
        }

        if (userRepository.existsByCpf(userDTO.getCpf())) {
            throw new ValidationException(ValidationRules.CPF_UNIQUE.getMessage());
        }
    }
}
