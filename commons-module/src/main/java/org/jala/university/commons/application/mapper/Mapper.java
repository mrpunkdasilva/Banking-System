package org.jala.university.commons.application.mapper;

public interface Mapper<T, I> {

    I mapTo(T input);
    T mapFrom(I input);
}
