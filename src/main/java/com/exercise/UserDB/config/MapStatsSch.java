package com.exercise.UserDB.config;

public class MapStatsSch {

    private final String id;
    private final int ct;
    private final String date;

    public MapStatsSch(String id, int ct, String date) {
        this.id = id;
        this.ct = ct;
        this.date = date;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MapStatsSch{");
        sb.append("id=").append(id);
        sb.append(", ct=").append(ct);
        sb.append(", date='").append(date).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
