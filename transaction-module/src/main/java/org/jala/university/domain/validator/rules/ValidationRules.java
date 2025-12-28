package org.jala.university.domain.validator.rules;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ValidationRules {
    // Email rules
    EMAIL_REGEX("^[A-Za-z0-9+_.-]+@(.+)$", "Formato de email inválido"),
    EMAIL_REQUIRED(null, "Email não pode ser vazio"),

    // Password rules
    PASSWORD_MIN_LENGTH("8", "Senha deve ter no mínimo 8 caracteres"),
    PASSWORD_REGEX("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", 
        "Senha deve conter pelo menos: uma letra maiúscula, uma letra minúscula, um número e um caractere especial"),
    PASSWORD_REQUIRED(null, "Senha não pode ser vazia"),

    // CPF rules
    CPF_LENGTH("11", "CPF deve conter 11 dígitos"),
    CPF_REQUIRED(null, "CPF não pode ser vazio"),
    CPF_REGEX("^\\d{11}$", "CPF deve conter apenas números"),
    CPF_INVALID(null, "CPF inválido"),
    CPF_UNIQUE(null, "CPF já cadastrado"),
    CPF_VALIDATION_WEIGHTS_DV1("10,9,8,7,6,5,4,3,2", "Pesos para cálculo do primeiro dígito verificador do CPF"),
    CPF_VALIDATION_WEIGHTS_DV2("11,10,9,8,7,6,5,4,3,2", "Pesos para cálculo do segundo dígito verificador do CPF"),

    // Phone rules
    PHONE_REGEX("^\\d{10,15}$", "Formato de telefone inválido"),
    PHONE_REQUIRED(null, "Telefone não pode ser vazio"),

    // Name rules
    NAME_REQUIRED(null, "Nome completo não pode ser vazio"),
    NAME_MIN_LENGTH("3", "Nome deve ter no mínimo 3 caracteres"),
    NAME_MAX_LENGTH("150", "Nome deve ter no máximo 150 caracteres"),

    // General rules
    UNIQUE_EMAIL(null, "Email já cadastrado");

    private final String rule;
    private final String message;
}