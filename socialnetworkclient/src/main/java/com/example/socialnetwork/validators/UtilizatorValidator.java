package com.example.socialnetwork.validators;

import com.example.socialnetwork.domain.User;

import java.util.Map;

public class UtilizatorValidator implements Validator<Long, User> {
    @Override
    public void validate_add(User entity, Map<Long, User> entities) throws ValidationException {
        if(entity.getFirstName().matches(".*[0-9].*") || !(Character.isUpperCase(entity.getFirstName().charAt(0)))) {
            throw new ValidationException("FirstName must not contain numbers and must start with uppercase character!");
        }
        if(entity.getLastName().matches(".*[0-9].*") || !(Character.isUpperCase(entity.getLastName().charAt(0)))) {
            throw new ValidationException("LastName must not contain numbers and must start with uppercase character!");
        }
        if (entities.containsKey(entity.getId())) {
            throw new IllegalArgumentException("This ID exists already");
        }
    }

    public void validate_add_db(User entity) throws ValidationException {
        if(entity.getFirstName().matches(".*[0-9].*") || !(Character.isUpperCase(entity.getFirstName().charAt(0)))) {
            throw new ValidationException("FirstName must not contain numbers and must start with uppercase character!");
        }
        if(entity.getLastName().matches(".*[0-9].*") || !(Character.isUpperCase(entity.getLastName().charAt(0)))) {
            throw new ValidationException("LastName must not contain numbers and must start with uppercase character!");
        }
    }
    @Override
    public void validate_del(Long id, Map<Long, User> entities) throws ValidationException {
        if (!entities.containsKey(id)) {
            throw new IllegalArgumentException("ID not found");
        }
    }
}