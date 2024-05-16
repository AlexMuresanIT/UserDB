package com.exercise.UserDB.service;

import com.exercise.UserDB.repository.MUserRepo;
import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class MigrationDB implements CustomTaskChange, ApplicationContextAware {

    private final Logger log = LoggerFactory.getLogger(MigrationDB.class);
    private ApplicationContext applicationContext;
    private ResourceAccessor resourceAccessor;
    private MUserRepo mUserRepo;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void execute(Database database) throws CustomChangeException {
//        mUserRepo.insert(new UserMongo("Alex","alex@email.com","pw1","Medias"));
//        mUserRepo.insert(new UserMongo("Mihai","mihai@email.com","pw2","Piatra Neamt"));
//        mUserRepo.insert(new UserMongo("Dragos","dragos@email.com","pw3","Sighet"));
        log.info("Users added.");
    }

    @Override
    public String getConfirmationMessage() {
        return "Users added";
    }

    @Override
    public void setUp() throws SetupException {
        this.mUserRepo=applicationContext.getBean(MUserRepo.class);
    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {
        this.resourceAccessor = resourceAccessor;
    }

    @Override
    public ValidationErrors validate(Database database) {
        return new ValidationErrors();
    }
}
