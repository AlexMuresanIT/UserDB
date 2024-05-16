package com.exercise.UserDB.config;

import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.ext.mongodb.database.MongoLiquibaseDatabase;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Bean
    public MongoLiquibaseDatabase database() throws DatabaseException {
        return (MongoLiquibaseDatabase) DatabaseFactory.getInstance()
                .openDatabase("mongodb://localhost:27017/UserDBMongo",
                        null,
                        null,
                        null,
                        null);
    }

    @Bean
    public Liquibase liquibase() throws LiquibaseException {
        return new Liquibase("changelog-master.xml",
                new ClassLoaderResourceAccessor(),
                database());
    }
}
