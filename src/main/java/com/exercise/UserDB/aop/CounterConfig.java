package com.exercise.UserDB.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class CounterConfig {

    @Bean
    public TrackCounter counter() {
        return new TrackCounter(new ConcurrentHashMap<>());
    }

    @Bean
    public TrackCounterM counterM() {
        return new TrackCounterM(new ConcurrentHashMap<>());
    }
}
