package org.jala.university.domain.services;

import org.jala.university.application.dto.BeneficiaryDTO;

public class ValidationService {
    public boolean validateBeneficiary(BeneficiaryDTO beneficiary) {
        // Validar nome
        if (isNullOrEmpty(beneficiary.getName())) {
            return false;
        }

        // Validar documento (CPF ou CNPJ)
        if (!isValidDocument(beneficiary.getDocument())) {
            return false;
        }

        // Validar número da conta
        if (isNullOrEmpty(beneficiary.getAccountNumber())) {
            return false;
        }

        // Validar banco
        if (isNullOrEmpty(beneficiary.getBank())) {
            return false;
        }

        return true;
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isValidDocument(String document) {
        if (isNullOrEmpty(document)) {
            return false;
        }

        // Remove caracteres não numéricos
        String cleanDocument = document.replaceAll("[^0-9]", "");

        // Validar CPF (11 dígitos)
        if (cleanDocument.length() == 11) {
            return isValidCPF(cleanDocument);
        }

        // Validar CNPJ (14 dígitos)
        if (cleanDocument.length() == 14) {
            return isValidCNPJ(cleanDocument);
        }

        return false;
    }

    private boolean isValidCPF(String cpf) {
        // Validação de CPF
        if (cpf.matches("(\\d)\\1{10}")) return false;

        // Cálculo dos dígitos verificadores
        int sum1 = 0, sum2 = 0;
        for (int i = 0; i < 9; i++) {
            sum1 += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int digit1 = 11 - (sum1 % 11);
        if (digit1 > 9) digit1 = 0;

        for (int i = 0; i < 10; i++) {
            sum2 += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int digit2 = 11 - (sum2 % 11);
        if (digit2 > 9) digit2 = 0;

        return Character.getNumericValue(cpf.charAt(9)) == digit1 &&
                Character.getNumericValue(cpf.charAt(10)) == digit2;
    }

    private boolean isValidCNPJ(String cnpj) {
        // Validação de CNPJ
        if (cnpj.matches("(\\d)\\1{13}")) return false;

        int[] weights1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] weights2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int sum1 = 0, sum2 = 0;
        for (int i = 0; i < 12; i++) {
            sum1 += Character.getNumericValue(cnpj.charAt(i)) * weights1[i];
        }

        int digit1 = 11 - (sum1 % 11);
        if (digit1 > 9) digit1 = 0;

        for (int i = 0; i < 13; i++) {
            sum2 += Character.getNumericValue(cnpj.charAt(i)) * weights2[i];
        }

        int digit2 = 11 - (sum2 % 11);
        if (digit2 > 9) digit2 = 0;

        return Character.getNumericValue(cnpj.charAt(12)) == digit1 &&
                Character.getNumericValue(cnpj.charAt(13)) == digit2;
    }
}