package com.exercise.UserDB.repository;

import com.exercise.UserDB.model.User;
import com.exercise.UserDB.model.UserMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MUserRepo extends MongoRepository<UserMongo,String> {
}
