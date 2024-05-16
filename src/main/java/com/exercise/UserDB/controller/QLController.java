package com.exercise.UserDB.controller;

import com.exercise.UserDB.aop.TrackCounterM;
import com.exercise.UserDB.aop.TrackInfo;
import com.exercise.UserDB.config.ConvertInterface;
import com.exercise.UserDB.config.MapStatsSch;
import com.exercise.UserDB.model.UserDTOMongo;
import com.exercise.UserDB.model.UserMongo;
import com.exercise.UserDB.service.UserMongoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.*;

@Controller
public class QLController implements ConvertInterface<UserDTOMongo, UserMongo> {

    private final UserMongoService service;
    private final TrackCounterM trackCounterM;

    public QLController(UserMongoService service,
                        TrackCounterM trackCounterM) {
        this.service = service;
        this.trackCounterM = trackCounterM;
    }

    private static final Logger logger = LoggerFactory.getLogger(QLController.class);

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @QueryMapping
    public List<UserDTOMongo> findAll(){
        logger.info("Retrieving all users");
        var users = service.findAll();
        return convertToDTO(users);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @QueryMapping
    public UserDTOMongo findUserById(@Argument String id) {
        logger.info("Retrieving user with id {}", id);
        return convertToDTO(service.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public UserMongo createUser(@Argument UserMongo user) {
        logger.info("Creating user {}", user);
        service.save(user);
        return user;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public boolean updateUser(@Argument UserMongo user, @Argument String id) {
        logger.info("Updating user {}", user);
        return service.update(id,user);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @MutationMapping
    public boolean deleteUser(@Argument String id) {
        logger.info("Deleting user with id {}", id);
        return service.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @QueryMapping
    public List<MapStatsSch> getUserStats() {
        Map<String,TrackInfo> map = trackCounterM.getCounter();
        List<MapStatsSch> list = new LinkedList<>();
        for(var entry : map.entrySet()) {
            String date = entry.getValue().getDate();
            list.add(new MapStatsSch(entry.getKey(),entry.getValue().getCt(),date));
        }
        return list;
    }

    @QueryMapping
    public List<MapStatsSch> resetStats() {
        Map<String,TrackInfo> map = trackCounterM.getCounter();
        List<MapStatsSch> list = new LinkedList<>();
        for(var entry : map.entrySet()) {
            String date = entry.getValue().getDate();
            MapStatsSch obj = new MapStatsSch(entry.getKey(), entry.getValue().getCt(),date);
            list.add(obj);
        }
        return list;
    }

    @SubscriptionMapping
    public Flux<String> randomNumber() {
        return Flux.interval(Duration.ofSeconds(3))
                .map(t->String.valueOf(new Random().nextInt(100)));
    }

    @SubscriptionMapping
    public Flux<List<UserDTOMongo>> getUsers() {
        return Flux.interval(Duration.ofSeconds(2))
                .map(t->convertToDTO(service.findAll()));
    }

    @Override
    public UserDTOMongo convertToDTO(UserMongo user) {
        return new UserDTOMongo(user);
    }

    @Override
    public List<UserDTOMongo> convertToDTO(List<UserMongo> users) {
        List<UserDTOMongo> userDTOs = new ArrayList<>();
        for (UserMongo user : users) {
            userDTOs.add(convertToDTO(user));
        }
        return userDTOs;
    }
}
