package org.jala.university.application.sanitizer;

import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * Utility class for sanitizing user input data in signup DTOs.
 * Provides methods to clean and format various types of user input
 * such as names, emails, and identification numbers.
 *
 * @version 1.0
 */
public final class SignupDTOSanitizer {
    
    /** Pattern to match multiple consecutive whitespace characters */
    private static final Pattern MULTIPLE_SPACES = Pattern.compile("\\s+");
    
    /** Pattern to match any non-digit character */
    private static final Pattern NON_DIGITS = Pattern.compile("[^0-9]");
    
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private SignupDTOSanitizer() {
        throw new AssertionError("Utility class - no instantiation");
    }

    /**
     * Sanitizes a full name by:
     * - Trimming leading/trailing spaces
     * - Replacing multiple spaces with single space
     * - Capitalizing first letter of each word
     *
     * @param fullName the full name to sanitize
     * @return sanitized full name or empty string if input is null
     */
    public static String sanitizeFullName(String fullName) {
        return sanitize(fullName, input -> MULTIPLE_SPACES.matcher(input.trim())
            .replaceAll(" ")
            .transform(SignupDTOSanitizer::capitalizeWords));
    }

    /**
     * Sanitizes an email address by converting it to lowercase.
     *
     * @param email the email address to sanitize
     * @return sanitized email address or empty string if input is null
     */
    public static String sanitizeEmail(String email) {
        return sanitize(email, String::toLowerCase);
    }

    /**
     * Sanitizes a password by removing leading/trailing whitespace.
     *
     * @param password the password to sanitize
     * @return sanitized password or empty string if input is null
     */
    public static String sanitizePassword(String password) {
        return sanitize(password, String::trim);
    }


    /**
     * Sanitizes a CPF (Brazilian tax ID) by removing all non-digit characters.
     *
     * @param cpf the CPF to sanitize
     * @return sanitized CPF containing only digits or empty string if input is null
     */
    public static String sanitizeCPF(String cpf) {
        return sanitize(cpf, input -> NON_DIGITS.matcher(input).replaceAll(""));
    }

    /**
     * Sanitizes a phone number by removing all non-digit characters.
     *
     * @param phoneNumber the phone number to sanitize
     * @return sanitized phone number containing only digits or empty string if input is null
     */
    public static String sanitizePhoneNumber(String phoneNumber) {
        return sanitize(phoneNumber, input -> NON_DIGITS.matcher(input).replaceAll(""));
    }

    /**
     * Generic sanitization method that applies a sanitizer function to input.
     *
     * @param input the input string to sanitize
     * @param sanitizer the function to apply for sanitization
     * @return sanitized string or empty string if input is null
     */
    private static String sanitize(String input, UnaryOperator<String> sanitizer) {
        return Objects.toString(input, "").transform(sanitizer);
    }

    /**
     * Capitalizes the first letter of each word in a text string.
     * Other letters are converted to lowercase.
     *
     * @param text the text to capitalize
     * @return text with first letter of each word capitalized
     */
    private static String capitalizeWords(String text) {
        if (text.isEmpty()) {
            return text;
        }

        StringBuilder result = new StringBuilder(text.length());
        boolean capitalizeNext = true;

        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                capitalizeNext = true;
                result.append(ch);
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(ch));
                capitalizeNext = false;
            } else {
                result.append(Character.toLowerCase(ch));
            }
        }

        return result.toString();
    }
}