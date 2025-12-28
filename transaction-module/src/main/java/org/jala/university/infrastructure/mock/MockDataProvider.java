package org.jala.university.infrastructure.mock;

import java.util.List;

/**
 * Interface for mock data providers.
 * Implementations should provide mock data for different entities in the application.
 *
 * @param <T> the type of data to be mocked
 */
public interface MockDataProvider<T> {
    
    /**
     * Provides a list of mock data.
     *
     * @return a list of mock data objects
     */
    List<T> provideMockData();
}