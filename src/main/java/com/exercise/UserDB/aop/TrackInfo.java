package com.exercise.UserDB.aop;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class TrackInfo {

    private int ct;
    private String date;

    public TrackInfo(int ct, OffsetDateTime date) {
        this.ct = ct;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.date = date.format(formatter);
    }

    public void incrementCt(){
        this.ct++;
    }

    public int getCt() {
        return ct;
    }

    public void setCt(int ct) {
        this.ct = ct;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("number of searches: ");
        sb.append(ct);
        sb.append(", last update ").append(date);
        return sb.toString();
    }
}
