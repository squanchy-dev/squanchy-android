package com.connfa.schedule.domain;

import android.content.Context;

import com.connfa.model.data.Event;
import com.connfa.model.data.Level;
import com.connfa.utils.DateUtils;
import com.google.auto.value.AutoValue;

import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;

@AutoValue
public abstract class SchedulePage {

    public static SchedulePage create(DateTime date) {
        return new AutoValue_SchedulePage(date, dummyEvents());
    }

    private static List<Event> dummyEvents() {
        Event event = new Event(TimeZone.getDefault());
        event.setId(123456);
        event.setDate(DateTime.now().toDate());
        event.setName("Test event \uD83C\uDF4C");
        event.setTrack(0);
        event.setSpeakers(Collections.<Long>emptyList());
        event.setFromTime("2017-01-31T08:00:00Z");
        event.setToTime("2017-01-31T08:00:00Z");
        event.setType(0);
        event.setExperienceLevel(Level.INTERMEDIATE);
        return Collections.singletonList(event);
    }

    public abstract DateTime date();

    public abstract List<Event> events();

    public String formattedTitle(Context context) {
        return DateUtils.getWeekNameAndDate(context, date().getMillis());
    }
}
