package org.jala.university.domain.validator.implementation;

import org.jala.university.domain.validator.ValidationException;
import org.jala.university.domain.validator.interfaces.Validator;
import org.jala.university.domain.validator.rules.ValidationRules;

import java.util.Arrays;

public class CPFValidator implements Validator<String> {
    @Override
    public void validate(String cpf) throws ValidationException {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new ValidationException(ValidationRules.CPF_REQUIRED.getMessage());
        }
        
        // Remover formatação para validação
        String cpfClean = cpf.replaceAll("[^0-9]", "");
        
        if (cpfClean.length() != 11) {
            throw new ValidationException(ValidationRules.CPF_INVALID.getMessage());
        }
        
        // Verificar se todos os dígitos são iguais
        boolean allDigitsEqual = true;
        for (int i = 1; i < cpfClean.length(); i++) {
            if (cpfClean.charAt(i) != cpfClean.charAt(0)) {
                allDigitsEqual = false;
                break;
            }
        }
        
        if (allDigitsEqual) {
            throw new ValidationException(ValidationRules.CPF_INVALID.getMessage());
        }
        
        // Validar dígitos verificadores
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (cpfClean.charAt(i) - '0') * (10 - i);
        }
        
        int remainder = sum % 11;
        int firstVerifier = remainder < 2 ? 0 : 11 - remainder;
        
        if (firstVerifier != (cpfClean.charAt(9) - '0')) {
            throw new ValidationException(ValidationRules.CPF_INVALID.getMessage());
        }
        
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (cpfClean.charAt(i) - '0') * (11 - i);
        }
        
        remainder = sum % 11;
        int secondVerifier = remainder < 2 ? 0 : 11 - remainder;
        
        if (secondVerifier != (cpfClean.charAt(10) - '0')) {
            throw new ValidationException(ValidationRules.CPF_INVALID.getMessage());
        }
    }
}
