package com.exercise.UserDB.rolesvalidator;

import org.springframework.security.core.userdetails.UserDetails;

public interface Validator {

    void validatePassword(UserDetails user);
}
