package com.exercise.UserDB.service;

import com.exercise.UserDB.exception.InvalidData;
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

        int zipcode = user.getAddress().getZipcode();
        int nr = user.getAddress().getNumber();

        if(zipcode > 99999 && zipcode < 1000000 && nr > 0 && nr < 1000) {
            mUserRepo.save(user);
            return true;
        }
        throw new InvalidData("Street number or zipcode are invalid.");
    }

    public List<UserMongo> findAll() {
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
            int zipcode = user.getAddress().getZipcode();
            int nr = user.getAddress().getNumber();

            if(zipcode > 9999 && zipcode < 1000000 && nr > 0 && nr < 1000) {
                UserMongo updatedUserMongo = updatedUser.get();
                updatedUserMongo.setName(user.getName());
                updatedUserMongo.setPassword(user.getPassword());
                updatedUserMongo.setEmail(user.getEmail());
                updatedUserMongo.setAddress(user.getAddress());
                mUserRepo.save(updatedUserMongo);
                return true;
            }
            throw new InvalidData("Street number or zipcode are invalid.");
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
