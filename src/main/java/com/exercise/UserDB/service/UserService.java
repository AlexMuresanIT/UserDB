package com.exercise.UserDB.service;

import com.exercise.UserDB.exception.NoUserFoundException;
import com.exercise.UserDB.model.User;
import com.exercise.UserDB.repository.MUserRepo;
import com.exercise.UserDB.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            return user.get();
        }
        throw new NoUserFoundException("User not found");
    }

    public User createUser(User user) {
        userRepository.save(user);
        return user;
    }

    public User updateUser(Long id, User user) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User updatedUser = userOptional.get();
            updatedUser.setName(user.getName());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword(user.getPassword());
            return userRepository.save(updatedUser);
        }
        throw new NoUserFoundException("User not found");
    }

    public User deleteUser(Long id) {
        userRepository.deleteById(id);
        return null;
    }
}
