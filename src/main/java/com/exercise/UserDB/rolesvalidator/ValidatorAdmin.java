package com.exercise.UserDB.rolesvalidator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service(ValidatorAdmin.BEAN_ID)
@Slf4j
public class ValidatorAdmin implements Validator {

    public static final String BEAN_ID = "admin";

    @Override
    public void validatePassword(UserDetails user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(encoder.matches("adminboss", user.getPassword())) {
            log.info("Password is good. This is the admin.");
        }else{
            throw new UsernameNotFoundException("Password is incorrect");
        }
    }


}
