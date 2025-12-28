package org.jala.university.domain.validator.interfaces;

import org.jala.university.domain.validator.ValidationException;

public interface Validator<T> {
    void validate(T t) throws ValidationException;
}
