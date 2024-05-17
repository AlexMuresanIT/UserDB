package com.exercise.UserDB.repository;

import com.exercise.UserDB.model.UserMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MUserRepo extends MongoRepository<UserMongo,String> {

    Optional<UserMongo> findByEmail(String email);
}
