package com.exercise.UserDB.aop;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

public class TrackCounterM {

    private Map<String, TrackInfo> counter;

    public TrackCounterM(Map<String, TrackInfo> counter) {
        this.counter = counter;
    }

    public Map<String, TrackInfo> getCounter() {
        return counter;
    }

    public Map<String,TrackInfo> setCounter(String id, OffsetDateTime dateTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = dateTime.format(formatter);

        TrackInfo trackInfo = counter.getOrDefault(id,new TrackInfo(0,dateTime));
        trackInfo.incrementCt();
        trackInfo.setDate(date);
        counter.put(id,trackInfo);
        return counter;
    }

    public Map<String,TrackInfo> updateStats() {

        OffsetDateTime minusTwoMinutes = OffsetDateTime.now().minusMinutes(2);

        counter =  counter.entrySet().stream()
                .filter(entry->{
                    String date = entry.getValue().getDate();
                    OffsetDateTime dateTime = OffsetDateTime.parse(date);
                    return dateTime.isAfter(minusTwoMinutes);
                })
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
        return counter;
    }

    public void resetCounter() {
        counter.clear();
    }
}
