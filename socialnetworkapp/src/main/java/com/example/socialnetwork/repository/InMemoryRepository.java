package com.example.socialnetwork.repository;

import com.example.socialnetwork.domain.Entity;
import com.example.socialnetwork.validators.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {
    private final Validator<ID, E> validator;
    private Map<ID,E> entities;

    public InMemoryRepository(Validator<ID, E> validator) {
        this.validator = validator;
        entities= new HashMap<>();
    }

    @Override
    public Optional<E> findOne(ID id){
        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate_add(entity, entities);
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(),entity));
    }

    @Override
    public Optional<E> delete(ID id) {
        validator.validate_del(id, entities);
        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        return  Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<E> update(E entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        if (!entities.containsKey(entity.getId())) {
            throw new IllegalArgumentException("ID not found");
        }
        return Optional.ofNullable(entities.replace(entity.getId(), entity));
    }
}
