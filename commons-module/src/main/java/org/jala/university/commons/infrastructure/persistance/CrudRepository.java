package org.jala.university.commons.infrastructure.persistance;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jala.university.commons.domain.Repository;
import org.jala.university.commons.domain.BaseEntity;
import org.springframework.util.Assert;

import java.util.List;

public abstract class CrudRepository<T
        extends BaseEntity<ID>, ID>
        implements Repository<T, ID> {

    private final Class<T> clazz;
    private final EntityManager entityManager;

    protected CrudRepository(Class<T> clazzToSet, EntityManager entityManager) {
        this.entityManager = entityManager;
        this.clazz = clazzToSet;
    }

    @Override
    @Transactional
    public final T findById(ID id) {
        return entityManager.find(clazz, id);
    }

    @Override
    @Transactional
    public final List<T> findAll() {
        return entityManager
                .createQuery("from " + clazz.getName()).getResultList();
    }

    @Override
    @Transactional
    public final T save(T entity) {
        Assert.notNull(entity, "Entity must not be null");
        entityManager.getTransaction().begin();
        if (isNew(entity)) {
            entityManager.persist(entityManager.merge(entity));
            entityManager.getTransaction().commit();
            return entity;
        }
        T mergedEntity = entityManager.merge(entity);
        entityManager.persist(mergedEntity);
        entityManager.getTransaction().commit();
        return mergedEntity;
    }

    @Override
    @Transactional
    public final void delete(T entity) {
        Assert.notNull(entity, "Entity must not be null");
        if (!isNew(entity)) {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.merge(entity));
            entityManager.getTransaction().commit();
        }
    }

    @Override
    @Transactional
    public final void deleteById(ID entityId) {
        Assert.notNull(entityId, "The given id must not be null");
        T entity = findById(entityId);
        if (entity != null) {
            entityManager.getTransaction().begin();
            entityManager.remove(entity);
            entityManager.getTransaction().commit();
        }
    }

    private boolean isNew(T entity) {
        if (entity.getId() != null) {
            return entityManager.find(clazz, entity.getId()) == null;
        }
        return true;
    }
}
