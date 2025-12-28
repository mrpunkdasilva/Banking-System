package org.jala.university.commons.domain;

import java.util.List;

public interface Repository<T extends BaseEntity<ID>, ID> {
    T findById(ID id);
    List<T> findAll();
    T save(T entity);
    void delete(T entity);
    void deleteById(ID entityId);
}
