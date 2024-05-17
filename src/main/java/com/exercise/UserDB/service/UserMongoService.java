package com.exercise.UserDB.service;

import com.exercise.UserDB.exception.NoUserFoundException;
import com.exercise.UserDB.model.UserMongo;
import com.exercise.UserDB.repository.MUserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserMongoService {

    private final MUserRepo mUserRepo;

    public UserMongoService(MUserRepo mUserRepo) {
        this.mUserRepo = mUserRepo;
    }

    public boolean save(UserMongo user) {
        mUserRepo.save(user);
        return true;
    }

    public List<UserMongo> findAll() {
        log.info("size: {}",mUserRepo.findAll().size());
        return mUserRepo.findAll();
    }

    public UserMongo findById(String id) {
        Optional<UserMongo> user = mUserRepo.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        throw new NoUserFoundException("User not found");
    }

    public boolean deleteById(String id) {
        Optional<UserMongo> user = mUserRepo.findById(id);
        if (user.isPresent()) {
            mUserRepo.deleteById(id);
            return true;
        }
        throw new NoUserFoundException("User not found");
    }

    public boolean update(String id, UserMongo user) {
        Optional<UserMongo> updatedUser = mUserRepo.findById(id);
        if(updatedUser.isPresent()){
            UserMongo updatedUserMongo = updatedUser.get();
            updatedUserMongo.setName(user.getName());
            updatedUserMongo.setPassword(user.getPassword());
            updatedUserMongo.setEmail(user.getEmail());
            updatedUserMongo.setTown(user.getTown());
            mUserRepo.save(updatedUserMongo);
            return true;
        }
        throw new NoUserFoundException("User not found");
    }

    public UserMongo findByEmail(String email) {
        var user = mUserRepo.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        }
        throw new NoUserFoundException("User not found");
    }
}
