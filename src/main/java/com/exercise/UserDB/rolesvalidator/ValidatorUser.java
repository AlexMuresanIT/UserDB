package com.exercise.UserDB.rolesvalidator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service(ValidatorUser.BEAN_ID)
@Slf4j
public class ValidatorUser implements Validator {

    public static final String BEAN_ID = "user";

    @Override
    public void validatePassword(UserDetails user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(encoder.matches("alexboss", user.getPassword()) || encoder.matches("mihaiboss", user.getPassword())) {
            log.info("Password is valid.");
            //return user.getAuthorities();
        }else{
            throw new UsernameNotFoundException("Password is incorrect.");
        }
    }
}
