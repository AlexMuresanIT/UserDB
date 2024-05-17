package com.exercise.UserDB.controller;

import com.exercise.UserDB.aop.TrackCounterM;
import com.exercise.UserDB.aop.TrackInfo;
import com.exercise.UserDB.config.ConvertInterface;
import com.exercise.UserDB.exception.InvalidData;
import com.exercise.UserDB.exception.NoUserFoundException;
import com.exercise.UserDB.model.UserDTOMongo;
import com.exercise.UserDB.model.UserMongo;
import com.exercise.UserDB.rolesvalidator.ValidatorFactory;
import com.exercise.UserDB.service.UserMongoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserMongoAPIController implements ConvertInterface<UserDTOMongo, UserMongo> {

    private static final Logger logger = LoggerFactory.getLogger(UserMongoAPIController.class);

    private final UserMongoService userMongoService;
    private final TrackCounterM trackCounterM;
    private final ValidatorFactory validatorFactory;

    public UserMongoAPIController(UserMongoService userMongoService,
                                  TrackCounterM trackCounter,
                                  ValidatorFactory validatorFactory) {
        this.userMongoService = userMongoService;
        this.trackCounterM = trackCounter;
        this.validatorFactory = validatorFactory;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/userm/{id}")
    public ResponseEntity<UserDTOMongo> getUserById(@PathVariable String id) {
        try{
            var user = userMongoService.findById(id);
            logger.info("User with id {} is retrieved", id);
            return new ResponseEntity<>(convertToDTO(user),HttpStatus.OK);
        }catch (NoUserFoundException e){
            logger.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/userm/email/{email}")
    public ResponseEntity<UserDTOMongo> getUserByEmail(@PathVariable String email) {
        try{
            var user = userMongoService.findByEmail(email);
            logger.info("User with email {} is retrieved", email);
            return new ResponseEntity<>(convertToDTO(user),HttpStatus.OK);
        }catch (NoUserFoundException e){
            logger.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/usersm")
    public ResponseEntity<List<UserDTOMongo>> getAllUsers() {
        logger.info("Users from MongoDB are retrieved");
        var users = userMongoService.findAll();
        return new ResponseEntity<>(convertToDTO(users), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addUserm")
    public ResponseEntity<UserMongo> saveUser(@RequestBody UserMongo user) {
        try{
            userMongoService.save(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }catch (InvalidData e){
            logger.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @DeleteMapping("/userm/delete/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id) {
        logger.info("User with id {} is deleted", id);
        try{
            userMongoService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (NoUserFoundException e){
            logger.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/userm/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody UserMongo user) {
        logger.info("User with id {} updated", user.getId());
        try{
            userMongoService.update(id,user);
            return new ResponseEntity<>("User updated.",HttpStatus.OK);
        }catch (NoUserFoundException e){
            logger.info(e.getMessage());
            return new ResponseEntity<>("Could not update the user.",HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/statsm")
    public Map<String, TrackInfo> getUserStatsM() {
        return trackCounterM.getCounter();
    }

    @PutMapping("/statsm/reset")
    public Map<String,TrackInfo> resetUserStatsM() {
        return trackCounterM.getCounter();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/usernamem")
    public String currentUserNameSimple(@AuthenticationPrincipal UserDetails userDetails) {
        validatorFactory.returnRole(userDetails.getUsername()).validatePassword(userDetails);
        return "Current role: " + userDetails.getUsername();
    }

    @Override
    public UserDTOMongo convertToDTO(UserMongo user) {
        return new UserDTOMongo(user);
    }

    @Override
    public List<UserDTOMongo> convertToDTO(List<UserMongo> users) {
        List<UserDTOMongo> dtos = new ArrayList<>();
        for (UserMongo user : users) {
            dtos.add(convertToDTO(user));
        }
        return dtos;
    }
}
