package com.exercise.UserDB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableMongoRepositories(basePackages = "com.exercise.UserDB.repository")
public class UserDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserDbApplication.class, args);
	}

}
