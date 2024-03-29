package com.example.socialnetwork.validators;

import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.Tuple;

import java.util.Map;

public class FriendshipValidator implements Validator<Tuple<Long, Long>, Friendship> {
    @Override
    public void validate_add(Friendship entity, Map<Tuple<Long, Long>, Friendship> entities) throws ValidationException {
        if (entities.containsKey(entity.getId())){
            throw new ValidationException("The ID already exist");
        }
    }
    @Override
    public void validate_del(Tuple<Long, Long> id, Map<Tuple<Long, Long>, Friendship> entities) throws ValidationException {
        if (!entities.containsKey(id)){
            throw new ValidationException("ID not found");
        }
    }
}
