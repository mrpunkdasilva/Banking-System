package org.jala.university.domain.validator.implementation;

import org.jala.university.domain.validator.ValidationException;
import org.jala.university.domain.validator.interfaces.Validator;
import org.jala.university.domain.validator.rules.ValidationRules;

public class EmailValidator implements Validator<String> {
    @Override
    public void validate(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException(ValidationRules.EMAIL_REQUIRED.getMessage());
        }
        if (!email.matches(ValidationRules.EMAIL_REGEX.getRule())) {
            throw new ValidationException(ValidationRules.EMAIL_REGEX.getMessage());
        }
    }
}