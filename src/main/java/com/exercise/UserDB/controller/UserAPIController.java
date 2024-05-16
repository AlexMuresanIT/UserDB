package com.exercise.UserDB.controller;

import com.exercise.UserDB.aop.TrackCounter;
import com.exercise.UserDB.aop.TrackInfo;
import com.exercise.UserDB.config.ConvertInterface;
import com.exercise.UserDB.model.User;
import com.exercise.UserDB.model.UserDTO;
import com.exercise.UserDB.model.UserDTOMongo;
import com.exercise.UserDB.model.UserMongo;
import com.exercise.UserDB.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@ControllerAdvice
public class UserAPIController implements ConvertInterface<UserDTO, User> {

    private static final Logger logger = LoggerFactory.getLogger(UserAPIController.class);

    private final UserService userService;
    private final TrackCounter trackCounter;

    public UserAPIController(UserService userService, TrackCounter trackCounter) {
        this.userService = userService;
        this.trackCounter = trackCounter;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        logger.info("Users are retrieved...");
        var users = userService.getAllUsers();
        return new ResponseEntity<>(convertToDTO(users), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        logger.info("User is retrieved...");
        var user = userService.getUserById(id);
        logger.info(user.toString());
        logger.info("User with ID "+id+" is retrieved...");
        return new ResponseEntity<>(convertToDTO(user), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/user/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        logger.info("User with ID " + id + " is updated...");
        User updatedUser = userService.updateUser(id, user);
        if(updatedUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @DeleteMapping("/user/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        logger.info("User with ID " + id + " is deleted...");
        userService.deleteUser(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        logger.info("User is added...");
        userService.createUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/stats")
    public Map<Long, TrackInfo> getUserStats() {
        return trackCounter.getCounter();
    }

    @PutMapping("/stats/reset")
    public Map<Long,TrackInfo> resetUserStats() {
        return trackCounter.getCounter();
    }

    @GetMapping(value = "/username")
    public String currentUserNameSimple(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return "Current role: " + principal.getName();
    }

    @Override
    public UserDTO convertToDTO(User user) {
        return new UserDTO(user);
    }

    @Override
    public List<UserDTO> convertToDTO(List<User> users) {
        List<UserDTO> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(convertToDTO(user));
        }
        return dtos;
    }
}
