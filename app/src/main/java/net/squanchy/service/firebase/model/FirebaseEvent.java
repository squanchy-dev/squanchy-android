package net.squanchy.service.firebase.model;

import java.util.List;

public class FirebaseEvent {

    public Long eventId;

    public String from;

    public String to;

    public Long type;

    public String name;

    public List<Long> speakers;

    public Long track;

    public Integer experienceLevel;

    public String place;

    public String text;

    public String link;

    public Long order;

    public Boolean deleted;

    public String version;

    public static class Holder {

        public List<Day> days;

    }

    public static class Day {

        public String date;

        public List<FirebaseEvent> programEvents;

        public List<FirebaseEvent> bofsEvents;

        public List<FirebaseEvent> events;
    }
}
