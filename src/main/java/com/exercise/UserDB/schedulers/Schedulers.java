package com.exercise.UserDB.schedulers;

import com.exercise.UserDB.aop.TrackCounterM;
import com.exercise.UserDB.aop.TrackInfo;
import com.exercise.UserDB.aop.TrackCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class Schedulers {

    private static final Logger logger = LoggerFactory.getLogger(Schedulers.class);

    private final TrackCounter trackCounter;
    private final TrackCounterM trackCounterM;

    public Schedulers(TrackCounter trackCounter, TrackCounterM trackCounterM) {
        this.trackCounter = trackCounter;
        this.trackCounterM = trackCounterM;
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void outputCurrentStats() {
        Map<Long, TrackInfo> stats = trackCounter.getCounter();
        if(!stats.isEmpty()) {
            stats.forEach((key, value) -> logger.info("User with id={} has been searched for {} times.", key, value.getCt()));
        }else{
            logger.info("No tracks found.");
        }

    }

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
    public void resetStats() {
        final var counter = trackCounter.updateStats();
        if(!counter.isEmpty()){
            counter.forEach((id,values)->{
                logger.info("User with id {} has the following properties: {}",id,values.toString());
            });
        }else{
            logger.info("No info to display yet!");
        }
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void outputCurrentStatsM() {
        Map<String, TrackInfo> stats = trackCounterM.getCounter();
        if(!stats.isEmpty()) {
            stats.forEach((key, value) -> logger.info("User with id={} has been searched for {} times.", key, value.getCt()));
        }else{
            logger.info("No tracks found.");
        }

    }

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
    public void resetStatsM() {
        final var counter = trackCounterM.updateStats();
        if(!counter.isEmpty()){
            counter.forEach((id,values)->{
                logger.info("User with id {} has the following properties: {}",id,values.toString());
            });
        }else{
            logger.info("No info to display yet!");
        }
    }
}
