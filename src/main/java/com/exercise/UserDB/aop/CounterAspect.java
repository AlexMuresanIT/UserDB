package com.exercise.UserDB.aop;

import lombok.Getter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Getter
public class CounterAspect {

    private static final Logger logger = LoggerFactory.getLogger(CounterAspect.class);

    private final Map<Long,Map<Integer, LocalDate>> userCounter = new HashMap<>();

    private final Map<String,Map<Integer, LocalDate>> userCounterM = new HashMap<>();

    private final TrackCounter trackCounter;
    private final TrackCounterM trackCounterM;

    public CounterAspect(TrackCounter trackCounter, TrackCounterM trackCounterM) {
        this.trackCounter = trackCounter;
        this.trackCounterM = trackCounterM;
    }

    @Around("execution(* com.exercise.UserDB.controller.UserAPIController.getUserById(..))")
    public Object countUserByIdCounting(ProceedingJoinPoint joinPoint) {
        Long id = (Long) joinPoint.getArgs()[0];
        trackCounter.setCounter(id, OffsetDateTime.now());
        logger.info("User with name {} entered. Total numbers of loggings and lastUpdated {} ",id,trackCounter.getCounter().get(id));
        try {
            return joinPoint.proceed();
        }catch (Throwable e){
            e.printStackTrace();
            return null;
        }
    }

    @Around("execution(* com.exercise.UserDB.controller.UserAPIController.resetUserStats(..))")
    public Object resetUserByIdCounting(ProceedingJoinPoint joinPoint) {
        trackCounter.resetCounter();
        logger.info("Stats were reset to 0...");
        try{
            return joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    @Around("execution(* com.exercise.UserDB.controller.QLController.findUserById(..))")
    public Object countUserByIdCountingQL(ProceedingJoinPoint joinPoint) {
        String id = (String) joinPoint.getArgs()[0];
        trackCounterM.setCounter(id, OffsetDateTime.now());
        logger.info("User with name {} entered. Total numbers of loggings and lastUpdated {} ",id,trackCounterM.getCounter().get(id));
        try {
            return joinPoint.proceed();
        }catch (Throwable e){
            e.printStackTrace();
            return null;
        }
    }

    @Around("execution(* com.exercise.UserDB.controller.QLController.resetStats(..))")
    public Object resetUserByIdCountingQL(ProceedingJoinPoint joinPoint) {
        trackCounterM.resetCounter();
        logger.info("Stats were reset to 0...");
        try{
            return joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    @Around("execution(* com.exercise.UserDB.controller.UserMongoAPIController.getUserById(..))")
    public Object countUserByIdCountingM(ProceedingJoinPoint joinPoint) {
        String id = (String) joinPoint.getArgs()[0];
        trackCounterM.setCounter(id, OffsetDateTime.now());
        logger.info("User with name {} entered. Total numbers of loggings and lastUpdated {} ",id,trackCounterM.getCounter().get(id));
        try {
            return joinPoint.proceed();
        }catch (Throwable e){
            e.printStackTrace();
            return null;
        }
    }

    @Around("execution(* com.exercise.UserDB.controller.UserMongoAPIController.resetUserStatsM(..))")
    public Object resetUserByIdCountingM(ProceedingJoinPoint joinPoint) {
        trackCounterM.resetCounter();
        logger.info("Stats were reset to 0...");
        try{
            return joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
