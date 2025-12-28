package org.jala.university.domain.validator.implementation;

import org.jala.university.domain.validator.ValidationException;
import org.jala.university.domain.validator.interfaces.Validator;
import org.jala.university.domain.validator.rules.ValidationRules;

public class PhoneValidator implements Validator<String> {
    @Override
    public void validate(String phone) throws ValidationException {
        if (phone == null || phone.trim().isEmpty()) {
            throw new ValidationException(ValidationRules.PHONE_REQUIRED.getMessage());
        }

        String cleanPhone = phone.replaceAll("[^0-9]", "");

        if (!cleanPhone.matches(ValidationRules.PHONE_REGEX.getRule())) {
            throw new ValidationException(ValidationRules.PHONE_REGEX.getMessage());
        }
    }
}
