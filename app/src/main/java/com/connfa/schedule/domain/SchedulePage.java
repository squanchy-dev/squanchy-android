package com.connfa.schedule.domain;

import android.content.Context;

import com.connfa.model.data.Event;
import com.connfa.model.data.Level;
import com.connfa.utils.DateUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class SchedulePage {

    private final Date date;           // TODO use JodaTime

    public SchedulePage(Date date) {
        this.date = date;
    }

    public String formattedTitle(Context context) {
        return DateUtils.getWeekNameAndDate(context, date.getTime());
    }

    public List<Event> events() {
        Event event = new Event(TimeZone.getDefault());
        event.setId(123456);
        event.setDate(new Date());
        event.setName("Test event \uD83C\uDF4C");
        event.setTrack(0);
        event.setSpeakers(Collections.<Long>emptyList());
        event.setFromTime("2017-01-31T08:00:00Z");
        event.setToTime("2017-01-31T08:00:00Z");
        event.setType(0);
        event.setExperienceLevel(Level.INTERMEDIATE);
        return Collections.singletonList(event);
    }
}
