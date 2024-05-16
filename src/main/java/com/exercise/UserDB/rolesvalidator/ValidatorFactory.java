package com.exercise.UserDB.rolesvalidator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ValidatorFactory {

    private final Map<String,Validator> validators;

    public Validator returnRole(String role) {
        return validators.get(role);
    }
}
