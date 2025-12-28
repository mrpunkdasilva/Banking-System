package org.jala.university.domain.validator.implementation;

import org.jala.university.domain.validator.ValidationException;
import org.jala.university.domain.validator.interfaces.Validator;
import org.jala.university.domain.validator.rules.ValidationRules;

public class PasswordValidator implements Validator<String> {
    @Override
    public void validate(String password) throws ValidationException {
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException(ValidationRules.PASSWORD_REQUIRED.getMessage());
        }
        if (password.length() < Integer.parseInt(ValidationRules.PASSWORD_MIN_LENGTH.getRule())) {
            throw new ValidationException(ValidationRules.PASSWORD_MIN_LENGTH.getMessage());
        }
        if (!password.matches(ValidationRules.PASSWORD_REGEX.getRule())) {
            throw new ValidationException(ValidationRules.PASSWORD_REGEX.getMessage());
        }
    }
}