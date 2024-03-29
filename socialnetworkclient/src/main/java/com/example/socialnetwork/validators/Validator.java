package com.example.socialnetwork.validators;

import java.util.Map;

public interface Validator<ID, E> {
    void validate_add(E entity, Map<ID, E> entities) throws ValidationException;

    void validate_del(ID id, Map<ID, E> entities) throws ValidationException;

}